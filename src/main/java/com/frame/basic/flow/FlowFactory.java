package com.frame.basic.flow;

import com.frame.execute.Executor;
import com.frame.basic.flow.flows.AppendableLine;
import com.frame.basic.flow.strategies.FactoryStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fdh on 2017/8/24.
 */
public abstract class FlowFactory<P> {

    protected FlowFactory() {

    }

    protected FactoryStrategy<P> strategy;

    public AppendableLine<P> getLine() {
        return strategy.getLine();
    }


    public void setExecutors(Executor<P,?>[][] executors, P production) {
        strategy.buildLine(executors, production);
    }

    static class Test {
        public Map<String, String> map1 = new HashMap<>();
        public Map<String, String> map2 = new HashMap<>();
    }
    public static void main(String[] args) {
        FlowFactory<Test> factory = new SimpleFactory<>();
        Executor[][] executors = new Executor[][]{
                {
                    new Executor<Test, String>() {
                        @Override
                        protected Object exec() throws Exception {
                            this.production.map1.put("1","map1 processed by worker 1");
                            return "map1 processed by worker 1";
                        }
                    }
                    ,new Executor<Test, String>() {
                        @Override
                        protected Object exec() throws Exception {
                            this.production.map2.put("2","map2 processed by worker 1");
                            return "map2 processed by worker 1";
                        }
                    }
                },
                {
                    new Executor<Test, String>() {
                        @Override
                        protected Object exec() throws Exception {
                            this.production.map1.put("3","map1 processed by worker 2");
                            return "map1 processed by worker 2";
                        }
                    }
                    ,new Executor<Test, String>() {
                        @Override
                        protected Object exec() throws Exception {
                            this.production.map2.put("4","map2 processed by worker 2");
                            return "map2 processed by worker 2";
                        }
                    }
                }
        };

        factory.setExecutors(executors,new Test());
        AppendableLine<Test> line = factory.getLine();
        Thread t = new Thread(() -> {
            try {
                line.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        line.close();
        while (line.hasNext()) {
            Test test = line.get();
            System.out.println(test.map1);
            System.out.println(test.map2);
        }
    }
}
