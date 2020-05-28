package com.bingo.erp.xo.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.Product;
import com.bingo.erp.xo.vo.GlassCalculateResultVO;
import com.bingo.erp.xo.vo.GlassCalculateVO;
import com.bingo.erp.xo.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.vo.MaterialCalculateVO;

import java.util.List;
import java.util.Map;

public interface ProductService extends SuperService<Product> {

    List<Product> getAllProducts();

    List<GlassCalculateResultVO> glassCalculate(GlassCalculateVO glassCalculateVO);

    List<MaterialCalculateResultVO> materialCalculate(MaterialCalculateVO materialCalculateVO) throws Exception;



}
