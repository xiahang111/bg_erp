package com.bingo.erp.xo.order.service.feign;

import com.bingo.erp.xo.order.dto.StoreDataDTO;
import com.bingo.erp.xo.order.service.DataGatherService;

import java.util.List;

public interface StoreDataService {

    List<StoreDataDTO> getStoreData();


}
