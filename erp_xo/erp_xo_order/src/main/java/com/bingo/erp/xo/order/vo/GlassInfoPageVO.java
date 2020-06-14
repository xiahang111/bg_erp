package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

import java.util.Date;

@Data
public class GlassInfoPageVO extends PageInfo<ProductRecordPageVO> {

    private Integer glassColor;

    private Date beginTime;

    private Date endTime;

}
