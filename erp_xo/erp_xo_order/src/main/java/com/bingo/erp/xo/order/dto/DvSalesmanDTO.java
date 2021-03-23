package com.bingo.erp.xo.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class DvSalesmanDTO {

    List<String> dates;

    List<Salesman30DaysDTO> salesmanDatas;
}
