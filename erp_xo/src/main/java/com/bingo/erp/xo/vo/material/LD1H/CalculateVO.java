package com.bingo.erp.xo.vo.material.LD1H;

import com.bingo.erp.base.vo.BaseCalculateVO;
import lombok.Data;


@Data
public class CalculateVO extends BaseCalculateVO {

    private Integer productUid;

    private Boolean isHandle;

    private Boolean isLight;

}
