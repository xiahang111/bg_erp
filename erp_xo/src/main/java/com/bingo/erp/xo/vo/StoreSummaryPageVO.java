package com.bingo.erp.xo.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

@Data
public class StoreSummaryPageVO extends PageInfo<StoreSummaryPageVO> {

    private Boolean desc;

    private String orderBy;

    private Integer materialColor;

}
