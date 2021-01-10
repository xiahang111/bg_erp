package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

import java.util.Date;

@Data
public class StoreOutsidePageVO extends PageInfo<StoreOutsidePageVO> {

    private String materialName;

    private String location;

    private String  materialStatus;

    private Boolean desc;

    private String orderBy;

    private Date beginTime;

    private Date endTime;
}
