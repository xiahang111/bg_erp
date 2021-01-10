package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.vo.PageInfo;
import lombok.Data;

@Data
public class RemindConfigPageVO extends PageInfo<RemindConfigPageVO> {

    private String materialName;

    private Integer materialColor;
}
