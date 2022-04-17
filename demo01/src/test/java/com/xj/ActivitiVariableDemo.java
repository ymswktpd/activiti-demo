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
 * @description: 流程变量（对象放入流程变量必须实现序列化接口）
 * 流程变量作用域（globa,local同名覆盖）可在连线上使用作为判断，入date<2021-10-10
 * @author: xijie
 * @dte: 2022/4/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiVariableDemo {

    /**
     * 出差申请，部门经理审批后，3天以下财务审批，以上总经理审批后财务审批
     */
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
 * 在分支线上设置条件(对象或变量均可)
 */
    /**
     * 部署
     */
    @Test
    public void test01() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/evection-variable.bpmn20.xml")
                .name("evection-variable").deploy();

        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }
/**
 * 1、启动时设置（变量作用域是整个流程实例）
 * 2、任务办理时设置   （变量作用域是整个流程实例）
 * 3、当前流程实例设置(通过流程实例id设置全局变量，该流程实例必须未执行完成)
 * 4、当前任务设置  taskService.setVariable
 */
    /**
     * 启动流程实例，设置流程变量
     */
    @Test
    public void test02() {
        Map<String, Object> map = new HashMap<>();
        Evection evection = new Evection();
        evection.setNum(2);
        //设置
        map.put("evection", evection);
        //设置assignee
        map.put("assignee0", "张三");
        map.put("assignee1", "李四");
        map.put("assignee2", "王五");
        map.put("assignee3", "赵六");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("evection-variable", map);
        System.out.println("流程定义id=" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id=" + processInstance.getId());
        System.out.println("流程实例名称=" + processInstance.getName());
        System.out.println("当前活动id=" + processInstance.getActivityId());
    }

    /**
     * 张三完成任务，小于3天直接到财务
     */
    @Test
    public void test03() {
        String key = "evection-variable";
        String assignee = "李四";
        Task task = taskService.createTaskQuery().processDefinitionKey(key)
                .taskAssignee(assignee).singleResult();
        if (task != null) {
            taskService.complete(task.getId());
            //2、任务办理时设置
//            taskService.complete(task.getId(),map);
            System.out.println("任务完成。。。。");
        }
    }

    /**
     * 3、当前流程实例设置(通过流程实例id设置全局变量，该流程实例必须未执行完成)
     */
    @Test
    public void setGlobalVariableByExecutionId() {
        //流程实例id
        String exectionId = "92b5b61b-be3c-11ec-bdff-8c8caa1eb9d2";
        Evection evection = new Evection();
        evection.setNum(3d);
        runtimeService.setVariable(exectionId, "evection", evection);
//        runtimeService.setVariables(exectionId,map);

    }


    /**
     * local变量 只能在当前未结束的流程实例任务节点中使用，节点间变量隔离，结束后失效，在history中可见
     * 1、任务办理时设置  taskService.setVariableLocal(taskId,map)
     */

}
