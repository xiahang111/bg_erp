package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.base.enums.CustomerResourceEnums;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.entity.CustomerInfo;
import com.bingo.erp.commons.feign.DataFeignClient;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.mapper.CustomerInfoMapper;
import com.bingo.erp.xo.order.service.CustomerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class CustomerInfoServiceImpl extends SuperServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerInfoService {

    @Resource
    private CustomerInfoService customerInfoService;

    @Resource
    private DataFeignClient dataFeignClient;

    @Override
    public void saveCustomerByOrder(String adminUid, CustomerVO customerVO) {
        QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();

        //保证没有重复的客户和收货人名称
        queryWrapper.eq("admin_uid", customerVO.getAdminUid());
        queryWrapper.eq("customer_nick", customerVO.getCustomerNick());
        queryWrapper.eq("customer_name", customerVO.getCustomerName());
        queryWrapper.eq("salesman", customerVO.getSalesman());

        CustomerInfo exist = customerInfoService.getOne(queryWrapper);


        if (null == exist) {
            CustomerInfo customerInfo = new CustomerInfo();
            if (StringUtils.isNotBlank(adminUid)){
                customerInfo.setAdminUid(adminUid);
            }
            if (StringUtils.isNotBlank(customerVO.getAdminUid())){
                customerInfo.setAdminUid(customerVO.getAdminUid());
            }
            customerInfo.setCustomerResource(CustomerResourceEnums.getByCode(customerVO.getCustomerResource()));
            customerInfo.setCustomerName(customerVO.getCustomerName());
            customerInfo.setCustomerAddr(customerVO.getCustomerAddr());
            customerInfo.setCustomerPhone(customerVO.getCutomerPhone());
            customerInfo.setSalesman(customerVO.getSalesman());
            customerInfo.setCustomerNick(customerVO.getCustomerNick());
            customerInfo.setExpress(customerVO.getExpress());
            if(StringUtils.isBlank(customerVO.getNameMapper())){
                customerVO.setNameMapper(customerVO.getCustomerNick()+"-"+customerVO.getCustomerName());
            }
            customerInfo.setNameMapper(customerVO.getNameMapper());

            try {
                customerInfoService.save(customerInfo);
            }catch (Exception e){
                log.error("保存客户信息失败,原因:"+e.getMessage());
            }

            customerVO.setCustomerUid(customerInfo.getUid());

        }else {
            exist.setCustomerResource(CustomerResourceEnums.getByCode(customerVO.getCustomerResource()));
            exist.setCustomerAddr(customerVO.getCustomerAddr());
            exist.setCustomerPhone(customerVO.getCutomerPhone());
            exist.setExpress(customerVO.getExpress());
            exist.setNameMapper(customerVO.getNameMapper());
            try {
                customerInfoService.updateById(exist);
            }catch (Exception e){
                log.error("更新客户信息失败,原因:"+e.getMessage());
            }

            customerVO.setCustomerUid(exist.getUid());
        }

        //通过feign保存客户订单映射信息
        log.info("保存客户订单映射信息开始,数据:{}",JsonUtils.objectToJson(customerVO));
        dataFeignClient.saveCustomerOrder(customerVO);
        log.info("保存客户订单映射信息结束");
    }
}
