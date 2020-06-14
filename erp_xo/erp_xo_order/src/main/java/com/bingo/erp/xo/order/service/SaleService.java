package com.bingo.erp.xo.order.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.xo.order.vo.MaterialVO;

import java.util.List;

public interface SaleService extends SuperService {

    List<MaterialVO> getBySalesMan(String salesman);
}
