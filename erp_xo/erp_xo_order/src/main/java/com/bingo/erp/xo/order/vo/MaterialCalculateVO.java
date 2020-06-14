package com.bingo.erp.xo.order.vo;

import com.bingo.erp.base.vo.BaseCalculateVO;
import lombok.Data;

@Data
public class MaterialCalculateVO extends BaseCalculateVO {

    private String productUid;

    private Boolean isHandle;

    private Boolean isLight;

    private Integer corner;
}
