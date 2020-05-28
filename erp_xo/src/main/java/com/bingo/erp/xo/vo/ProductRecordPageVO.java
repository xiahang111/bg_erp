package com.bingo.erp.xo.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

import java.util.Date;

@Data
public class ProductRecordPageVO extends PageInfo<ProductRecordPageVO> {

    private String productUid;

    private Date createTime;

}
