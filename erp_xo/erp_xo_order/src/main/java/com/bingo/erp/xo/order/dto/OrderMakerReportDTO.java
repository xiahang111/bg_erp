package com.bingo.erp.xo.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderMakerReportDTO {

    //制单人列表
    private List<String> orderMakers;

    //制单量列表
    private List<Integer> orderNums;

}
