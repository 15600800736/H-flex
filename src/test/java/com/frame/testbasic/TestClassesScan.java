package com.frame.testbasic;

import com.frame.annotations.ActionClass;
import com.frame.execute.control.Controller;
import com.frame.execute.control.MainController;
import com.frame.context.info.StringInfomation.Configuration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by fdh on 2017/7/27.
 */
@ActionClass
public class TestClassesScan {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Test
    public void testScan() {
        Controller<Boolean> controller = new MainController();
        try {
            controller.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Configuration configuration = ((MainController)controller).configuration;
//        Assert.assertTrue(configuration.isRegisterd());
        Map<String, String> map = configuration.getClassesPathMapper();
        map.entrySet().forEach( es -> System.out.println(es.getKey() + " " + es.getValue()));
    }
}
