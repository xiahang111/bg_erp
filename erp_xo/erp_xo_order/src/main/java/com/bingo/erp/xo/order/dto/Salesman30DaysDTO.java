package com.bingo.erp.xo.order.dto;

import lombok.Data;

import java.util.List;

/**
 * 销售员30天的销售数据
 */
@Data
public class Salesman30DaysDTO {

    //销售员的名字
    private String salesman;

    //销售员的数据 有30条
    private List<String> datas;
}
