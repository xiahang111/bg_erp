package com.bingo.erp.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.commons.entity.DataGather;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.xo.order.mapper.DataGatherMapper;
import com.bingo.erp.xo.order.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
public class IndexDataTask {


    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private DataGatherMapper dataGatherMapper;

    @Scheduled(cron = "30 0/10 * * * ?")
    private void statisticIndexData() {

        log.info("统计首页信息定时任务开始============");

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        List<OrderInfo> orderInfos = orderInfoMapper.selectList(queryWrapper);

        List<OrderInfo> todays = new ArrayList<>();
        List<OrderInfo> month = new ArrayList<>();

        for (OrderInfo orderInfo : orderInfos) {

            if (DateUtils.isToday(orderInfo.getCreateTime())) {
                todays.add(orderInfo);
            }

            if (DateUtils.isThisMonth(orderInfo.getCreateTime())) {
                month.add(orderInfo);
            }

        }

        DataGather dataGather = dataGatherMapper.getLastDataGather();


        if (DateUtils.isToday(dataGather.getCreateTime())) {
            dataGather.setDayOrderNums(new BigDecimal(todays.size()));
            dataGather.setMonthOrderNums(new BigDecimal(month.size()));
            dataGather.setDaySaleNums(getTotalPrice(todays));
            dataGather.setMonthSaleNums(getTotalPrice(month));
            dataGatherMapper.updateById(dataGather);
        } else {
            dataGather = new DataGather();
            dataGather.setDayOrderNums(new BigDecimal(todays.size()));
            dataGather.setMonthOrderNums(new BigDecimal(month.size()));
            dataGather.setDaySaleNums(getTotalPrice(todays));
            dataGather.setMonthSaleNums(getTotalPrice(month));
            dataGatherMapper.insert(dataGather);

        }

        log.info("统计首页信息定时任务结束============");

    }

    private BigDecimal getTotalPrice(List<OrderInfo> orderInfos) {

        BigDecimal totalPirce = new BigDecimal("0");

        for (OrderInfo orderInfo : orderInfos) {

            totalPirce = totalPirce.add(orderInfo.getTotalPrice()).setScale(0, BigDecimal.ROUND_HALF_UP);

        }

        return totalPirce;

    }

}
