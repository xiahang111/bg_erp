package com.bingo.erp.xo.person.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.enums.CustomerResourceEnums;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.global.BaseRedisConf;
import com.bingo.erp.base.global.BaseSysConf;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.base.vo.CustomerVO;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.person.entity.CustomerInfo;
import com.bingo.erp.xo.person.mapper.CustomerInfoMapper;
import com.bingo.erp.xo.person.service.CustomerInfoService;
import com.bingo.erp.xo.person.vo.CustomerPageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerInfoServiceImpl extends SuperServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerInfoService {

    @Resource
    private CustomerInfoService customerInfoService;


    @Resource
    private RedisUtil redisUtil;

    @Override
    public IPage<CustomerInfo> getCustomerByAdminUid(String adminUid, CustomerPageVO customerPageVO) throws Exception {


        String adminUidJson = redisUtil.get(BaseRedisConf.ALL_ADMIN_ADMINUID);

        if (StringUtils.isBlank(adminUidJson)) {
            log.error("获取用户列表缓存失败");
            throw new MessageException("获取用户列表失败");
        }

        Map adminUidMap = JsonUtils.jsonToMap(adminUidJson, HashMap.class);

        String adName = (String) adminUidMap.get(adminUid);

        QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();

        if (!BaseSysConf.ADMIN.equals(adName)) {
            queryWrapper.eq("admin_uid", adminUid);
        }

        if (StringUtils.isNotBlank(customerPageVO.getOrderBy())) {
            queryWrapper.orderByDesc(customerPageVO.getOrderBy());
        }

        if (StringUtils.isNotBlank(customerPageVO.getKeyword())){
            queryWrapper.like("customer_name",customerPageVO.getKeyword());
        }

        //分页查询
        Page<CustomerInfo> page = new Page<>();
        page.setCurrent(customerPageVO.getCurrentPage());
        page.setSize(customerPageVO.getPageSize());

        IPage<CustomerInfo> customerInfoIPage = customerInfoService.page(page, queryWrapper);

        List<CustomerInfo> customerInfos = customerInfoIPage.getRecords();
        customerInfos.stream().forEach(customerInfo -> {

            String adUid = customerInfo.getAdminUid();

            String adminName = (String) adminUidMap.get(adUid);

            customerInfo.setAdminUid(adminName);
        });

        return customerInfoIPage;
    }

    @Override
    public void saveCustomerByOrder(String adminUid,CustomerVO customerVO) {

        QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("customer_name", customerVO.getCunstomerName());
        queryWrapper.eq("salesman", customerVO.getSalesman());

        CustomerInfo exist = customerInfoService.getOne(queryWrapper);


        if (null == exist) {
            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setAdminUid(adminUid);
            customerInfo.setAdminUid(customerVO.getAdminUid());
            customerInfo.setCustomerResource(CustomerResourceEnums.getByCode(customerVO.getCustomerResource()));
            customerInfo.setCustomerName(customerVO.getCunstomerName());
            customerInfo.setCustomerAddr(customerVO.getCustomerAddr());
            customerInfo.setCustomerPhone(customerVO.getCutomerPhone());
            customerInfo.setSalesman(customerVO.getSalesman());

            customerInfoService.save(customerInfo);
        }
    }
}
