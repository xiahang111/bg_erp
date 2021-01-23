package com.bingo.erp.xo.order.service.impl.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.utils.DateUtils;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.dto.OrderDTO;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.service.OrderService;
import com.bingo.erp.xo.order.service.feign.OrderDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单数据相关处理类
 */
@Service
public class OrderDataServiceImpl implements OrderDataService {

    @Resource
    private OrderService orderService;

    @Override
    public List<OrderDTO> getOrderByAdminUid(String adminUid) {

        List<OrderDTO> orderDTOS = new ArrayList<>();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("status", SysConf.NORMAL_STATUS);
        queryWrapper.eq("admin_uid", adminUid);
        queryWrapper.orderByDesc("create_time");

        List<OrderInfo> orderInfos = orderService.list(queryWrapper);

        if (null == orderInfos || orderInfos.size() <= 0) {
            return orderDTOS;
        }

        for (OrderInfo orderInfo : orderInfos) {

            OrderDTO dto = new OrderDTO();
            dto.setOrderId(orderInfo.getOrderId());
            if (StringUtils.isNotBlank(orderInfo.getCustomerName())){
                dto.setCustomerName(orderInfo.getCustomerName());
            }

            if (StringUtils.isNotBlank(orderInfo.getOrderMaker())){
                dto.setOrderMaker(orderInfo.getOrderMaker());
            }

            dto.setTotalPrice(orderInfo.getTotalPrice().toString());
            dto.setOrderDate(DateUtils.dateTimeToStr(orderInfo.getCreateTime()));
            dto.setPhoneNum(orderInfo.getCustomerPhoneNum());
            if (StringUtils.isNotBlank(orderInfo.getCustomerAddr())) {
                if (orderInfo.getCustomerAddr().length() > 14) {
                    dto.setCustomerAddr(orderInfo.getCustomerAddr().substring(0, 14) + "...");
                } else {
                    dto.setCustomerAddr(orderInfo.getCustomerAddr());
                }
            }
            orderDTOS.add(dto);
        }

        return orderDTOS;
    }
}
