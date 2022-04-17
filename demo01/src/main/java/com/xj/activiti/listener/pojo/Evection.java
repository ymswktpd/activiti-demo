package com.xj.activiti.listener.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 出差申请对象
 * @author: xijie
 * @dte: 2022/4/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evection implements Serializable {

    private long id;
    private String evectionName;
    private double num;
    private Date beginDate;
    private Date endDate;
    private String destination;
    private String reason;

}
