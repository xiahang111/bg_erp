package com.bingo.erp.xo.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.xo.vo.MaterialVO;

import java.util.List;

public interface SaleService extends SuperService {

    List<MaterialVO> getBySalesMan(String salesman);
}
