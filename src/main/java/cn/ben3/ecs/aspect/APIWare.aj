package cn.ben3.ecs.aspect;

import com.alibaba.fastjson.JSONObject;

public aspect APIWare {
    pointcut printResponse():
            call(public static com.alibaba.fastjson.JSONObject cn.ben3.ecs.api.API.rebootInstance(..));

    pointcut loginEvent():
            call(public void cn.ben3.ecs.client.controller.LoginController.processLogin(javafx.event.ActionEvent)) ;

    void around():loginEvent(){


    }
    JSONObject around(): printResponse() {
        JSONObject json = proceed();
        System.out.println(json);
        return json;
    }
}