package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.MaterialStatusEnums;
import com.bingo.erp.base.enums.StoreMaterialStatus;
import com.bingo.erp.base.enums.StoreOriginalResource;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.StoreOriginalInfo;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.commons.entity.vo.StoreRecordInfo;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.StoreOriginalInfoMapper;
import com.bingo.erp.xo.order.service.StoreOriginalInfoService;
import com.bingo.erp.xo.order.service.StoreOriginalRecordInfoService;
import com.bingo.erp.xo.order.vo.StoreOriginRecordVO;
import com.bingo.erp.xo.order.vo.StoreOriginalPageVO;
import com.bingo.erp.xo.order.vo.StoreRecordPageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class StoreOriginalInfoServiceImpl
        extends SuperServiceImpl<StoreOriginalInfoMapper, StoreOriginalInfo> implements StoreOriginalInfoService {

    @Resource
    private StoreOriginalInfoService storeOriginalInfoService;

    @Resource
    private StoreOriginalRecordInfoService storeOriginalRecordInfoService;

    @Override
    public IPage<StoreOriginalInfo> getStoreOriginal(StoreOriginalPageVO storeOriginalPageVO) {

        QueryWrapper<StoreOriginalInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("material_name");
        queryWrapper.isNotNull("material_name");

        if (StringUtils.isNotBlank(storeOriginalPageVO.getKeyword())) {
            queryWrapper.like("material_name", storeOriginalPageVO.getKeyword());
        }

        if (StringUtils.isNotBlank(storeOriginalPageVO.getOrderBy())) {

            if (storeOriginalPageVO.getDesc()) {
                queryWrapper.orderByDesc(storeOriginalPageVO.getOrderBy());
            } else {
                queryWrapper.orderByAsc(storeOriginalPageVO.getOrderBy());
            }
        }

        //分页查询
        Page<StoreOriginalInfo> page = new Page<>();
        page.setCurrent(storeOriginalPageVO.getCurrentPage());
        page.setSize(storeOriginalPageVO.getPageSize());

        IPage<StoreOriginalInfo> storeOriginalInfoIPage = storeOriginalInfoService.page(page, queryWrapper);

        return storeOriginalInfoIPage;

    }


    @Override
    public IPage<StoreOriginalRecordInfo> getStoreOriginalRecord(StoreRecordPageVO storeRecordPageVO) {

        QueryWrapper<StoreOriginalRecordInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_material_status", storeRecordPageVO.getMaterialStatus());

        //查询状态为1的数据记录
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        if (StringUtils.isNotBlank(storeRecordPageVO.getKeyword())) {
            queryWrapper.like("material_name", storeRecordPageVO.getKeyword());
        }

        if (StringUtils.isNotBlank(storeRecordPageVO.getOrderBy())) {

            if (storeRecordPageVO.getDesc()) {
                queryWrapper.orderByDesc(storeRecordPageVO.getOrderBy(), "create_time");
            } else {
                queryWrapper.orderByAsc(storeRecordPageVO.getOrderBy());
            }

        } else {
            queryWrapper.orderByAsc("create_time");
        }

        //分页查询
        Page<StoreOriginalRecordInfo> page = new Page<>();
        page.setCurrent(storeRecordPageVO.getCurrentPage());
        page.setSize(storeRecordPageVO.getPageSize());

        IPage<StoreOriginalRecordInfo> storeOriginalRecordInfoIPage = storeOriginalRecordInfoService.page(page, queryWrapper);


        return storeOriginalRecordInfoIPage;
    }

    @Override
    @Transactional
    public void saveStoreOriginalRecord(StoreOriginRecordVO storeOriginRecordVO) throws Exception {

        QueryWrapper<StoreOriginalInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("material_name", storeOriginRecordVO.getMaterialName());
        queryWrapper.eq("specification", storeOriginRecordVO.getSpecification());

        StoreOriginalInfo one = storeOriginalInfoService.getOne(queryWrapper);

        if (null == one) {
            throw new MessageException("坯料库内无此材料，请仔细核对品名与规格!");
        }

        StoreOriginalRecordInfo storeOriginalRecordInfo = new StoreOriginalRecordInfo();

        // 坯料来源
        storeOriginalRecordInfo.setOriginalResource(StoreOriginalResource.getByCode(storeOriginRecordVO.getOriginalResource()));

        storeOriginalRecordInfo.setStoreMaterialStatus(StoreMaterialStatus.getByCode(storeOriginRecordVO.getStoreMaterialStatus()));

        storeOriginalRecordInfo.setMaterialStatus(MaterialStatusEnums.getByCode(storeOriginRecordVO.getMaterialStatus()));

        storeOriginalRecordInfo.setMaterialName(storeOriginRecordVO.getMaterialName());

        storeOriginalRecordInfo.setUnit(storeOriginRecordVO.getUnit());

        storeOriginalRecordInfo.setSpecification(storeOriginRecordVO.getSpecification());

        storeOriginalRecordInfo.setMaterialNum(storeOriginRecordVO.getMaterialNum());

        if (storeOriginRecordVO.getMaterialStatus() == StoreMaterialStatus.IN.code) {

            storeOriginalRecordInfo.setPrice(storeOriginRecordVO.getPrice());

            storeOriginalRecordInfo.setAluPrice(storeOriginRecordVO.getAluPrice());

            storeOriginalRecordInfo.setOrderDate(storeOriginRecordVO.getOrderDate());

            storeOriginalRecordInfo.setWeight(storeOriginRecordVO.getWeight());

            storeOriginalRecordInfo.setTotalWeight(storeOriginRecordVO.getTotalWeight());

            storeOriginalRecordInfo.setTotalPrice(storeOriginRecordVO.getTotalPrice());

            //增加总金额，重新计算单价
            one.setTotalPrice(one.getTotalPrice().add(storeOriginalRecordInfo.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
            one.setTotalWeight(one.getTotalWeight().add(storeOriginalRecordInfo.getTotalWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
            one.setMaterialNum(one.getMaterialNum().add(storeOriginalRecordInfo.getMaterialNum()).setScale(0, BigDecimal.ROUND_HALF_UP));

            //重新计算单价

            //非0校验
            if (one.getMaterialNum().compareTo(new BigDecimal(0)) > 0 ){
                BigDecimal newPrice = one.getTotalPrice().divide(one.getMaterialNum(), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                one.setPrice(newPrice);
            }

        } else {

            //出库
            storeOriginalRecordInfo.setPrice(one.getPrice());

            storeOriginalRecordInfo.setWeight(one.getWeight());

            storeOriginalRecordInfo.setTotalWeight(storeOriginalRecordInfo.getWeight().multiply(storeOriginalRecordInfo.getMaterialNum()).setScale(2, BigDecimal.ROUND_HALF_UP));

            storeOriginalRecordInfo.setTotalPrice(storeOriginalRecordInfo.getPrice().multiply(storeOriginalRecordInfo.getMaterialNum()).setScale(2, BigDecimal.ROUND_HALF_UP));

            //增加总金额，重新计算单价
            one.setTotalPrice(one.getTotalPrice().subtract(storeOriginalRecordInfo.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
            one.setTotalWeight(one.getTotalWeight().subtract(storeOriginalRecordInfo.getTotalWeight()).setScale(2, BigDecimal.ROUND_HALF_UP));
            one.setMaterialNum(one.getMaterialNum().subtract(storeOriginalRecordInfo.getMaterialNum()).setScale(0, BigDecimal.ROUND_HALF_UP));

            if (one.getMaterialNum().compareTo(new BigDecimal(0)) <= 0){
                throw new MessageException("库存不足！不得出库！");
            }
        }



        storeOriginalRecordInfoService.save(storeOriginalRecordInfo);
        storeOriginalInfoService.updateById(one);

    }
}
