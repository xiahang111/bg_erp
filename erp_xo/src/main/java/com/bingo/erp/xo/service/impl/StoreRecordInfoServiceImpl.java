package com.bingo.erp.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.base.enums.StoreMaterialResource;
import com.bingo.erp.base.enums.StoreMaterialStatus;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.commons.entity.vo.StoreRecordInfo;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.mapper.StoreRecordInfoMapper;
import com.bingo.erp.xo.mapper.StoreSummaryInfoMapper;
import com.bingo.erp.xo.service.StoreRecordInfoService;
import com.bingo.erp.xo.service.StoreSummaryInfoService;
import com.bingo.erp.xo.vo.StoreRecordPageVO;
import com.bingo.erp.xo.vo.StoreRecordVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class StoreRecordInfoServiceImpl
        extends SuperServiceImpl<StoreRecordInfoMapper, StoreRecordInfo> implements StoreRecordInfoService {


    @Resource
    private StoreRecordInfoMapper storeRecordInfoMapper;

    @Resource
    private StoreSummaryInfoMapper storeSummaryInfoMapper;

    @Resource
    private StoreSummaryInfoService storeSummaryInfoService;

    @Resource
    private StoreRecordInfoService storeRecordInfoService;

    @Override
    public IPage<StoreRecordInfo> getStoreRecord(StoreRecordPageVO storeRecordPageVO) {

        QueryWrapper<StoreRecordInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("material_status", storeRecordPageVO.getMaterialStatus());

        if (StringUtils.isNotBlank(storeRecordPageVO.getKeyword())) {
            queryWrapper.like("material_name", storeRecordPageVO.getKeyword());
        }

        if (StringUtils.isNotBlank(storeRecordPageVO.getOrderBy())) {

            if (storeRecordPageVO.getDesc()) {
                queryWrapper.orderByDesc(storeRecordPageVO.getOrderBy());
            } else {
                queryWrapper.orderByAsc(storeRecordPageVO.getOrderBy());
            }

        } else {
            queryWrapper.orderByAsc("material_name");
        }

        if (null != storeRecordPageVO.getMaterialColor()) {
            queryWrapper.eq("material_color", storeRecordPageVO.getMaterialColor());
        }

        //分页查询
        Page<StoreRecordInfo> page = new Page<>();
        page.setCurrent(storeRecordPageVO.getCurrentPage());
        page.setSize(storeRecordPageVO.getPageSize());

        IPage<StoreRecordInfo> storeRecordInfoIPage = storeRecordInfoService.page(page, queryWrapper);


        return storeRecordInfoIPage;
    }

    @Override
    @Transactional
    public void saveStoreRecord(StoreRecordVO storeRecordVO) throws Exception {


        QueryWrapper<StoreSummaryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_name", storeRecordVO.getMaterialName());
        queryWrapper.eq("material_color", storeRecordVO.getMaterialColor());
        queryWrapper.eq("specification", storeRecordVO.getSpecification());

        StoreSummaryInfo storeSummaryInfo = storeSummaryInfoService.getOne(queryWrapper);

        if (null == storeSummaryInfo) {
            throw new MessageException("库存内没有此材料，请确认或添加！");
        }

        StoreRecordInfo storeRecordInfo = new StoreRecordInfo();
        //出入库状态
        storeRecordInfo.setMaterialStatus(StoreMaterialStatus.getByCode(storeRecordVO.getMaterialStatus()));
        //出入库源
        storeRecordInfo.setMaterialResource(StoreMaterialResource.getByCode(storeRecordVO.getMaterialResource()));
        //材料名称
        storeRecordInfo.setMaterialName(storeRecordVO.getMaterialName());
        //材料颜色
        storeRecordInfo.setMaterialColor(MaterialColorEnums.getByCode(storeRecordVO.getMaterialColor()));
        //材料数量
        storeRecordInfo.setMaterialNum(storeRecordVO.getMaterialNum());
        //材料规格
        storeRecordInfo.setSpecification(storeRecordVO.getSpecification());
        //材料单位
        storeRecordInfo.setUnit(storeSummaryInfo.getUnit());

        //材料单价
        storeRecordInfo.setPrice(storeSummaryInfo.getPrice());

        //材料总价格
        storeRecordInfo.setTotalPrice(storeRecordInfo.getMaterialNum().multiply(storeRecordInfo.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));


        if (storeRecordVO.getMaterialStatus() == StoreMaterialStatus.IN.code) {
            storeSummaryInfo.setTotalPrice(storeRecordInfo.getTotalPrice().add(storeRecordInfo.getTotalPrice()));
            storeSummaryInfo.setTotalWeight(storeSummaryInfo.getTotalWeight().add(storeRecordInfo.getMaterialNum().multiply(storeSummaryInfo.getWeight())));
            storeSummaryInfo.setMaterialNum(storeSummaryInfo.getMaterialNum().add(storeRecordInfo.getMaterialNum()));

        } else {

            if (storeRecordInfo.getMaterialNum().compareTo(storeSummaryInfo.getMaterialNum()) > 0) {
                throw new MessageException("材料不足,请确认！");
            }

            storeSummaryInfo.setTotalPrice(storeRecordInfo.getTotalPrice().subtract(storeRecordInfo.getTotalPrice()));
            storeSummaryInfo.setTotalWeight(storeSummaryInfo.getTotalWeight().subtract(storeRecordInfo.getMaterialNum().multiply(storeSummaryInfo.getWeight())));
            storeSummaryInfo.setMaterialNum(storeSummaryInfo.getMaterialNum().subtract(storeRecordInfo.getMaterialNum()));

        }


        //更新总表
        storeRecordInfoMapper.insert(storeRecordInfo);
        storeSummaryInfoMapper.updateById(storeSummaryInfo);

    }
}
