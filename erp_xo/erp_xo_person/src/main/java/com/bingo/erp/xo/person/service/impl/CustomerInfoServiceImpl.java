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
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.person.entity.CustomerInfo;
import com.bingo.erp.xo.person.feign.DataFeignClient;
import com.bingo.erp.xo.person.mapper.CustomerInfoMapper;
import com.bingo.erp.xo.person.service.CustomerInfoService;
import com.bingo.erp.xo.person.vo.CustomerPageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CustomerInfoServiceImpl extends SuperServiceImpl<CustomerInfoMapper, CustomerInfo> implements CustomerInfoService {

    @Resource
    private CustomerInfoService customerInfoService;

    @Resource
    private DataFeignClient dataFeignClient;


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

    /**
     * 获取当前登录用户下的所有客户
     * @param uid
     * @param key
     * @return
     */
    @Override
    public List<CustomerInfo> searchCustomer(String uid, String key) {

        QueryWrapper<CustomerInfo> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("admin_uid",uid);
        //queryWrapper.like("customer_name",key);

        List<CustomerInfo> customerInfos = customerInfoService.list(queryWrapper);

        if(null == customerInfos || customerInfos.size() <= 0){
            log.warn("获取客户信息为空!uid:"+uid+",key:"+key+"");
            return Collections.EMPTY_LIST;
        }
        return customerInfos;
    }

}
