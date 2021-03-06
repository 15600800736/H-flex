package com.frame.context.info.StringInformation;


/**
 * Created by fdh on 2017/7/17.
 */

/**
 * <p>ActionInfo is a delegate of the basic unit in the frame -- action
 * include all attributes and child nodes.
 * All of the information is a string, wait to be parsed by parser to an Method or something</p>
 */
public class ActionInfo {

    /**

     * <p>the action's method name in its class</p>
     */
    private String name;
    /**
     * <p>the action's id to identify the action, if it isn't specified, it will equals method's full name</p>
     */
    private String id;

    /**
     * <p>the class that the action belongs</p>
     */
    private String actionClass;
    /**
     * <p>the parameter's type, maybe include type alias.</p>
     */
    private String[] param;

    /**
     * <p>The return type of the method</p>
     */
    private String returnType;
    /**
     * <p>Represent if the method has been overloaded</p>
     */
    private Boolean overload = false;

    private ActionInfo(String id) {
        this.id = id;
    }

    public Boolean getOverload() {
        return overload;
    }

    public ActionInfo setOverload(Boolean overload) {
        this.overload = overload;
        return this;
    }

    public String[] getParam() {
        return param;
    }

    public ActionInfo setParam(String[] param) {
        this.param = param;
        return this;
    }


    public String getName() {
        return name;
    }

    public ActionInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public ActionInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getReturnType() {
        return returnType;
    }

    public ActionInfo setReturnType(String returnType) {
        this.returnType = returnType;
        return this;
    }

    public String getActionClass() {
        return actionClass;
    }

    public ActionInfo setActionClass(String actionClass) {
        this.actionClass = actionClass;
        return this;
    }

    public static ActionInfo createActionInfo(String id) {
        return new ActionInfo(id);
    }
}
