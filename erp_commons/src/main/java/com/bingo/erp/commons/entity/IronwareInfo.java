package com.bingo.erp.commons.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bingo.erp.base.entity.SuperEntity;
import lombok.Data;

import java.math.BigDecimal;

@TableName("t_ironware_info")
@Data
public class IronwareInfo extends SuperEntity<IronwareInfo> {

    @TableField("order_info_uid")
    private String orderInfouid;

    private String ironwareName;

    private String unit;

    private String ironwareColor;

    private String specification;

    private Integer ironwareNum;

    private BigDecimal price;

    private String remark;

    private BigDecimal totalPrice;

    public IronwareInfo() {
    }

    public IronwareInfo(String orderInfoUid,
                        String ironwareName,
                        String unit,
                        String ironwareColor,
                        String specification,
                        Integer ironwareNum,
                        BigDecimal price,
                        String remark,
                        BigDecimal totalPrice) {
        this.orderInfouid = orderInfoUid;
        this.ironwareName = ironwareName;
        this.unit = unit;
        this.ironwareColor = ironwareColor;
        this.specification = specification;
        this.ironwareNum = ironwareNum;
        this.price = price;
        this.remark = remark;
        this.totalPrice = totalPrice;
    }
}
