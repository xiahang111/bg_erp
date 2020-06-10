package com.bingo.erp.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.mapper.StoreSummaryInfoMapper;
import com.bingo.erp.xo.service.StoreRecordInfoService;
import com.bingo.erp.xo.service.StoreSummaryInfoService;
import com.bingo.erp.xo.vo.StoreSummaryPageVO;
import com.bingo.erp.xo.vo.StoreSummaryVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class StoreSummaryInfoServiceImpl extends SuperServiceImpl<StoreSummaryInfoMapper, StoreSummaryInfo> implements StoreSummaryInfoService {

    @Resource
    private StoreSummaryInfoService storeSummaryInfoService;

    @Resource
    private StoreSummaryInfoMapper storeSummaryInfoMapper;

    @Override
    public IPage<StoreSummaryInfo> getStoreSummaryByPage(StoreSummaryPageVO storeSummaryPageVO) {

        QueryWrapper<StoreSummaryInfo> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(storeSummaryPageVO.getKeyword())) {
            queryWrapper.like("material_name", storeSummaryPageVO.getKeyword());
        }

        if (StringUtils.isNotBlank(storeSummaryPageVO.getOrderBy())) {

            if (storeSummaryPageVO.getDesc()) {
                queryWrapper.orderByDesc(storeSummaryPageVO.getOrderBy());
            } else {
                queryWrapper.orderByAsc(storeSummaryPageVO.getOrderBy());
            }

        } else {
            queryWrapper.orderByAsc("material_name");
        }

        if (null != storeSummaryPageVO.getMaterialColor()) {
            queryWrapper.eq("material_color", storeSummaryPageVO.getMaterialColor());
        }

        //分页查询
        Page<StoreSummaryInfo> page = new Page<>();
        page.setCurrent(storeSummaryPageVO.getCurrentPage());
        page.setSize(storeSummaryPageVO.getPageSize());

        IPage<StoreSummaryInfo> storeSummaryInfoIPage = storeSummaryInfoService.page(page, queryWrapper);

        return storeSummaryInfoIPage;
    }


    @Override
    public void saveStoreSummary(StoreSummaryVO storeSummaryVO) throws Exception {

        //校验
        if (StringUtils.isBlank(storeSummaryVO.getMaterialName()) || StringUtils.isBlank(storeSummaryVO.getSpecification()) ){
            throw new MessageException("信息不能为空哦");
        }
        QueryWrapper<StoreSummaryInfo> queryWrapper = new QueryWrapper<>();

        StoreSummaryInfo storeSummaryInfo ;
        if ( storeSummaryVO.getUid() == null){

            queryWrapper.eq("material_name",storeSummaryVO.getMaterialName());
            queryWrapper.eq("specification",storeSummaryVO.getSpecification());
            queryWrapper.eq("material_color",storeSummaryVO.getMaterialColor());

            storeSummaryInfo = storeSummaryInfoService.getOne(queryWrapper);

            if (null != storeSummaryInfo){
                throw new MessageException("数据库已有此材料，不可新增，请确认！");
            }

            storeSummaryInfo = new StoreSummaryInfo();

        }else {
            queryWrapper.eq("uid",storeSummaryVO.getUid());

            storeSummaryInfo = storeSummaryInfoService.getOne(queryWrapper);
        }


        storeSummaryInfo.setMaterialName(storeSummaryVO.getMaterialName());

        if (null != storeSummaryVO.getMaterialColor()){
            storeSummaryInfo.setMaterialColor(MaterialColorEnums.getByCode(storeSummaryVO.getMaterialColor()));
        }
        if (null != storeSummaryVO.getMaterialNum()){
            storeSummaryInfo.setMaterialNum(storeSummaryVO.getMaterialNum());
        }
        storeSummaryInfo.setSpecification(storeSummaryVO.getSpecification());
        storeSummaryInfo.setUnit(storeSummaryVO.getUnit());
        storeSummaryInfo.setPrice(storeSummaryVO.getPrice());
        storeSummaryInfo.setWeight(storeSummaryVO.getWeight());

        storeSummaryInfo.setTotalWeight(storeSummaryInfo.getWeight().multiply(storeSummaryInfo.getMaterialNum()).setScale(2, BigDecimal.ROUND_HALF_UP));
        storeSummaryInfo.setTotalPrice(storeSummaryInfo.getPrice().multiply(storeSummaryInfo.getMaterialNum()).setScale(2,BigDecimal.ROUND_HALF_UP));

        storeSummaryInfoService.saveOrUpdate(storeSummaryInfo);

    }

    @Override
    @Transactional
    public void deleteStoreSummary(StoreSummaryVO storeSummaryVO) {

        String uid = storeSummaryVO.getUid();

        storeSummaryInfoMapper.deleteById(uid);

    }
}
