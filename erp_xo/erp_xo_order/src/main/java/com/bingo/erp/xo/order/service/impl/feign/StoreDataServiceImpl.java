package com.bingo.erp.xo.order.service.impl.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.commons.entity.StoreOriginalInfo;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.xo.order.dto.StoreDataDTO;
import com.bingo.erp.xo.order.service.DataGatherService;
import com.bingo.erp.xo.order.service.StoreOriginalInfoService;
import com.bingo.erp.xo.order.service.StoreSummaryInfoService;
import com.bingo.erp.xo.order.service.feign.StoreDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreDataServiceImpl implements StoreDataService {

    @Autowired
    private StoreSummaryInfoService storeSummaryInfoService;

    @Autowired
    private StoreOriginalInfoService storeOriginalInfoService;

    @Override
    public List<StoreDataDTO> getStoreData() {

        List<StoreDataDTO> result = new ArrayList<>();
        //获取所有库存数据
        List<StoreOriginalInfo> storeOriginalInfos = storeOriginalInfoService.list();

        List<StoreSummaryInfo> storeSummaryInfos = storeSummaryInfoService.list();

        StoreDataDTO originDataDTO = new StoreDataDTO("origin");

        StoreDataDTO summaryDataDTO = new StoreDataDTO("summary");

        originDataDTO.setStoreTotalType(storeOriginalInfos.size());
        summaryDataDTO.setStoreTotalType(storeSummaryInfos.size());
        //获取胚料库存相关数据
        storeOriginalInfos.stream().forEach(storeOriginalInfo -> {

            if (storeOriginalInfo.getMaterialNum().intValue() > 0){
                originDataDTO.setStoreTotalNum(originDataDTO.getStoreTotalNum() + storeOriginalInfo.getMaterialNum().intValue());
            }

            if(storeOriginalInfo.getTotalPrice().compareTo(new BigDecimal("0")) > 0){
                originDataDTO.setStoreTotalPrice(originDataDTO.getStoreTotalPrice().add(storeOriginalInfo.getTotalPrice()));
            }

            if (storeOriginalInfo.getTotalWeight().compareTo(new BigDecimal("0")) > 0){
                originDataDTO.setStoreTotalWeight(originDataDTO.getStoreTotalWeight().add(storeOriginalInfo.getTotalWeight()));
            }

        });
        result.add(originDataDTO);

        //获取成品库存数据
        storeSummaryInfos.stream().forEach(storeSummaryInfo -> {
            if (storeSummaryInfo.getMaterialNum().intValue() > 0){
                summaryDataDTO.setStoreTotalNum(summaryDataDTO.getStoreTotalNum() + storeSummaryInfo.getMaterialNum().intValue());
            }

            if(storeSummaryInfo.getTotalPrice().compareTo(new BigDecimal("0")) > 0){
                summaryDataDTO.setStoreTotalPrice(summaryDataDTO.getStoreTotalPrice().add(storeSummaryInfo.getTotalPrice()));
            }

            if (storeSummaryInfo.getTotalWeight().compareTo(new BigDecimal("0")) > 0){
                summaryDataDTO.setStoreTotalWeight(summaryDataDTO.getStoreTotalWeight().add(storeSummaryInfo.getTotalWeight()));
            }
        });
        result.add(summaryDataDTO);

        return result;
    }

}

























