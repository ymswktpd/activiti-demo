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
 * @description: 工作流demo启动类
 * @author: xijie
 * @dte: 2022/4/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiDemo {


    //注入运行任务
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

//    //流程部署
//    @Test
//    public void testActiviti() {
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        Deployment deploy = repositoryService.createDeployment()
//                .addClasspathResource("processes/test.bpmn20.xml")
//                .name("test")
//                .deploy();
//
//        System.out.println(deploy.getId());       //流程定义id
//        System.out.println(deploy.getName());     //流程定义名称
//
//    }


    @Test
    public void testStartProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("test");
        System.out.println("流程定义id=" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id=" + processInstance.getId());
        System.out.println("当前活动id=" + processInstance.getActivityId());
    }

    /**
     * 部署
     */
    @Test
    public void test01() {
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/evection.bpmn20.xml").name("evection").deploy();

        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void test02() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("evection");
        System.out.println("流程定义id=" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id=" + processInstance.getId());
        System.out.println("当前活动id=" + processInstance.getActivityId());
    }

    /**
     * 任务查询
     */
    @Test
    public void test03() {
        String assignee = "lisi";
        List<Task> evection = taskService.createTaskQuery().processDefinitionKey("evection").taskAssignee(assignee).list();
        evection.stream().forEach(x -> {
            System.out.println("流程实例id=" + x.getProcessDefinitionId());
            System.out.println("任务id=" + x.getId());
            System.out.println("任务负责人=" + x.getAssignee());
            System.out.println("任务名称=" + x.getName());
        });
    }

    /**
     * 任务处理
     */
    @Test
    public void test04() {
        String assignee = "lisi";
//        List<Task> evection = taskService.createTaskQuery().processDefinitionKey("evection").taskAssignee(assignee).list();
//        evection.stream().forEach(x -> {
//            taskService.complete(x.getId());
//        });
        Task task = taskService.createTaskQuery().taskAssignee(assignee).processDefinitionKey("evection").singleResult();
        taskService.complete(task.getId());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void test05() {
        List<ProcessDefinition> evection = repositoryService.createProcessDefinitionQuery().processDefinitionKey("evection").orderByProcessDefinitionVersion().desc().list();
        evection.stream().forEach(x -> {
            System.out.println("流程定义id=" + x.getId());
            System.out.println("流程定义name=" + x.getName());
            System.out.println("流程定义key=" + x.getKey());
            System.out.println("流程定义version=" + x.getVersion());
            System.out.println("流程部署id=" + x.getDeploymentId());
        });

    }

    /**
     * 流程删除
     */
    @Test
    public void test06() {
        repositoryService.deleteDeployment("805b585c-be00-11ec-a0fb-8c8caa1eb9d2");
        //已有实例，级联删除，只开放给超级管理员
//        repositoryService.deleteDeployment("805b585c-be00-11ec-a0fb-8c8caa1eb9d2",true);
    }

    /**
     * 流程资源下载（用activiti方法，要加common-io）
     */
    @Test
    public void test07() throws IOException {

        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey("evection").list();
//        InputStream pngInput = repositoryService.getResourceAsStream(processDefinitions.get(1).getDeploymentId(), processDefinitions.get(1).getDiagramResourceName());
        InputStream bpmnInput = repositoryService.getResourceAsStream(processDefinitions.get(1).getDeploymentId(), processDefinitions.get(1).getResourceName());
        //保存文件
//        File filePng = new File("d:/evection.png");
        File fileBpmn = new File("d:/evection.bpmn20.xml");
//        OutputStream pngOut = new FileOutputStream(filePng);
        OutputStream bpmnOut = new FileOutputStream(fileBpmn);

//        IOUtils.copy(pngInput,pngOut);
        IOUtils.copy(bpmnInput, bpmnOut);
//        pngInput.close();
//        pngOut.close();
        bpmnInput.close();
        bpmnOut.close();
    }

    /**
     * 查询历史信息
     */
    @Test
    public void test08(){
        HistoricActivityInstanceQuery activityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        List<HistoricActivityInstance> historicActivityInstanceList = activityInstanceQuery.processDefinitionId("evection:2:84dd0ff5-be00-11ec-a0fb-8c8caa1eb9d2").orderByHistoricActivityInstanceStartTime().desc().list();
        historicActivityInstanceList.stream().forEach(x->{
            System.out.println(x.getActivityId());
            System.out.println(x.getActivityName());
            System.out.println(x.getActivityName());
            System.out.println(x.getActivityType());
            System.out.println(x.getAssignee());
            System.out.println(x.getProcessDefinitionId());
            System.out.println(x.getProcessInstanceId());
            System.out.println("---------------------------------------------------------------");
        });
    }

    /**
     * 业务和流程实例关联（activiti ACT_RU_EXECUTION 中预留businessKey）
     * 启动流程实例添加businessKey
     */
    @Test
    public void test09(){
        //此处可通过businessKey业务id
        ProcessInstance evection = runtimeService.startProcessInstanceByKey("evection", "1001");
        System.out.println("businessKey="+evection.getBusinessKey());
    }

    /**
     * 全部流程挂起 or 激活（流程下实例全部挂起，不允许启动新实例）
     * 挂起后再操作对应的流程实例会抛异常
     */
    @Test
    public void test10(){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("evection").list().get(0);

        //获取状态
        boolean suspended = processDefinition.isSuspended();
        if (suspended) {
            //当前挂起则激活
            repositoryService.activateProcessDefinitionById(processDefinition.getId(),
            true,
            null);
            System.out.println(processDefinition.getId()+"="+"已激活");
        }else{
            //当前激活则挂起
            repositoryService.suspendProcessDefinitionById(
                    processDefinition.getId(),
                    true,null
            );
            System.out.println(processDefinition.getId()+"="+"已挂起");
        }
    }
    /**
     * 单个流程实例挂起（流程下其他实例不影响）id 为ACT_RU_TASK 中PARENT_TASK_ID_
     *
     */
    @Test
    public void test11(){
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId("02fc0bcb-be01-11ec-bdbf-8c8caa1eb9d2").singleResult();

        boolean suspended = instance.isSuspended();
        String id = instance.getId();
        if(suspended){
            runtimeService.activateProcessInstanceById(id);
            System.out.println(id+"激活");
        }else{
            runtimeService.suspendProcessInstanceById(id);
            System.out.println("挂起");
        }

    }

}
