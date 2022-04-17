package com.xj;

import com.xj.activiti.listener.pojo.Evection;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关
 */

/**
 * @description: 工作流网关(并行网关)
 * @author: xijie
 * @dte: 2022/4/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiBingXingDemo {



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
 * 排他网关（只有一个可执行，比流程变量粒度细，异常会抛出）
 * 并行网关（都得走）(fork分支和join分支,并行网关不会解析条件，即使有条件也不解析)
 * 包含网关（可以看做排他网关 和 并行网关的结合）
 * 事件网关 （根据事件觉得流程走向，比较少用）
 *
 */

/**
 * 排他网关只选一个true执行，如果多条线true，会选id小的执行
 */
    /**
     * 部署
     */
    @Test
    public void test01() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/evection-bingxing-gateway.bpmn20.xml")
                .name("出差申请单-并行网关").deploy();

        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动
     */
    @Test
    public  void test02(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("evection-bingxing-gateway");
        System.out.println("流程定义id=" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id=" + processInstance.getId());
        System.out.println("流程实例名称=" + processInstance.getName());
        System.out.println("当前活动id=" + processInstance.getActivityId());
    }
    /**
     * 任务处理
     */
    @Test
    public void test04() {
//        String assignee = "zhangsan";
//        Task task = taskService.createTaskQuery()
//                .taskAssignee(assignee)
//                .processDefinitionKey("evection-bingxing-gateway")
//                .singleResult();
//        Evection evection = new Evection();
//        evection.setNum(2d);
//        Map<String,Object> map = new HashMap<>();
//        map.put("evection",evection);
//        taskService.complete(task.getId(),map);

        String assignee = "wangwu";
        Task task = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .processDefinitionKey("evection-bingxing-gateway")
                .singleResult();
        taskService.complete(task.getId());
    }

}
