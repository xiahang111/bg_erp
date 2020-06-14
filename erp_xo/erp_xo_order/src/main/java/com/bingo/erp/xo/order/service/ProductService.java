package com.bingo.erp.xo.order.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.Product;
import com.bingo.erp.xo.order.vo.GlassCalculateResultVO;
import com.bingo.erp.xo.order.vo.GlassCalculateVO;
import com.bingo.erp.xo.order.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.order.vo.MaterialCalculateVO;


import java.util.List;

public interface ProductService extends SuperService<Product> {

    List<Product> getAllProducts(Integer productType);

    List<GlassCalculateResultVO> glassCalculate(GlassCalculateVO glassCalculateVO);

    List<MaterialCalculateResultVO> materialCalculate(MaterialCalculateVO materialCalculateVO) throws Exception;



}
