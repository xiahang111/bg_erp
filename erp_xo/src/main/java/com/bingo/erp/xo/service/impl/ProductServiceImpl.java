package com.bingo.erp.xo.service.impl;

import com.bingo.erp.base.enums.CornerEnums;
import com.bingo.erp.base.enums.HandleEnums;
import com.bingo.erp.base.enums.LightEnums;
import com.bingo.erp.base.enums.MaterialEnums;
import com.bingo.erp.base.fatocry.material.MaterialCalculateFactory;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MaterialCalculateRecord;
import com.bingo.erp.commons.entity.Product;
import com.bingo.erp.commons.entity.ProductCalculate;
import com.bingo.erp.commons.entity.ProductCalculateRecord;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.enums.MaterialFactoryEnum;
import com.bingo.erp.xo.factory.LD1HCalculateFactory;
import com.bingo.erp.xo.global.RedisConf;
import com.bingo.erp.xo.mapper.MaterialCalculateRecordMapper;
import com.bingo.erp.xo.mapper.ProductCalculateMapper;
import com.bingo.erp.xo.mapper.ProductCalculateRecordMapper;
import com.bingo.erp.xo.mapper.ProductMapper;
import com.bingo.erp.xo.service.ProductService;
import com.bingo.erp.xo.vo.GlassCalculateResultVO;
import com.bingo.erp.xo.vo.GlassCalculateVO;
import com.bingo.erp.xo.vo.MaterialCalculateResultVO;
import com.bingo.erp.xo.vo.MaterialCalculateVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl extends SuperServiceImpl<ProductMapper, Product> implements ProductService {


    @Autowired
    RedisUtil redisUtil;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductCalculateMapper productCalculateMapper;

    @Resource
    private ProductCalculateRecordMapper productCalculateRecordMapper;

    @Resource
    private MaterialCalculateRecordMapper materialCalculateRecordMapper;

    Gson gson = new Gson();

    @Override
    public List<Product> getAllProducts() {

        return productMapper.selectAllProducts();

    }

    @Override
    public List<GlassCalculateResultVO> glassCalculate(GlassCalculateVO glassCalculateVO) {

        List<Product> productList;

        List<ProductCalculate> productCalculateList;

        List<GlassCalculateResultVO> resultVOS = new ArrayList<>();

        /*
            获取产品信息，主要是获取产品名称
         */
        String productJson = redisUtil.get(RedisConf.ALL_PRODUCT_INFORMATION);


        if (StringUtils.isNotBlank(productJson)) {
            productList = gson.fromJson(productJson, new TypeToken<List<Product>>() {

            }.getType());
        } else {
            productList = productMapper.selectAllProducts();

            redisUtil.set(RedisConf.ALL_PRODUCT_INFORMATION, gson.toJson(productList));
        }

        /*
            获取产品计算方式
         */
        String productGlassCalculateJson = redisUtil.get(RedisConf.ALL_PRODUCT_GLASS_CALCULATE);

        if (StringUtils.isNotBlank(productGlassCalculateJson)) {
            productCalculateList = gson.fromJson(productGlassCalculateJson, new TypeToken<List<ProductCalculate>>() {

            }.getType());
        } else {
            productCalculateList = productCalculateMapper.getAllProductCalculate();

            redisUtil.set(RedisConf.ALL_PRODUCT_GLASS_CALCULATE, gson.toJson(productCalculateList));
        }

        /**
         * 开始计算
         */

        if (!glassCalculateVO.getIsBatch()) {//单个数据

            GlassCalculateResultVO resultVO = glassCalculate(glassCalculateVO.getProductId(), glassCalculateVO.getHeight(), glassCalculateVO.getWidth(), productCalculateList);

            resultVO.setProductName(productList.stream().
                    filter(product -> product.getUid().equals(glassCalculateVO.getProductId())).collect(Collectors.toList()).get(0).getProductName());

            productCalculateRecordMapper.insert(new ProductCalculateRecord(resultVO.getProductUid(), resultVO.getProductName(), resultVO.getWidthResult(), resultVO.getHeightResult()));
            resultVOS.add(resultVO);
        } else {//批量数据

            String batchData = glassCalculateVO.getBatchData();

            String[] datas = batchData.split(";");

            for (String data : datas) {

                BigDecimal height = new BigDecimal(data.split(",")[0]);
                BigDecimal width = new BigDecimal(data.split(",")[1]);

                GlassCalculateResultVO resultVO = glassCalculate(glassCalculateVO.getProductId(), height, width, productCalculateList);

                resultVO.setProductName(productList.stream().
                        filter(product -> product.getUid().equals(glassCalculateVO.getProductId())).collect(Collectors.toList()).get(0).getProductName());


                productCalculateRecordMapper.insert(new ProductCalculateRecord(resultVO.getProductUid(), resultVO.getProductName(), resultVO.getWidthResult(), resultVO.getHeightResult()));
                resultVOS.add(resultVO);
            }

        }

        return resultVOS;


    }

    private GlassCalculateResultVO glassCalculate(String productUid, BigDecimal height, BigDecimal width, List<ProductCalculate> productCalculateList) {

        GlassCalculateResultVO glassCalculateResultVO = new GlassCalculateResultVO(productUid);

        ProductCalculate target = productCalculateList.stream().
                filter(productCalculate -> productCalculate.getProductUid().equals(productUid)).collect(Collectors.toList()).get(0);

        glassCalculateResultVO.setHeightResult(height.subtract(target.getHeight()));
        glassCalculateResultVO.setWidthResult(width.subtract(target.getWidth()));

        return glassCalculateResultVO;

    }

    @Override
    public List<MaterialCalculateResultVO> materialCalculate(MaterialCalculateVO materialCalculateVO) throws Exception {

        List<MaterialCalculateResultVO> results = new ArrayList<>();

        /**
         * 获取工厂类型
         */
        MaterialCalculateFactory factory = (MaterialCalculateFactory) MaterialFactoryEnum.getFactoryClass(Integer.valueOf(materialCalculateVO.getProductUid())).newInstance();

        MaterialCalculateResultVO vo = (MaterialCalculateResultVO) factory.calculate(materialCalculateVO);

        /*
            获取产品信息，主要是获取产品名称
         */

        List<Product> productList;
        String productJson = redisUtil.get(RedisConf.ALL_PRODUCT_INFORMATION);


        if (StringUtils.isNotBlank(productJson)) {
            productList = gson.fromJson(productJson, new TypeToken<List<Product>>() {

            }.getType());
        } else {
            productList = productMapper.selectAllProducts();

            redisUtil.set(RedisConf.ALL_PRODUCT_INFORMATION, gson.toJson(productList));
        }

        vo.setProductName(productList.stream().
                filter(product -> product.getUid().equals(vo.getProductUid())).collect(Collectors.toList()).get(0).getProductName());

        //存入数据库

        MaterialCalculateRecord record = convert(vo);
        //获取产品name
        materialCalculateRecordMapper.insert(record);

        //返回数据
        results.add(vo);

        return results;
    }

    private MaterialCalculateRecord convert(MaterialCalculateResultVO vo) {

        MaterialCalculateRecord record = new MaterialCalculateRecord();

        return record;

    }
}
