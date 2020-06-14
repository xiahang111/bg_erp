package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

@Data
public class StoreRecordPageVO extends PageInfo<StoreRecordPageVO> {

    private Boolean desc;

    private String orderBy;

    private Integer materialColor;

    private Integer materialStatus;

    private Integer materialResource;

}
