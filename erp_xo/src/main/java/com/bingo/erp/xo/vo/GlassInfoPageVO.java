package com.bingo.erp.xo.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GlassInfoPageVO extends PageInfo<ProductRecordPageVO> {

    private Integer glassColor;

    private Date beginTime;

    private Date endTime;

}
