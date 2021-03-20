package com.bingo.erp.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.base.enums.OrderStatusEnums;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.entity.OrderProcess;
import com.bingo.erp.commons.entity.OrderProcessAnalyze;
import com.bingo.erp.commons.feign.PersonFeignClient;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.service.OrderProcessAnalyzeService;
import com.bingo.erp.xo.order.service.OrderProcessService;
import com.bingo.erp.xo.order.tools.AnalyzeTools;
import com.bingo.erp.xo.order.vo.CellData;
import com.bingo.erp.xo.order.vo.RowData;
import com.bingo.erp.xo.order.vo.SheetData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebListener {


    @Resource
    private PersonFeignClient personFeignClient;

    @Resource
    private OrderProcessAnalyzeService orderProcessAnalyzeService;

    @Resource
    private AnalyzeTools analyzeTools;

    @Resource
    private OrderProcessService orderProcessService;

    @RabbitListener(queues = "bingo.web")
    public void saveCustomer(String json) {

        log.info("收到rabbitmq信息，准备保存客户信息,json:" + json);

        if (null != json) {

            CustomerVO customerVO = (CustomerVO) JsonUtils.jsonToObject(json, CustomerVO.class);

            personFeignClient.saveCustomerByOrder(customerVO);

            log.info("保存客户信息成功");
        } else {
            log.info("客户信息为null！");
        }


    }

    @RabbitListener(queues = "bingo.web.process.analyze")
    public synchronized void processAnalyze(String json) {

        log.info("收到rabbitmq信息，文件信息:" + json);

        //查询出未解析的文件
        QueryWrapper<OrderProcessAnalyze> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        queryWrapper.orderByAsc("create_time");

        List<OrderProcessAnalyze> orderProcessAnalyzes = orderProcessAnalyzeService.list(queryWrapper);

        if (null == orderProcessAnalyzes || orderProcessAnalyzes.size() <= 0) {
            log.info("没有还未解析的流程表数据!!!");
        }

        for (OrderProcessAnalyze analyze : orderProcessAnalyzes) {

            //将数据值成正在处理的状态
            analyze.setStatus(SysConf.ANALIZE_STATUS);
            orderProcessAnalyzeService.updateById(analyze);

            String fileName = analyze.getFileName();

            //解析excel文件获取数据
            List<SheetData> sheetDatas = analyzeTools.getDataMapXssf("", fileName);

            //获取表格中所有数据并且转换为orderprocess对象
            List<OrderProcess> orderProcesses = anaylzeMapper(sheetDatas);

            List<String> orderIds = new ArrayList<>();

            orderProcesses.stream().forEach(orderProcess -> {
                orderIds.add(orderProcess.getOrderId());
            });

            //根据表格中的orderid获取数据库中的数据
            QueryWrapper<OrderProcess> processQueryWrapper = new QueryWrapper<>();
            processQueryWrapper.in("order_id", orderIds);
            List<OrderProcess> querys = orderProcessService.list(processQueryWrapper);

            //定义需要更新的数据
            List<OrderProcess> updates = new ArrayList<>();

            for (OrderProcess orderProcess : orderProcesses) {

                String orderId = orderProcess.getOrderId();
                List<OrderProcess> filterProcess = querys.stream().filter(orderProcess1 -> {
                    return orderProcess1.getOrderId().equals(orderId);
                }).collect(Collectors.toList());

                if (filterProcess.size() >= 1) {
                    OrderProcess needUpdate = revertUpdate(filterProcess.get(0), orderProcess);
                    updates.add(needUpdate);
                } else {
                    log.info("OrderId:" + orderId + ",在数据库内没有数据");
                    continue;
                }

            }

            if (updates.size() > 0) {
                orderProcessService.updateBatchById(updates);
            } else {
                log.info("没有需要更新的数据!!!");
            }

            analyze.setStatus(SysConf.DELETE_STATUS);
            orderProcessAnalyzeService.updateById(analyze);

        }

    }

    private OrderProcess revertUpdate(OrderProcess needUpdate, OrderProcess orderProcess) {

        if (null != orderProcess) {
            needUpdate.setOrderStatus(orderProcess.getOrderStatus());
        }

        if (null != orderProcess.getOrderDate()) {
            needUpdate.setOrderDate(orderProcess.getOrderDate());
        }

        if (null != orderProcess.getDeliveryDate()) {
            needUpdate.setDeliveryDate(orderProcess.getDeliveryDate());
        }

        if (StringUtils.isNotBlank(orderProcess.getRestPrice())) {
            needUpdate.setRestPrice(orderProcess.getRestPrice());
        }

        if (StringUtils.isNotBlank(orderProcess.getMaterialNum())) {
            needUpdate.setMaterialNum(orderProcess.getMaterialNum());
        }

        if (null != orderProcess.getGlassHave()) {
            needUpdate.setGlassHave(orderProcess.getGlassHave());
        }

        if (null != orderProcess.getGlassArrive()) {
            needUpdate.setGlassArrive(orderProcess.getGlassArrive());
        }

        if (null != orderProcess.getMaterialDate()) {
            needUpdate.setMaterialDate(orderProcess.getMaterialDate());
        }

        if (null != orderProcess.getDigitalDate()) {
            needUpdate.setDigitalDate(orderProcess.getDigitalDate());
        }

        if (null != orderProcess.getDrillDate()) {
            needUpdate.setDrillDate(orderProcess.getDrillDate());
        }

        if (null != orderProcess.getAssembleDate()) {
            needUpdate.setAssembleDate(orderProcess.getAssembleDate());
        }

        if (null != orderProcess.getPackageDate()) {
            needUpdate.setPackageDate(orderProcess.getPackageDate());
        }

        if (StringUtils.isNotBlank(orderProcess.getPackageNum())) {
            needUpdate.setPackageNum(orderProcess.getPackageNum());
        }

        if (StringUtils.isNotBlank(orderProcess.getRemark())) {
            needUpdate.setRemark(orderProcess.getRemark());
        }

        if (StringUtils.isNotBlank(orderProcess.getCustomerAddr())) {
            needUpdate.setCustomerAddr(orderProcess.getCustomerAddr());
        }

        if (StringUtils.isNotBlank(orderProcess.getExpressReal())) {
            needUpdate.setExpressReal(orderProcess.getExpressReal());
        }

        if (StringUtils.isNotBlank(orderProcess.getExpressId())) {
            needUpdate.setExpressId(orderProcess.getExpressId());
        }

        if (null != orderProcess.getDeliveryDateReal()) {
            needUpdate.setDeliveryDateReal(orderProcess.getDeliveryDateReal());
        }

        return needUpdate;

    }

    private List<OrderProcess> anaylzeMapper(List<SheetData> sheetDatas) {

        List<OrderProcess> result = new ArrayList<>();

        for (int i = 0; i < sheetDatas.size(); i++) {

            if (i == 0) {
                result.addAll(revertComplete(sheetDatas.get(i).getRowDatas()));
            }

            if (i == 1) {
                result.addAll(revertNotComplete(sheetDatas.get(i).getRowDatas()));
            }

            if (i == 2) {
                result.addAll(revertMateiral(sheetDatas.get(i).getRowDatas()));
            }

        }

        return result;

    }


    /**
     * 成品料解析方法
     *
     * @param rowDatas
     * @return
     */
    private List<OrderProcess> revertComplete(List<RowData> rowDatas) {

        List<OrderProcess> result = new ArrayList<>();

        for (RowData rowData : rowDatas) {
            List<CellData> cellDatas = rowData.getCellDatas();
            OrderProcess orderProcess = new OrderProcess();
            loop:
            for (CellData cellData : cellDatas) {

                if (cellDatas.size() <= 2 || cellData.getData().equals("进度")) {
                    break loop;
                }

                if (cellData.getCell() == 0 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderStatus(OrderStatusEnums.getByLabel(cellData.getData()));
                }

                if (cellData.getCell() == 2 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderId(cellData.getData());
                }

                if (cellData.getCell() == 3 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderDate(DateUtils.strToDateTime(cellData.getData(), DateUtils.FORMAT_STRING_DAY));
                }

                if (cellData.getCell() == 4 && StringUtils.isNotBlank(cellData.getData())) {
                    try {
                        if (cellData.getData().equals("做好就出")) {
                            orderProcess.setDeliveryDate(new Date());
                        } else {
                            orderProcess.setDeliveryDate(DateUtils.strToDateTime(cellData.getData(), DateUtils.FORMAT_STRING_DAY));
                        }
                    } catch (Exception e) {
                        orderProcess.setDeliveryDate(new Date());
                    }
                }

                if (cellData.getCell() == 5 && StringUtils.isNotBlank(cellData.getData())) {

                    if (SysConf.NO_RESTPRICE.equals(cellData.getData())) {
                        orderProcess.setRestPrice("0");
                    } else {
                        orderProcess.setRestPrice(cellData.getData());
                    }
                }

                if (cellData.getCell() == 6 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderType(cellData.getData());
                }

                if (cellData.getCell() == 7 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setMaterialNum(cellData.getData());
                }

                if (cellData.getCell() == 8 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setGlassHave(cellData.getData().equals("√") ? true : false);
                }

                if (cellData.getCell() == 9) {
                    orderProcess.setGlassHave(StringUtils.isNotBlank(cellData.getData()) ? true : false);
                }

                if (cellData.getCell() == 10 && StringUtils.isNotBlank(cellData.getData())) {


                    try {
                        Date materialDate = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));

                        orderProcess.setMaterialDate(materialDate);
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("当前数据:"+cellData.getData());
                    }


                }

                if (cellData.getCell() == 11 && StringUtils.isNotBlank(cellData.getData())) {

                    Date date = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));

                    orderProcess.setDigitalDate(date);

                }

                if (cellData.getCell() == 12 && StringUtils.isNotBlank(cellData.getData())) {

                    Date date = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));

                    orderProcess.setDrillDate(date);

                }

                if (cellData.getCell() == 13 && StringUtils.isNotBlank(cellData.getData())) {

                    Date date = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));

                    orderProcess.setAssembleDate(date);

                }

                if (cellData.getCell() == 14 && StringUtils.isNotBlank(cellData.getData())) {

                    Date date = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));

                    orderProcess.setPackageDate(date);

                }

                if (cellData.getCell() == 15 && StringUtils.isNotBlank(cellData.getData())) {

                    orderProcess.setPackageNum(cellData.getData());

                }

                if (cellData.getCell() == 16 && StringUtils.isNotBlank(cellData.getData())) {

                    orderProcess.setRemark(cellData.getData());

                }

                if (cellData.getCell() == 17 && StringUtils.isNotBlank(cellData.getData())) {

                    orderProcess.setCustomerName(cellData.getData());

                }

                if (cellData.getCell() == 18 && StringUtils.isNotBlank(cellData.getData())) {

                    orderProcess.setCustomerPhone(cellData.getData());

                }

                if (cellData.getCell() == 19 && StringUtils.isNotBlank(cellData.getData())) {

                    orderProcess.setCustomerAddr(cellData.getData());

                }

                if (cellData.getCell() == 20 && StringUtils.isNotBlank(cellData.getData())) {

                    orderProcess.setExpress(cellData.getData());

                }

                if (cellData.getCell() == 21 && StringUtils.isNotBlank(cellData.getData())) {

                    orderProcess.setExpressReal(cellData.getData());

                }

                if (cellData.getCell() == 22 && StringUtils.isNotBlank(cellData.getData())) {

                    orderProcess.setExpressId(cellData.getData());

                }
            }
            if (StringUtils.isNotBlank(orderProcess.getOrderId())) {
                result.add(orderProcess);
            }
        }

        return result;
    }

    /**
     * 半成品料解析方法
     *
     * @param rowDatas
     * @return
     */
    private List<OrderProcess> revertNotComplete(List<RowData> rowDatas) {

        List<OrderProcess> result = new ArrayList<>();

        for (RowData rowData : rowDatas) {
            List<CellData> cellDatas = rowData.getCellDatas();
            OrderProcess orderProcess = new OrderProcess();
            loop:
            for (CellData cellData : cellDatas) {

                if (cellDatas.size() <= 2 || cellData.getData().equals("进度")) {
                    break loop;
                }

                if (cellData.getCell() == 0 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderStatus(OrderStatusEnums.getByLabel(cellData.getData()));
                }

                if (cellData.getCell() == 2 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderId(cellData.getData());
                }

                if (cellData.getCell() == 3 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderDate(DateUtils.strToDateTime(cellData.getData(), DateUtils.FORMAT_STRING_DAY));
                }

                if (cellData.getCell() == 4 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setDeliveryDate(DateUtils.strToDateTime(cellData.getData(), DateUtils.FORMAT_STRING_DAY));
                }

                if (cellData.getCell() == 5 && StringUtils.isNotBlank(cellData.getData())) {

                    if (SysConf.NO_RESTPRICE.equals(cellData.getData())) {
                        orderProcess.setRestPrice("0");
                    } else {
                        orderProcess.setRestPrice(cellData.getData());
                    }
                }
                if (cellData.getCell() == 6 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderType(cellData.getData());
                }

                if (cellData.getCell() == 7 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setMaterialNum(cellData.getData());
                }
                if (cellData.getCell() == 8 && StringUtils.isNotBlank(cellData.getData())) {

                    Date materialDate = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));
                    orderProcess.setMaterialDate(materialDate);
                }
                if (cellData.getCell() == 9 && StringUtils.isNotBlank(cellData.getData())) {
                    Date date = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));

                    orderProcess.setDigitalDate(date);
                }

                if (cellData.getCell() == 10 && StringUtils.isNotBlank(cellData.getData())) {
                    Date date = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));

                    orderProcess.setDrillDate(date);

                }
                if (cellData.getCell() == 11 && StringUtils.isNotBlank(cellData.getData())) {
                    Date date = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));

                    orderProcess.setAssembleDate(date);
                }
                if (cellData.getCell() == 12 && StringUtils.isNotBlank(cellData.getData())) {
                    Date date = DateUtils.getByMonthDay(Integer.valueOf(cellData.getData().split("\\.")[0]), Integer.valueOf(cellData.getData().split("\\.")[1]));
                    orderProcess.setPackageDate(date);
                }
                if (cellData.getCell() == 13 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setPackageNum(cellData.getData());
                }
                if (cellData.getCell() == 14 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setRemark(cellData.getData());
                }
                if (cellData.getCell() == 15 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setCustomerName(cellData.getData());
                }
                if (cellData.getCell() == 16 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setCustomerPhone(cellData.getData());
                }
                if (cellData.getCell() == 17 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setCustomerAddr(cellData.getData());
                }
                if (cellData.getCell() == 18 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setExpress(cellData.getData());
                }

                if (cellData.getCell() == 19 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setExpressReal(cellData.getData());
                }
                if (cellData.getCell() == 20 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setExpressId(cellData.getData());
                }
                if (cellData.getCell() == 21 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setDeliveryDateReal(DateUtils.strToDateTime(cellData.getData(), DateUtils.FORMAT_STRING_DAY));
                }
            }

            if (StringUtils.isNotBlank(orderProcess.getOrderId())) {
                result.add(orderProcess);
            }

        }

        return result;
    }

    private List<OrderProcess> revertMateiral(List<RowData> rowDatas) {

        List<OrderProcess> result = new ArrayList<>();

        for (RowData rowData : rowDatas) {
            List<CellData> cellDatas = rowData.getCellDatas();
            OrderProcess orderProcess = new OrderProcess();
            loop:
            for (CellData cellData : cellDatas) {

                if (cellDatas.size() <= 2 || cellData.getData().equals("进度")) {
                    break loop;
                }

                if (cellData.getCell() == 0 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderStatus(OrderStatusEnums.getByLabel(cellData.getData()));
                }

                if (cellData.getCell() == 1 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderId(cellData.getData());
                }

                if (cellData.getCell() == 2 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderDate(DateUtils.strToDateTime(cellData.getData(), DateUtils.FORMAT_STRING_DAY));
                }

                if (cellData.getCell() == 3 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setDeliveryDate(DateUtils.strToDateTime(cellData.getData(), DateUtils.FORMAT_STRING_DAY));
                }

                if (cellData.getCell() == 4 && StringUtils.isNotBlank(cellData.getData())) {

                    if (SysConf.NO_RESTPRICE.equals(cellData.getData())) {
                        orderProcess.setRestPrice("0");
                    } else {
                        orderProcess.setRestPrice(cellData.getData());
                    }
                }
                if (cellData.getCell() == 5 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setOrderType(cellData.getData());
                }


                if (cellData.getCell() == 6 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setPackageNum(cellData.getData());
                }
                if (cellData.getCell() == 7 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setRemark(cellData.getData());
                }
                if (cellData.getCell() == 8 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setCustomerName(cellData.getData());
                }
                if (cellData.getCell() == 9 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setCustomerPhone(cellData.getData());
                }
                if (cellData.getCell() == 10 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setCustomerAddr(cellData.getData());
                }
                if (cellData.getCell() == 11 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setExpress(cellData.getData());
                }

                if (cellData.getCell() == 12 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setExpressReal(cellData.getData());
                }
                if (cellData.getCell() == 13 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setExpressId(cellData.getData());
                }
                if (cellData.getCell() == 14 && StringUtils.isNotBlank(cellData.getData())) {
                    orderProcess.setDeliveryDateReal(DateUtils.strToDateTime(cellData.getData(), DateUtils.FORMAT_STRING_DAY));
                }
            }

            if (StringUtils.isNotBlank(orderProcess.getOrderId())) {
                result.add(orderProcess);
            }
        }

        return result;
    }
}























