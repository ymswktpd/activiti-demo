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
 * @description: 个人业务处理 使用UEL表达式替代用户固定写法（统一表达式）
 * @author: xijie
 * @dte: 2022/4/11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiUELDemo {


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
     * 部署流程
     */
    @Test
    public void test01(){
        Deployment deployment = repositoryService.createDeployment().addClasspathResource("processes/evection-uel.bpmn20.xml").name("evection-uel").deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * uel方式(除此外还有uel-method方式;还有method和value的结合,类似xx.getyy(zz)；还支持解析判断 order.price>500)
     * 创建流程实例，并给uel赋值
     */
    @Test
    public void test02(){
        //设置assignee取值
        Map<String,Object> map = new HashMap<>();
        map.put("assignee0","张三");
        map.put("assignee1","李四");
        map.put("assignee2","王五");
        map.put("assignee3","赵六");
        runtimeService.startProcessInstanceByKey("evection-uel",map);
    }


    /**
     * 除固定分配与uel赋值外，还有监听器分配！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
     * 可以使用监听器完成很多activiti业务
     */
}
