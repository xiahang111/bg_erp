package com.bingo.erp.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.commons.entity.DataGather;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.mapper.AdminMapper;
import com.bingo.erp.xo.order.mapper.DataGatherMapper;
import com.bingo.erp.xo.order.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
@Slf4j
public class IndexDataTask {


    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private DataGatherMapper dataGatherMapper;

    @Resource
    private AdminMapper adminMapper;

    @Autowired
    RedisUtil redisUtil;

    @Scheduled(cron = "0 0/2 * * * ?")
    private void statisticIndexData() {

        log.info("统计首页信息定时任务开始============");

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        //获取游客权限的账号
        try {
            String adminInfoJson = redisUtil.get(RedisConf.USER_ADMIN_INFO_KEY);
            log.info("获取到的账户json信息:" + adminInfoJson);
            List<Map<String, String>> adminInfoMapList = (List<Map<String, String>>) JsonUtils.jsonArrayToArrayList(adminInfoJson);
            List<String> adminList = new ArrayList<>();


            for (Map<String, String> map : adminInfoMapList) {
                if (!map.get("roleName").equals("visit")) {
                    adminList.add(map.get("uid"));
                }
            }


            queryWrapper.eq("status", SysConf.NORMAL_STATUS);
            if (adminList.size() > 0) {
                queryWrapper.in("adminUid", adminList);
            }
        } catch (Exception e) {
            log.info("获取用户权限出问题,原因:" + e.getMessage());
        }

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
