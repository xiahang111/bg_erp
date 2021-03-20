package com.bingo.erp.xo.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.xo.data.entity.StoreData;
import com.bingo.erp.xo.data.mapper.StoreDataMapper;
import com.bingo.erp.xo.data.service.StoreDataService;
import com.bingo.erp.xo.data.vo.BarReportResultVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class StoreDataServiceImpl extends SuperServiceImpl<StoreDataMapper,StoreData> implements StoreDataService {


    @Resource
    private StoreDataMapper storeDataMapper;

    @Override
    public List<StoreData> getLastStoreDatas() {

        QueryWrapper<StoreData> queryWrapper = new QueryWrapper<>();

        queryWrapper.ge("create_time",DateUtils.getTodayStartTime());

        List<StoreData> storeDatas = storeDataMapper.selectList(queryWrapper);

        if (CollectionUtil.isEmpty(storeDatas)){
            return Collections.EMPTY_LIST;
        }

        return storeDatas;
    }

    @Override
    public List<BarReportResultVO> getStoreNumReport() {
        return storeDataMapper.getStoreNumReport();
    }
}
