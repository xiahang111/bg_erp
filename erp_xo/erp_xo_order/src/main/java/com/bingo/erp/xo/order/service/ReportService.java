package com.bingo.erp.xo.order.service;

import com.bingo.erp.xo.order.dto.*;

import java.util.List;
import java.util.Map;

public interface ReportService {

    OrderMakerReportDTO getOrderMakerData() throws Exception;

    OrderMakerReportDTO getOrderMakerSaleData() throws Exception;

    ReportBarDTO getAllStoreWeightTop() throws Exception;

    ReportBarDTO getAllStorePriceTop() throws Exception;

    ReportBarDTO getStoreNumReport() throws Exception;

    ReportDoubleBarDTO getStoreInOutReport() throws Exception;

    ReportBarDTO getSalesmanNumReport() throws Exception;

    ReportBarDTO getSalesmanPriceReport() throws Exception;

    ReportBarDTO getSalesmanMonthReportByName(String salesmanName) throws Exception;

    List<ReportPieDTO> getOrderTypePie() throws Exception;

    DvOrderNumDTO getDvOrderNum() throws Exception;

    DvOrderCenterDTO getDvOrderCenter() throws Exception;

    List<ReportPieDTO> getCustomerList() throws Exception;

    List<DvSalesmanDTO>  getSalesmanByGroup() throws Exception;

    DvSalesmanDTO getSalesmanData() throws Exception;

    DvSalesmanDTO getGroupSalesmanData() throws Exception;

    Map<String ,List<SaleTopDTO>> getSalesTopData() throws Exception;


}
