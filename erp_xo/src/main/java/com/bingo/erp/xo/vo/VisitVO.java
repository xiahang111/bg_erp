package com.bingo.erp.xo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VisitVO {

    List<BigDecimal> expectedData;

    List<BigDecimal> actualData;



}
