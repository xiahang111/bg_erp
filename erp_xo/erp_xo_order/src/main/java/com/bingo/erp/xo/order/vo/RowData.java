package com.bingo.erp.xo.order.vo;

import lombok.Data;

import java.util.List;

@Data
public class RowData {

    private int row;

    List<CellData> cellDatas;
}
