package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

@Data
public class StoreOriginalPageVO extends PageInfo<StoreOriginalPageVO> {

    private String orderBy;

    private Boolean desc;
}
