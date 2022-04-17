package com.xj;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.List;

/**
 * 对应evection1文件，每个阶段多个候选人candidateUsers
 */

/**
 * @description: 工作流demo启动类
 * @author: xijie
 * @dte: 2022/4/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiGroupDemo {


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
     * 部署
     */
    @Test
    public void test01() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/evection1.bpmn20.xml")
                .name("出差申请单-组任务").deploy();

        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动
     */
    @Test
    public  void test02(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("evection1");
        System.out.println("流程定义id=" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id=" + processInstance.getId());
        System.out.println("流程实例名称=" + processInstance.getName());
        System.out.println("当前活动id=" + processInstance.getActivityId());
    }



/**
 * 组任务
 * 组任务办理流程
 * 1、查询组任务
 * 2、拾取任务（拾取后不想处理，需要归还回去）
 * 3.查询个人任务
 * 4、办理个人任务
 */

    /**
     * 查询组任务 ******************activiti7添加security 对candidateUser有影响
     */
    @Test
    public void test03(){

        String KEY = "evection1";
        String condidateUser = "zhangsan";
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(KEY)
                .taskCandidateUser(condidateUser).list();

//      查询个人待办          .taskAssignee(condidateUser);
        taskList.stream().forEach(x->{
            System.out.println("流程实例id=" + x.getProcessDefinitionId());
            System.out.println("任务id=" + x.getId());
            System.out.println("任务负责人=" + x.getAssignee());
            System.out.println("任务名称=" + x.getName());
        });
    }

    /**
     * 候选人拾取任务
     */
    @Test
    public void test04(){
        String taskId = "d78f3bc7-be42-11ec-b1e9-8c8caa1eb9d2";
        String userId = "zhangsan";
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(userId)
                .singleResult();
        if(task != null){
            taskService.claim(taskId,userId);
            System.out.println("拾取成功");
        }
    }

        /**
         * 处理完成
         * task.complete
         */

/**
 * 归还
 *
 */
@Test
public void test06(){
    String taskId = "d78f3bc7-be42-11ec-b1e9-8c8caa1eb9d2";
    String userId = "zhangsan";
    Task task = taskService.createTaskQuery()
            .taskId(taskId)
            .taskCandidateUser(userId)
            .singleResult();
    if(task != null){
        //设置负责人为null即为归还
        taskService.setAssignee(taskId,null);
        System.out.println("拾取成功");
    }
}

/**
 * 任务交接，任务负责人将任务交给其他负责人
 */
@Test
public void test07(){
    String taskId = "d78f3bc7-be42-11ec-b1e9-8c8caa1eb9d2";
    String userId = "zhangsan";
    Task task = taskService.createTaskQuery()
            .taskId(taskId)
            .taskCandidateUser(userId)
            .singleResult();
    if(task != null){
        //设置负责人为null即为归还
        taskService.setAssignee(taskId,"zhaoliu");
        System.out.println("拾取成功");
    }
}
}
