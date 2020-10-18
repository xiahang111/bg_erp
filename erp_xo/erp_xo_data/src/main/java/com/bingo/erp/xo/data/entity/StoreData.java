package com.bingo.erp.xo.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("d_store_data")
public class StoreData extends SuperEntity<StoreData> {

    /**
     * 库存种类  origin 坯料数据  summary 成品料数据
     */
    private String storeType;

    /**
     * 仓库总数量
     */
    private int storeTotalNum;

    /**
     * 仓库总价值
     */
    private BigDecimal storeTotalPrice;

    /**
     * 仓库总重量
     */
    private BigDecimal storeTotalWeight;

    /**
     * 仓库总种类数量
     */
    private int storeTotalType;

}
