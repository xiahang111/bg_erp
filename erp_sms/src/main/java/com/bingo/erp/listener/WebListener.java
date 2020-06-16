package com.bingo.erp.listener;

import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.feign.PersonFeignClient;
import com.bingo.erp.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebListener {


    @Autowired
    private PersonFeignClient personFeignClient;

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
}
