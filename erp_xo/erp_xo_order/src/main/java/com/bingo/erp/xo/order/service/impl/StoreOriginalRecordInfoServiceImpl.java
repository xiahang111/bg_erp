package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.base.enums.StoreMaterialStatus;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.StoreOriginalInfo;
import com.bingo.erp.commons.entity.StoreOriginalRecordInfo;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.StoreOriginalInfoMapper;
import com.bingo.erp.xo.order.mapper.StoreOriginalRecordInfoMapper;
import com.bingo.erp.xo.order.service.StoreOriginalInfoService;
import com.bingo.erp.xo.order.service.StoreOriginalRecordInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class StoreOriginalRecordInfoServiceImpl
        extends SuperServiceImpl<StoreOriginalRecordInfoMapper, StoreOriginalRecordInfo> implements StoreOriginalRecordInfoService {

    @Resource
    private StoreOriginalRecordInfoMapper storeOriginalRecordInfoMapper;

    @Resource
    private StoreOriginalInfoService storeOriginalInfoService;

    @Resource
    private StoreOriginalInfoMapper storeOriginalInfoMapper;

    @Resource
    private StoreOriginalRecordInfoService storeOriginalRecordInfoService;


    @Override
    public void callbackStoreRecord(String storeRecordUid) throws Exception {

        StoreOriginalRecordInfo storeOriginalRecordInfo = storeOriginalRecordInfoMapper.selectById(storeRecordUid);

        if (null == storeOriginalRecordInfo) {
            throw new MessageException("无此库存记录信息");
        }

        QueryWrapper<StoreOriginalInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_name", storeOriginalRecordInfo.getMaterialName());
        queryWrapper.eq("specification", storeOriginalRecordInfo.getSpecification());

        StoreOriginalInfo storeOriginalInfo = storeOriginalInfoService.getOne(queryWrapper);

        if (null == storeOriginalInfo) {
            throw new MessageException("无此坯料库存材料信息");
        }

        if (storeOriginalRecordInfo.getMaterialStatus().code == StoreMaterialStatus.IN.code){

            storeOriginalInfo.setTotalWeight(storeOriginalInfo.getTotalWeight().
                    subtract(storeOriginalInfo.getWeight().
                            multiply(storeOriginalRecordInfo.getMaterialNum())).
                    setScale(2, BigDecimal.ROUND_HALF_UP));

            storeOriginalInfo.setTotalPrice(storeOriginalInfo.getTotalPrice().
                    subtract(storeOriginalRecordInfo.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));

            storeOriginalInfo.setMaterialNum(storeOriginalInfo.getMaterialNum().subtract(storeOriginalRecordInfo.getMaterialNum()));
        } else {

            storeOriginalInfo.setTotalWeight(storeOriginalInfo.getTotalWeight().
                    add(storeOriginalInfo.getWeight().
                            multiply(storeOriginalRecordInfo.getMaterialNum())).
                    setScale(2, BigDecimal.ROUND_HALF_UP));

            storeOriginalInfo.setTotalPrice(storeOriginalInfo.getTotalPrice().
                    add(storeOriginalRecordInfo.getTotalPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));

            storeOriginalInfo.setMaterialNum(storeOriginalInfo.getMaterialNum().add(storeOriginalRecordInfo.getMaterialNum()));

        }

        storeOriginalInfoMapper.updateById(storeOriginalInfo);

        storeOriginalRecordInfo.setStatus(SysConf.DELETE_STATUS);

        storeOriginalRecordInfoMapper.updateById(storeOriginalRecordInfo);
    }
}
