package com.bingo.erp.xo.order.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.base.enums.MaterialStatusEnums;
import com.bingo.erp.base.enums.OrderStatusEnums;
import com.bingo.erp.base.enums.OrderTypeEnums;
import com.bingo.erp.commons.entity.CustomerInfo;
import com.bingo.erp.commons.entity.GroupSalesmanInfo;
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
import com.bingo.erp.xo.order.service.*;
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

    @Resource
    private GroupSalesmanService groupSalesmanService;

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


    @Override
    public List<DvSalesmanDTO> getSalesmanByGroup() throws Exception {

        List<DvSalesmanDTO> result = new ArrayList<>();

        List<GroupSalesmanInfo> groupSalesmanInfos = groupSalesmanService.list();

        //获取这些销售员的订单数据
        List<String> dateStrings = DateUtils.getPeroidTime(30);

        //获取最近三十天所有订单的情况

        //获取分组下的人员姓名情况
        Map<String, List<String>> groupMap = new HashMap<>();

        List<String> salesmanNameList = new ArrayList<>();
        for (GroupSalesmanInfo groupSalesmanInfo : groupSalesmanInfos) {

            String groupName = groupSalesmanInfo.getGroupName();
            String salesman = groupSalesmanInfo.getSalesman();
            salesmanNameList.add(salesman);

            List<String> salesmanList = groupMap.get(groupName);
            if (null != salesmanList) {
                salesmanList.add(salesman);
            } else {
                salesmanList = new ArrayList<>();
                salesmanList.add(salesman);
            }
            groupMap.put(groupName, salesmanList);
        }


        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("salesman", salesmanNameList);
        queryWrapper.ge("create_time", DateUtils.getOneDayStartTime(DateUtil.offsetDay(new Date(), -30)));
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        List<OrderInfo> orderInfos = orderService.list(queryWrapper);

        Map<String, List<String>> map = getSalesmanData(dateStrings, salesmanNameList, orderInfos);

        for (String groupName : groupMap.keySet()) {

            DvSalesmanDTO dvSalesmanDTO = new DvSalesmanDTO();
            dvSalesmanDTO.setDates(dateStrings);

            List<Salesman30DaysDTO> salesmanDatas = new ArrayList<>();
            for (String salesmanName : groupMap.get(groupName)) {

                Salesman30DaysDTO salesman30DaysDTO = new Salesman30DaysDTO();
                salesman30DaysDTO.setSalesman(salesmanName);
                salesman30DaysDTO.setDatas(map.get(salesmanName));
                salesmanDatas.add(salesman30DaysDTO);
            }

            if (salesmanDatas.size() < 3) {
                Salesman30DaysDTO salesman30DaysDTO1 = new Salesman30DaysDTO();
                salesman30DaysDTO1.setSalesman("暂无");

                List<String> datas = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    datas.add("0");
                }

                salesman30DaysDTO1.setDatas(datas);
                salesmanDatas.add(salesman30DaysDTO1);
            }
            dvSalesmanDTO.setSalesmanDatas(salesmanDatas);

            result.add(dvSalesmanDTO);
        }

        return result;

    }

    @Override
    public DvSalesmanDTO getGroupSalesmanData() throws Exception {

        DvSalesmanDTO dvSalesmanDTO = new DvSalesmanDTO();

        List<GroupSalesmanInfo> groupSalesmanInfos = groupSalesmanService.list();

        //获取这些销售员的订单数据
        List<String> dateStrings = DateUtils.getPeroidTime(30);

        dvSalesmanDTO.setDates(dateStrings);
        //获取最近三十天所有订单的情况

        //获取分组下的人员姓名情况
        Map<String, List<String>> groupMap = new HashMap<>();

        List<String> salesmanNameList = new ArrayList<>();
        for (GroupSalesmanInfo groupSalesmanInfo : groupSalesmanInfos) {

            String groupName = groupSalesmanInfo.getGroupName();
            String salesman = groupSalesmanInfo.getSalesman();
            salesmanNameList.add(salesman);

            List<String> salesmanList = groupMap.get(groupName);
            if (null != salesmanList) {
                salesmanList.add(salesman);
            } else {
                salesmanList = new ArrayList<>();
                salesmanList.add(salesman);
            }
            groupMap.put(groupName, salesmanList);
        }


        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("salesman", salesmanNameList);
        queryWrapper.ge("create_time", DateUtils.getOneDayStartTime(DateUtil.offsetDay(new Date(), -30)));
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        List<OrderInfo> orderInfos = orderService.list(queryWrapper);

        Map<String, List<String>> map = getSalesmanData(dateStrings, groupMap, orderInfos);


        List<Salesman30DaysDTO> salesmanDatas = new ArrayList<>();
        for (String groupName : map.keySet()) {

            Salesman30DaysDTO salesman30DaysDTO = new Salesman30DaysDTO();
            salesman30DaysDTO.setSalesman(groupName);
            salesman30DaysDTO.setDatas(map.get(groupName));
            salesmanDatas.add(salesman30DaysDTO);
        }

        dvSalesmanDTO.setSalesmanDatas(salesmanDatas);

        return dvSalesmanDTO;
    }

    @Override
    public Map<String, List<SaleTopDTO>> getSalesTopData() throws Exception {

        Map<String, List<SaleTopDTO>> result = new HashMap<>();

        List<GroupSalesmanInfo> groupSalesmanInfos = groupSalesmanService.list();

        //获取分组下的人员姓名情况
        Map<String, List<String>> groupMap = new HashMap<>();

        List<String> salesmanNameList = new ArrayList<>();
        for (GroupSalesmanInfo groupSalesmanInfo : groupSalesmanInfos) {

            String groupName = groupSalesmanInfo.getGroupName();
            String salesman = groupSalesmanInfo.getSalesman();
            salesmanNameList.add(salesman);

            List<String> salesmanList = groupMap.get(groupName);
            if (null != salesmanList) {
                salesmanList.add(salesman);
            } else {
                salesmanList = new ArrayList<>();
                salesmanList.add(salesman);
            }
            groupMap.put(groupName, salesmanList);
        }

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("salesman", salesmanNameList);
        queryWrapper.ge("create_time", DateUtils.getOneDayStartTime(DateUtil.offsetDay(new Date(), -30)));
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        List<OrderInfo> orderInfos = orderService.list(queryWrapper);

        List<SaleTopDTO> persons = new ArrayList<>();

        for (String salesmanName : salesmanNameList) {

            SaleTopDTO saleTopDTO = new SaleTopDTO();

            saleTopDTO.setName(salesmanName);

            List<OrderInfo> salesmanOrders = orderInfos.stream().filter(orderInfo -> {

                return orderInfo.getSalesman().equals(salesmanName);

            }).collect(Collectors.toList());

            saleTopDTO.setPrice(getTotalPriceByOrders(salesmanOrders));

            persons.add(saleTopDTO);

        }

        persons.sort(((o1, o2) -> o2.getPrice().compareTo(o1.getPrice())));

        result.put("person", persons);

        /*List<SaleTopDTO> groups = new ArrayList<>();
        for (String groupName : groupMap.keySet()) {

            SaleTopDTO saleTopDTO = new SaleTopDTO();

            saleTopDTO.setName(groupName);

            List<String> names = groupMap.get(groupName);
            List<OrderInfo> salesmanOrders = orderInfos.stream().filter(orderInfo -> {

                return names.contains(orderInfo.getSalesman());

            }).collect(Collectors.toList());

            saleTopDTO.setPrice(getTotalPriceByOrders(salesmanOrders));

            groups.add(saleTopDTO);

        }

        groups.sort(((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())));
        result.put("group", groups);*/


        return result;
    }

    private Double getTotalPriceByOrders(List<OrderInfo> orderInfos) {

        BigDecimal total = new BigDecimal("0");

        for (OrderInfo orderInfo : orderInfos) {

            if (null != orderInfo.getTotalPrice()) {
                total = total.add(orderInfo.getTotalPrice());
            }

        }

        return total.doubleValue();

    }

    @Override
    public DvSalesmanDTO getSalesmanData() throws Exception {

        List<GroupSalesmanInfo> groupSalesmanInfos = groupSalesmanService.list();

        //获取这些销售员的订单数据
        List<String> dateStrings = DateUtils.getPeroidTime(30);

        List<String> salesmanNameList = new ArrayList<>();

        groupSalesmanInfos.stream().forEach(groupSalesmanInfo -> {
            salesmanNameList.add(groupSalesmanInfo.getSalesman());
        });

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("salesman", salesmanNameList);
        queryWrapper.ge("create_time", DateUtils.getOneDayStartTime(DateUtil.offsetDay(new Date(), -30)));
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        List<OrderInfo> orderInfos = orderService.list(queryWrapper);

        Map<String, List<String>> map = getSalesmanData(dateStrings, salesmanNameList, orderInfos);

        DvSalesmanDTO dvSalesmanDTO = new DvSalesmanDTO();
        dvSalesmanDTO.setDates(dateStrings);

        List<Salesman30DaysDTO> salesmanDatas = new ArrayList<>();
        for (String salesmanName : salesmanNameList) {
            Salesman30DaysDTO salesman30DaysDTO = new Salesman30DaysDTO();
            salesman30DaysDTO.setSalesman(salesmanName);
            salesman30DaysDTO.setDatas(map.get(salesmanName));
            salesmanDatas.add(salesman30DaysDTO);
        }

        dvSalesmanDTO.setSalesmanDatas(salesmanDatas);

        return dvSalesmanDTO;
    }

    /**
     * 将数据转化成为key：组名 value：30天内小组的总数据的形式
     *
     * @param dateStrings
     * @param groupMap
     * @param orderInfos
     * @return
     */
    private Map<String, List<String>> getSalesmanData(List<String> dateStrings, Map<String, List<String>> groupMap, List<OrderInfo> orderInfos) {
        Map<String, List<String>> map = new HashMap<>();

        for (String groupName : groupMap.keySet()) {

            List<String> groupSalesmanNames = groupMap.get(groupName);

            for (String date : dateStrings) {
                List<String> datas = map.get(groupName);
                if (null == datas || datas.size() <= 0) {
                    datas = new ArrayList<>();
                }

                List<OrderInfo> salesmanDatas = orderInfos.stream().filter(orderInfo -> {

                    return groupSalesmanNames.contains(orderInfo.getSalesman()) &&
                            date.equals(DateUtils.formateDate(orderInfo.getCreateTime(), DateUtils.FORMAT_STRING_DAY));
                }).collect(Collectors.toList());

                BigDecimal totalPrice = new BigDecimal("0");
                for (OrderInfo orderInfo : salesmanDatas) {
                    if (null != orderInfo.getTotalPrice()) {
                        totalPrice = totalPrice.add(orderInfo.getTotalPrice());
                    }
                }

                datas.add(totalPrice.toString());
                map.put(groupName, datas);
            }


        }
        return map;

    }

    /**
     * 将订单数据处理成为 key：销售员 value：销售员近30天数据 的形式
     *
     * @param dateStrings
     * @param salesmanNameList
     * @param orderInfos
     * @return
     */
    private Map<String, List<String>> getSalesmanData(List<String> dateStrings, List<String> salesmanNameList, List<OrderInfo> orderInfos) {
        Map<String, List<String>> map = new HashMap<>();

        for (String date : dateStrings) {

            for (String salesman : salesmanNameList) {

                List<String> datas = map.get(salesman);
                if (null == datas || datas.size() <= 0) {
                    datas = new ArrayList<>();
                }
                List<OrderInfo> salesmanDatas = orderInfos.stream().filter(orderInfo -> {

                    return orderInfo.getSalesman().equals(salesman) &&
                            date.equals(DateUtils.formateDate(orderInfo.getCreateTime(), DateUtils.FORMAT_STRING_DAY));

                }).collect(Collectors.toList());


                BigDecimal totalPrice = new BigDecimal("0");
                for (OrderInfo orderInfo : salesmanDatas) {
                    if (null != orderInfo.getTotalPrice()) {
                        totalPrice = totalPrice.add(orderInfo.getTotalPrice());
                    }
                }

                datas.add(totalPrice.toString());
                map.put(salesman, datas);
            }

        }

        return map;

    }
}




























