package com.bingo.erp.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MaterialCalculateRecord;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.global.SQLConf;
import com.bingo.erp.xo.mapper.MaterialCalculateRecordMapper;
import com.bingo.erp.xo.service.MaterialCalculateRecordService;
import com.bingo.erp.xo.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.vo.ProductRecordPageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialCalculateRecordServiceImpl extends
        SuperServiceImpl<MaterialCalculateRecordMapper, MaterialCalculateRecord> implements MaterialCalculateRecordService {


    @Resource
    private MaterialCalculateRecordService materialCalculateRecordService;

    @Override
    public IPage<MaterialCalculateRecord> getMaterialRecordPage(ProductRecordPageVO productRecordPageVO) {
        QueryWrapper<MaterialCalculateRecord> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(productRecordPageVO.getKeyword()) && StringUtils.isNotEmpty(productRecordPageVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.PRODUCT_NAME, productRecordPageVO.getKeyword().trim());
        }

        if (StringUtils.isNotEmpty(productRecordPageVO.getProductUid())) {
            queryWrapper.eq(SQLConf.PRODUCT_UID, productRecordPageVO.getProductUid());
        }

        //使用时间倒序
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);

        //分页查询
        Page<MaterialCalculateRecord> page = new Page<>();
        page.setCurrent(productRecordPageVO.getCurrentPage());
        page.setSize(productRecordPageVO.getPageSize());

        IPage<MaterialCalculateRecord> pageList = materialCalculateRecordService.page(page, queryWrapper);


        return pageList;
    }
}
