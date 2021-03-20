package com.bingo.erp.xo.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportBarDTO {

    //横坐标
    private List<String> xaxis;

    //纵坐标
    private List<String> yaxis;
}
