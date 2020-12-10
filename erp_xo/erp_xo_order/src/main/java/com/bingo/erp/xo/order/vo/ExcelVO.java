package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.util.List;

@Data
public class ExcelVO {

    private Integer totalRow;

    private Integer totalCell;

    List<InfoVO> infoVOs;

}
