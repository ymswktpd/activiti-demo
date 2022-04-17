package com.xj;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 个人业务处理监听器
 * @author: xijie
 * @dte: 2022/4/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiListenerDemo {


    //注入运行任务
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    /**
     * 除固定分配与uel赋值外，还有监听器分配用户数据  ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
     * 可以使用监听器完成很多activiti业务(需要用其他工具配置)
     * 部署，创建流程实例
     */


    /**
     * 任务办理注意权限问题，哪些人能complete哪些任务，注意businessKey
     */
}
