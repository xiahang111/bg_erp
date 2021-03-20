package com.bingo.erp.xo.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportDoubleBarDTO {

    List<String> xAxis;

    List<String> leftAxis;

    List<String> rightAxis;
}
