package com.xj.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @description: 监听器
 * @author: xijie
 * @dte: 2022/4/17
 */
public class MyTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if("创建请假单".equals(delegateTask.getName())
        && "create".equals(delegateTask.getEventName())){
            //指定任务负责人
            delegateTask.setAssignee("张三-Listener");
        }

    }
}
