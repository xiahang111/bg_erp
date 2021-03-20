package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.util.List;

@Data
public class SheetData {

    private Integer sheet;

    List<RowData> rowDatas;

}
