package com.bingo.erp.xo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class MaterialVO {


    /**
     * 是否是净尺寸
     */
    public Boolean isClear;
    /**
     * 产品类型 成品半成品
     */
    public int productType;

    /**
     * 收货人
     */
    public String customerName;

    /**
     * 客户昵称
     */
    public String customerNick;

    /**
     * 收货地址
     */
    public String customerAddr;

    /**
     * 客户手机号
     */
    public String customerPhoneNum;


    /**
     * 快递信息
     */
    public String express;

    /**
     * 材料信息
     */
    public List<MaterialInfoVO> materials;

    /**
     * 五金信息
     */
    public List<IronwareInfoVO> ironwares;

    public int bigPackageNum;

    public int simplePackageNum;

    public Date orderDate;

    public Date deliveryDate;

    /**
     * 订单编号
     */
    public String orderId;

    /**
     * 业务员
     */
    public String salesman;

    /**
     * 制单人
     */
    public String orderMaker;

    /**
     *
     */
    public BigDecimal ironTotalPrice;

    /**
     * 门框玻璃总信息
     */
    public BigDecimal materialTotalprice;
    /**
     * 订单总金额
     */
    public BigDecimal orderTotalPrice;

    /**
     * 总订单备注
     */
    public String remark;

    /**
     * 是否含有天地横梁
     */
    public Boolean isHaveTransom;

    /**
     * 天地横梁信息
     */
    public List<TransomVO> transoms;
}
