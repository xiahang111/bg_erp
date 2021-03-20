package com.bingo.erp.xo.data.service.impl;

import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.data.dto.CustomerTopDTO;
import com.bingo.erp.xo.data.entity.CustomerOrder;
import com.bingo.erp.xo.data.mapper.CustomerOrderMapper;
import com.bingo.erp.xo.data.service.CustomerOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CustomerOrderServiceImpl extends SuperServiceImpl<CustomerOrderMapper,CustomerOrder> implements CustomerOrderService {

    @Resource
    private CustomerOrderMapper customerOrderMapper;

    @Override
    public void saveCustomerOrder(CustomerVO customerVO) {

        if (StringUtils.isBlank(customerVO.getCustomerUid())){
            log.error("保存客户订单映射数据失败,没有客户id");
            return;
        }

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomerUid(customerVO.getCustomerUid());
        customerOrder.setOrderUid(customerVO.getOrderUid());
        customerOrder.setOrderId(customerVO.getOrderId());
        customerOrder.setTotalPrice(customerVO.getTotalPrice());

        try {
            customerOrderMapper.insert(customerOrder);
        }catch (Exception e){

            log.error("保存客户订单映射数据失败!原因:"+e.getMessage());
        }

    }

    @Override
    public List<CustomerTopDTO> getCustomerTop() {
        return customerOrderMapper.getCustomerTop();
    }
}
