package com.bingo.erp.xo.order.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.bingo.erp.base.enums.OrderTypeEnums;
import com.bingo.erp.base.enums.ProductTypeEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.commons.entity.DeskInfo;
import com.bingo.erp.commons.entity.IronwareInfo;
import com.bingo.erp.commons.entity.MetalInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.xo.order.global.ExcelConf;
import com.bingo.erp.xo.order.mapper.OrderInfoMapper;
import com.bingo.erp.xo.order.service.*;
import com.bingo.erp.xo.order.tools.AnalyzeTools;
import com.bingo.erp.xo.order.tools.OrderTools;
import com.bingo.erp.xo.order.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Value(value = "${srcFileUrl}")
    private String SRC_FILE_URL;

    @Resource
    private AnalyzeTools analyzeTools;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private IronwareInforService ironwareInforService;

    @Resource
    private DeskInfoService deskInfoService;

    @Resource
    private MetalInfoService metalInfoService;

    @Resource
    private OrderTools orderTools;

    @Override
    public List<String> fileAnalyze(String adminUid, MultipartFile file) throws Exception {

        List<String> result = new ArrayList<>();

        String suffix = DateUtils.formateDate(new Date(), DateUtils.DAYFORMAT_STRING) + RandomUtil.randomNumbers(6) + ExcelConf.FILE_SUFFIX;

        String fileName = "报价单" + suffix;

        File saveFile = new File(SRC_FILE_URL + fileName);
        file.transferTo(saveFile);

        Map<Integer, Map<Integer, String>> dataMap = analyzeTools.getDataMap(SRC_FILE_URL, fileName);

        if(null == dataMap){
            throw new MessageException("解析报价单失败");
        }

        Integer orderType = analyzeTools.analyzeOrderType(dataMap);

        if(null != orderType && orderType == OrderTypeEnums.METAL.code){
            saveMetalOrder(adminUid,dataMap);
        }if(null != orderType && orderType == OrderTypeEnums.DESK.code){
            saveDeskOrder(adminUid,dataMap);
        }
        else {
            return getDoorLaminate(adminUid,dataMap);
        }

        return result;

    }


    public void saveDeskOrder(String adminUid,Map<Integer, Map<Integer, String>> dataMap)throws Exception{
        ProductVO productVO = analyzeTools.analyzeOrderInfo(dataMap,OrderTypeEnums.DESK.code);

        OrderInfo orderInfo = analyzeTools.castOrderInfo(productVO,adminUid);
        orderInfo.setOrderType(OrderTypeEnums.DESK);
        orderInfo.setProductType(ProductTypeEnums.Other);


        orderInfoMapper.insert(orderInfo);
        Map<String ,Object> map = analyzeTools.analyzeDesk(dataMap,orderInfo.getUid());
        List<IronwareInfo> ironwareInfos = (List<IronwareInfo>) map.get("irons");
        List<DeskInfo> deskInfos = (List<DeskInfo>) map.get("desks");

        if(deskInfos.size() > 0 ){
            deskInfoService.saveBatch(deskInfos);
        }
        if (ironwareInfos.size() > 0){
            ironwareInforService.saveBatch(ironwareInfos);
        }
        //保存客户信息并且保存订单客户关系
        orderTools.saveCustomer(adminUid,productVO,orderInfo);
    }

    /**
     * 保存料单
     * @param adminUid
     * @param dataMap
     */
    public void saveMetalOrder(String adminUid,Map<Integer, Map<Integer, String>> dataMap) throws Exception{

        ProductVO productVO = analyzeTools.analyzeOrderInfo(dataMap,OrderTypeEnums.METAL.code);

        OrderInfo orderInfo = analyzeTools.castOrderInfo(productVO,adminUid);
        orderInfo.setOrderType(OrderTypeEnums.METAL);
        orderInfo.setProductType(ProductTypeEnums.Other);


        orderInfoMapper.insert(orderInfo);

        Map<String ,Object> map = analyzeTools.analyzeMetal(dataMap,orderInfo.getUid());
        List<MetalInfo> metalInfos = (List<MetalInfo>) map.get("metals");
        List<IronwareInfo> ironwareInfos = (List<IronwareInfo>) map.get("irons");

        if (metalInfos.size() > 0){
            metalInfoService.saveBatch(metalInfos);
        }

        if (ironwareInfos.size() > 0){
            ironwareInforService.saveBatch(ironwareInfos);
        }
        //保存客户信息并且保存订单客户关系
        orderTools.saveCustomer(adminUid,productVO,orderInfo);
    }

    /**
     * 计算门单\层板灯单
     * @param adminUid
     * @param dataMap
     * @return
     * @throws Exception
     */
    private List<String> getDoorLaminate(String adminUid,Map<Integer, Map<Integer, String>> dataMap) throws Exception{

        List<String> result = new ArrayList<>();

        //解析玻璃门信息
        List<MaterialInfoVO> materialInfoVOS = analyzeTools.analyzeMaterialInfo(dataMap);

        //解析层板信息
        List<LaminateInfoVO> laminateInfoVOS = analyzeTools.analyzeLaminate(dataMap);

        //解析五金信息
        List<IronwareInfoVO> ironwareInfoVOS = analyzeTools.analyzeIronInfos(dataMap);

        //解析天地横梁信息
        List<TransomVO> transomVOS = analyzeTools.analyzeTransomVO(dataMap);

        ProductVO productVO = analyzeTools.analyzeOrderInfo(dataMap,OrderTypeEnums.DOORORDER.code);

        if (laminateInfoVOS.size() > 0 && materialInfoVOS.size() > 0) {

            throw new MessageException("暂时不支持层板玻璃门同时存在");

        }

        if (laminateInfoVOS.size() > 0) {
            LaminateVO laminateVO = (LaminateVO) analyzeTools.castProduct(productVO,2);
            laminateVO.setOrderType(OrderTypeEnums.CBDORDER.code);
            laminateVO.setIronwares(ironwareInfoVOS);
            laminateVO.setLaminateInfos(laminateInfoVOS);
            result = orderService.saveCBDOrder(adminUid, laminateVO);
        }

        if (materialInfoVOS.size() > 0) {
            MaterialVO materialVO = (MaterialVO) analyzeTools.castProduct(productVO,1);
            materialVO.setOrderType(OrderTypeEnums.DOORORDER.code);
            materialVO.setIronwares(ironwareInfoVOS);
            materialVO.setMaterials(materialInfoVOS);
            if (transomVOS.size()>0){
                materialVO.setIsHaveTransom(true);
                materialVO.setTransoms(transomVOS);
            }
            result = orderService.saveOrder(adminUid, materialVO);
        }

        return result;
    }


}
