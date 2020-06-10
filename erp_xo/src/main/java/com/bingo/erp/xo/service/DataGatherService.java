package com.bingo.erp.xo.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.DataGather;
import com.bingo.erp.xo.vo.VisitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
