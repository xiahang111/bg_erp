package com.bingo.erp.xo.order.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.base.enums.MaterialStatusEnums;
import com.bingo.erp.base.enums.OrderStatusEnums;
import com.bingo.erp.base.enums.OrderTypeEnums;
import com.bingo.erp.commons.entity.CustomerInfo;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.OrderProcess;
import com.bingo.erp.commons.feign.DataFeignClient;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.xo.order.dto.*;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.OrderInfoMapper;
import com.bingo.erp.xo.order.mapper.StoreRecordInfoMapper;
import com.bingo.erp.xo.order.mapper.StoreSummaryInfoMapper;
import com.bingo.erp.xo.order.service.CustomerInfoService;
import com.bingo.erp.xo.order.service.OrderProcessService;
import com.bingo.erp.xo.order.service.OrderService;
import com.bingo.erp.xo.order.service.ReportService;
import com.bingo.erp.xo.order.vo.BarReportResultVO;
import com.bingo.erp.xo.order.vo.DoubleBarReportReslutVO;
import com.bingo.erp.xo.order.vo.OrderMakerReportResultVO;
import com.bingo.erp.xo.order.vo.PieResultVO;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {


    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private OrderService orderService;

    @Resource
    private StoreSummaryInfoMapper storeSummaryInfoMapper;

    @Resource
    private StoreRecordInfoMapper storeRecordInfoMapper;

    @Resource
    private DataFeignClient dataFeignClient;

    @Resource
    private CustomerInfoService customerInfoService;

    @Resource
    private OrderProcessService orderProcessService;

    @Override
    public OrderMakerReportDTO getOrderMakerData() throws Exception {

        OrderMakerReportDTO dto = new OrderMakerReportDTO();
        List<OrderMakerReportResultVO> queryResultVO = orderInfoMapper.getOrderMakerReportResult();

        if (null == queryResultVO || queryResultVO.size() <= 0) {
            return dto;
        }
        //制单人列表
        List<String> orderMakers = new ArrayList<>();

        //制单量列表
        List<Integer> orderNums = new ArrayList<>();

        for (OrderMakerReportResultVO reportResultVO : queryResultVO) {
            orderMakers.add(reportResultVO.getOrderMaker());
            orderNums.add(reportResultVO.getCount());
        }

        dto.setOrderMakers(orderMakers);
        dto.setOrderNums(orderNums);

        return dto;
    }

    @Override
    public OrderMakerReportDTO getOrderMakerSaleData() throws Exception {

        OrderMakerReportDTO dto = new OrderMakerReportDTO();
        List<OrderMakerReportResultVO> queryResultVO = orderInfoMapper.getOrderMakerSaleReportResult();
        //制单人列表
        List<String> orderMakers = new ArrayList<>();

        //制单量列表
        List<Integer> orderNums = new ArrayList<>();

        for (OrderMakerReportResultVO reportResultVO : queryResultVO) {
            orderMakers.add(reportResultVO.getOrderMaker());
            orderNums.add(reportResultVO.getCount());
        }

        dto.setOrderMakers(orderMakers);
        dto.setOrderNums(orderNums);

        return dto;
    }

    @Override
    public ReportBarDTO getAllStoreWeightTop() throws Exception {

        ReportBarDTO dto = new ReportBarDTO();

        List<BarReportResultVO> storeWeightTops = storeSummaryInfoMapper.getAllStoreWeightTop();

        if (null == storeWeightTops && storeWeightTops.size() <= 0) {
            return dto;
        }

        //横坐标
        List<String> xaxis = new ArrayList<>();

        //纵坐标
        List<String> yaxis = new ArrayList<>();
        for (BarReportResultVO resultVO : storeWeightTops) {
            xaxis.add(resultVO.getName());
            yaxis.add(resultVO.getValue().toString());

        }

        dto.setXaxis(xaxis);
        dto.setYaxis(yaxis);
        return dto;
    }

    @Override
    public ReportBarDTO getAllStorePriceTop() throws Exception {

        ReportBarDTO dto = new ReportBarDTO();

        List<BarReportResultVO> storePriceTops = storeSummaryInfoMapper.getAllStorePriceTop();

        if (null == storePriceTops && storePriceTops.size() <= 0) {
            return dto;
        }

        //横坐标
        List<String> xaxis = new ArrayList<>();

        //纵坐标
        List<String> yaxis = new ArrayList<>();
        for (BarReportResultVO resultVO : storePriceTops) {
            xaxis.add(resultVO.getName());
            yaxis.add(resultVO.getValue().toString());
        }

        dto.setXaxis(xaxis);
        dto.setYaxis(yaxis);
        return dto;
    }

    @Override
    public ReportBarDTO getStoreNumReport() throws Exception {

        ReportBarDTO reportBarDTO = new ReportBarDTO();

        String dataResultJson = dataFeignClient.getStoreNumReport();

        Map<String, ?> stringMap = JsonUtils.jsonToMap(dataResultJson, HashMap.class);

        List<LinkedTreeMap<String, String>> treeMaps = (List<LinkedTreeMap<String, String>>) JsonUtils.jsonToObject(JsonUtils.objectToJson(stringMap.get("data")), List.class);

        List<BarReportResultVO> barReportResultVOS = new ArrayList<>();

        for (LinkedTreeMap treeMap : treeMaps) {
            BarReportResultVO barReportResultVO = new BarReportResultVO();
            barReportResultVO.setName(treeMap.get("name").toString());
            barReportResultVO.setValue(Double.valueOf(treeMap.get("value").toString()));
            barReportResultVOS.add(barReportResultVO);
        }

        List<String> dates = DateUtils.getPeroidTime(20);

        List<String> yaxis = new ArrayList<>();

        for (String data : dates) {
            List<BarReportResultVO> resultVOS = barReportResultVOS.stream().filter(barReportResultVO -> barReportResultVO.getName().equals(data)).collect(Collectors.toList());

            if (null != resultVOS && resultVOS.size() > 0) {
                yaxis.add(resultVOS.get(0).getValue().toString());
            } else {
                yaxis.add("0");
            }
        }

        reportBarDTO.setXaxis(dates);
        reportBarDTO.setYaxis(yaxis);

        return reportBarDTO;
    }

    @Override
    public ReportDoubleBarDTO getStoreInOutReport() throws Exception {

        ReportDoubleBarDTO dto = new ReportDoubleBarDTO();

        List<String> leftAxis = new ArrayList<>();

        List<String> rightAxis = new ArrayList<>();

        List<String> dates = DateUtils.getPeroidTime(7);

        final List<DoubleBarReportReslutVO> doubleBarReports = storeRecordInfoMapper.getDoubleBarReport();

        for (String date : dates) {

            List<DoubleBarReportReslutVO> reportReslutVOS = doubleBarReports.stream().
                    filter(doubleBarReportReslutVO -> doubleBarReportReslutVO.getName().equals(date)).collect(Collectors.toList());

            if (null == reportReslutVOS || reportReslutVOS.size() <= 0) {

                leftAxis.add("0");
                rightAxis.add("0");
            }

            if (reportReslutVOS.size() == 1) {
                DoubleBarReportReslutVO reportReslutVO = reportReslutVOS.get(0);

                if (reportReslutVO.getValue2().equals(MaterialStatusEnums.STOREIN.code + "")) {
                    leftAxis.add(reportReslutVO.getValue1());
                    rightAxis.add("0");
                } else {
                    rightAxis.add(reportReslutVO.getValue1());
                    leftAxis.add("0");
                }
            }

            if (reportReslutVOS.size() == 2) {
                for (DoubleBarReportReslutVO reportReslutVO : reportReslutVOS) {
                    if (reportReslutVO.getValue2().equals(MaterialStatusEnums.STOREIN.code + "")) {
                        leftAxis.add(reportReslutVO.getValue1());
                    } else {
                        rightAxis.add(reportReslutVO.getValue1());
                    }
                }
            }

        }

        dto.setXAxis(dates);
        dto.setLeftAxis(leftAxis);
        dto.setRightAxis(rightAxis);
        return dto;
    }

    @Override
    public ReportBarDTO getSalesmanNumReport() throws Exception {

        ReportBarDTO dto = new ReportBarDTO();

        List<String> xAxis = new ArrayList<>();

        List<String> yAxis = new ArrayList<>();

        List<DoubleBarReportReslutVO> salesmanTotalReports = orderInfoMapper.getSalesmanTotalReport();

        for (DoubleBarReportReslutVO reportReslutVO : salesmanTotalReports) {
            xAxis.add(reportReslutVO.getName());
            yAxis.add(reportReslutVO.getValue1());
        }

        dto.setXaxis(xAxis);
        dto.setYaxis(yAxis);

        return dto;
    }

    @Override
    public ReportBarDTO getSalesmanPriceReport() throws Exception {
        ReportBarDTO dto = new ReportBarDTO();

        List<String> xAxis = new ArrayList<>();

        List<String> yAxis = new ArrayList<>();

        List<DoubleBarReportReslutVO> salesmanTotalReports = orderInfoMapper.getSalesmanTotalReport();

        for (DoubleBarReportReslutVO reportReslutVO : salesmanTotalReports) {
            xAxis.add(reportReslutVO.getName());
            yAxis.add(reportReslutVO.getValue2());
        }

        dto.setXaxis(xAxis);
        dto.setYaxis(yAxis);

        return dto;
    }

    @Override
    public ReportBarDTO getSalesmanMonthReportByName(String salesmanName) throws Exception {

        ReportBarDTO dto = new ReportBarDTO();

        List<String> yAxis = new ArrayList<>();

        List<String> dates = DateUtils.getPeroidTime(30);

        List<BarReportResultVO> salesmanMonthReports = orderInfoMapper.getSalesmanMonthReport(salesmanName);

        for (String data : dates) {
            List<BarReportResultVO> resultVOS = salesmanMonthReports.stream().filter(barReportResultVO -> barReportResultVO.getName().equals(data)).collect(Collectors.toList());

            if (null != resultVOS && resultVOS.size() > 0) {
                yAxis.add(resultVOS.get(0).getValue().toString());
            } else {
                yAxis.add("0");
            }
        }

        dto.setXaxis(dates);
        dto.setYaxis(yAxis);

        return dto;
    }

    @Override
    public List<ReportPieDTO> getOrderTypePie() throws Exception {

        List<PieResultVO> resultVOS = orderInfoMapper.getOrderTypePie();

        List<ReportPieDTO> report = new ArrayList<>();

        for (PieResultVO pieResultVO : resultVOS) {

            ReportPieDTO dto = new ReportPieDTO();
            dto.setValue(pieResultVO.getValue());
            dto.setName(OrderTypeEnums.getEnumByCode(Integer.valueOf(pieResultVO.getName())));

            report.add(dto);

        }
        return report;
    }

    @Override
    public DvOrderNumDTO getDvOrderNum() throws Exception {

        DvOrderNumDTO dvOrderNumDTO = new DvOrderNumDTO();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        Integer orderNum = orderService.list(queryWrapper).size();

        dvOrderNumDTO.setTotalOrderNum(orderNum + "");

        queryWrapper.ge("create_time", DateUtils.getTodayStartTime());
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        Integer todayOrderNum = orderService.list(queryWrapper).size();

        dvOrderNumDTO.setTodayOrderNum(todayOrderNum + "");

        QueryWrapper<OrderProcess> processQueryWrapper = new QueryWrapper<>();

        processQueryWrapper.notIn("order_status", OrderStatusEnums.COMPLETE.code);

        Integer workingNum = orderProcessService.list(processQueryWrapper).size();

        dvOrderNumDTO.setWorkingOrderNum(workingNum + "");
        dvOrderNumDTO.setCompleteOrderNum(orderNum - workingNum + "");


        return dvOrderNumDTO;
    }

    @Override
    public DvOrderCenterDTO getDvOrderCenter() throws Exception {

        DvOrderCenterDTO dto = new DvOrderCenterDTO();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.ge("create_time", DateUtils.getTodayStartTime());
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);

        List<OrderInfo> orderInfos = orderService.list(queryWrapper);

        dto.setTodayOrderNum(orderInfos.size() + "");
        dto.setTodayOrderPrice(getTotalPrice(orderInfos));

        queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("create_time", DateUtils.getMonthStartTime());
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        orderInfos = orderService.list(queryWrapper);

        dto.setMonthOrderNum(orderInfos.size() + "");
        dto.setMonthOrderPrice(getTotalPrice(orderInfos));

        queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("create_time", DateUtil.beginOfYear(new Date()));
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        orderInfos = orderService.list(queryWrapper);

        dto.setYearOrderNum(orderInfos.size() + "");
        dto.setYearOrderPrice(getTotalPrice(orderInfos));


        return dto;
    }

    private String getTotalPrice(List<OrderInfo> orderInfos) {

        BigDecimal totalPrice = new BigDecimal("0");

        for (OrderInfo orderInfo : orderInfos) {

            if (null != orderInfo.getTotalPrice()) {
                totalPrice = totalPrice.add(orderInfo.getTotalPrice());
            }

        }

        return totalPrice.toString();

    }

    @Override
    public List<ReportPieDTO> getCustomerList() throws Exception {

        List<ReportPieDTO> dtos = new ArrayList<>();

        String customerJson = dataFeignClient.getCustomerTop();

        Map<String, ?> stringMap = JsonUtils.jsonToMap(customerJson, HashMap.class);

        List<LinkedTreeMap<String, String>> treeMaps = (List<LinkedTreeMap<String, String>>) JsonUtils.jsonToObject(JsonUtils.objectToJson(stringMap.get("data")), List.class);

        for (LinkedTreeMap treeMap : treeMaps) {
            ReportPieDTO dto = new ReportPieDTO();
            String customerUid = (String) treeMap.get("customerUid");
            dto.setValue(treeMap.get("totalPrice").toString());
            CustomerInfo customerInfo = customerInfoService.getById(customerUid);
            if (null == customerInfo) {
                dto.setName("未知");
            } else {
                dto.setName(customerInfo.getCustomerNick());
            }

            dtos.add(dto);

        }

        return dtos;
    }
}




























