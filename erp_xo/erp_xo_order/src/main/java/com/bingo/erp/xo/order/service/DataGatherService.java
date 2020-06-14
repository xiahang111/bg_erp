package com.bingo.erp.xo.order.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.DataGather;
import com.bingo.erp.xo.order.vo.VisitVO;


/**
 * 汇总数据表 服务类
 */

public interface DataGatherService extends SuperService<DataGather> {


    /**
     * 获取最近一条数据
     * @return
     */
    public DataGather getLastDataGather();


    public VisitVO getLineData();
}
