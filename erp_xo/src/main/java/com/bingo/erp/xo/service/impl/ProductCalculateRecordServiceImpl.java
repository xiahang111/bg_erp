package com.bingo.erp.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.MaterialInfo;
import com.bingo.erp.commons.entity.Product;
import com.bingo.erp.commons.entity.ProductCalculateRecord;
import com.bingo.erp.commons.entity.vo.GlassInfoVO;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.global.RedisConf;
import com.bingo.erp.xo.global.SQLConf;
import com.bingo.erp.xo.mapper.MaterialInfoMapper;
import com.bingo.erp.xo.mapper.OrderInfoMapper;
import com.bingo.erp.xo.mapper.ProductCalculateRecordMapper;
import com.bingo.erp.xo.mapper.ProductMapper;
import com.bingo.erp.xo.service.MaterialInfoService;
import com.bingo.erp.xo.service.ProductCalculateRecordService;
import com.bingo.erp.xo.vo.GlassCalculateRecordVO;
import com.bingo.erp.xo.vo.GlassInfoPageVO;
import com.bingo.erp.xo.vo.ProductRecordPageVO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCalculateRecordServiceImpl extends
        SuperServiceImpl<ProductCalculateRecordMapper, ProductCalculateRecord> implements ProductCalculateRecordService {


    @Autowired
    RedisUtil redisUtil;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductCalculateRecordMapper productCalculateRecordMapper;


    @Resource
    private ProductCalculateRecordService productCalculateRecordService;

    @Resource
    private MaterialInfoMapper materialInfoMapper;

    @Resource
    private MaterialInfoService materialInfoService;

    @Resource
    private OrderInfoMapper orderInfoMapper;


    Gson gson = new Gson();

    @Override
    public List<GlassCalculateRecordVO> getAllGlassCalculateRecord() {

        List<GlassCalculateRecordVO> recordVOS = new ArrayList<>();

        List<Product> productList;
        /*
            获取产品信息，主要是获取产品名称
         */
        String productJson = redisUtil.get(RedisConf.ALL_PRODUCT_INFORMATION);


        if (StringUtils.isNotBlank(productJson)) {
            productList = gson.fromJson(productJson, new TypeToken<List<Product>>() {

            }.getType());
        } else {
            productList = productMapper.selectAllProducts(1);

            redisUtil.set(RedisConf.ALL_PRODUCT_INFORMATION, gson.toJson(productList));
        }

        List<ProductCalculateRecord> productCalculateRecords = productCalculateRecordMapper.getAllGlassCalculateRecord();

        productCalculateRecords.stream().forEach(record -> {
            GlassCalculateRecordVO vo = new GlassCalculateRecordVO(record);
            vo.setProductName(productList.stream().
                    filter(product -> product.getUid().equals(vo.getProductUid())).collect(Collectors.toList()).get(0).getProductName());

            recordVOS.add(vo);
        });

        return recordVOS;

    }

    @Override
    public IPage<ProductCalculateRecord> getGlassRecordPage(ProductRecordPageVO productRecordPageVO) {

        QueryWrapper<ProductCalculateRecord> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(productRecordPageVO.getKeyword()) && StringUtils.isNotEmpty(productRecordPageVO.getKeyword().trim())) {
            queryWrapper.like(SQLConf.PRODUCT_NAME, productRecordPageVO.getKeyword().trim());
        }

        if (StringUtils.isNotEmpty(productRecordPageVO.getProductUid())) {
            queryWrapper.eq(SQLConf.PRODUCT_UID, productRecordPageVO.getProductUid());
        }

        //使用时间倒序
        queryWrapper.orderByDesc(SQLConf.CREATE_TIME);

        //分页查询
        Page<ProductCalculateRecord> page = new Page<>();
        page.setCurrent(productRecordPageVO.getCurrentPage());
        page.setSize(productRecordPageVO.getPageSize());

        IPage<ProductCalculateRecord> pageList = productCalculateRecordService.page(page, queryWrapper);


        return pageList;
    }


    @Override
    public IPage<GlassInfoVO> getGlassInfo(GlassInfoPageVO glassInfoPageVO) {


        QueryWrapper<MaterialInfo> queryWrapper = new QueryWrapper<>();


        if (null != glassInfoPageVO.getBeginTime() && null != glassInfoPageVO.getEndTime()) {
            queryWrapper.ge("create_time", glassInfoPageVO.getBeginTime());
            queryWrapper.le("create_time", glassInfoPageVO.getEndTime());
        }

        if (null != glassInfoPageVO.getGlassColor() && glassInfoPageVO.getGlassColor() != 0) {
            queryWrapper.eq("glass_color", glassInfoPageVO.getGlassColor());
        }
        queryWrapper.ne("glass_color",0);
        queryWrapper.orderByDesc("create_time");

        //分页查询
        Page<MaterialInfo> page = new Page<>();
        page.setCurrent(glassInfoPageVO.getCurrentPage());
        page.setSize(glassInfoPageVO.getPageSize());
        IPage<MaterialInfo> materialInfoIPage = materialInfoService.page(page, queryWrapper);

        List<GlassInfoVO> glassInfoVOS = new ArrayList<>();
        List<MaterialInfo> materialInfos = materialInfoIPage.getRecords();

        materialInfos.stream().forEach(materialInfo -> {
            String orderInfoUid = materialInfo.getOrderInfouId();

            String orderId = orderInfoMapper.getOrderIdByUid(orderInfoUid);

            GlassInfoVO glassInfoVO = new GlassInfoVO(orderId,
                    materialInfo.getGlassHeight(),
                    materialInfo.getGlassWidth(),
                    materialInfo.getGlassColor().name,
                    materialInfo.getMaterialNum(),
                    materialInfo.getCreateTime());

            glassInfoVOS.add(glassInfoVO);

        });

        Page<GlassInfoVO> glassInfoVOPage = new Page<>();
        glassInfoVOPage.setRecords(glassInfoVOS);
        glassInfoVOPage.setSize(materialInfoIPage.getSize());
        glassInfoVOPage.setCurrent(materialInfoIPage.getCurrent());
        glassInfoVOPage.setOrders(materialInfoIPage.orders());
        glassInfoVOPage.setTotal(materialInfoIPage.getTotal());


        return glassInfoVOPage;
    }
}
