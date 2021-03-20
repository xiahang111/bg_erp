package com.bingo.erp.xo.order.service;

import com.bingo.erp.xo.order.vo.SelectVO;

import java.util.List;

public interface BaseDataService {

    /**
     * 获取制单人列表
     *
     * @return
     * @throws Exception
     */
    List<SelectVO> getAllOrderMaker() throws Exception;

    List<SelectVO> getAllSaleMan() throws Exception;

    List<SelectVO> getAllColor() throws Exception;

    List<SelectVO> getAllOrderType() throws Exception;

    List<SelectVO> getAllOrderStatus() throws Exception;

    List<SelectVO> getAllExpressRecord() throws Exception;
}
