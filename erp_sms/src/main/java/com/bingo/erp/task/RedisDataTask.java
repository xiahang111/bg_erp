package com.bingo.erp.task;

import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.Role;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.service.*;
import com.bingo.erp.xo.order.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@Slf4j
public class RedisDataTask {


    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RoleService roleService;

    @Resource
    private AdminService adminService;

    @Resource
    private OrderService orderService;

    @Resource
    private MaterialInfoService materialInfoService;

    @Resource
    private LaminateInfoService laminateInfoService;

    @Resource
    private OrderGlassDetailService orderGlassDetailService;

    /**
     *
     */
    @Scheduled(cron = " 0 0/2 * * * ?")
    public void saveRoleList() {

        log.info("更新权限信息到redis开始");
        List<Role> roles = roleService.list();

        redisUtil.setEx(RedisConf.ALL_ROLE_LIST, JsonUtils.objectToJson(roles), 20l, TimeUnit.MINUTES);
        log.info("更新权限信息到redis结束");

    }

    @Scheduled(cron = " 0 0/5 * * * ?")
    public void saveAdminNameAndUid() {
        log.info("更新用户和uid到redis开始");
        List<Admin> admins = adminService.list();

        Map<String, String> resultMap = new HashMap<>();

        for (Admin admin : admins) {

            resultMap.put(admin.getUid(), admin.getUserName());
        }

        redisUtil.setEx(RedisConf.ALL_ADMIN_ADMINUID, JsonUtils.objectToJson(resultMap), 10l, TimeUnit.MINUTES);

    }

    @Scheduled(cron = " 0 0/2 * * * ?")
    public void saveAdminInfo() {

        log.info("更新用户所有信息开始");
        List<Admin> admins = adminService.list();

        List<Map<String, String>> adminMapList = new ArrayList<>();

        for (Admin admin : admins) {
            String uid = admin.getUid();
            String username = admin.getUserName();
            String roleUid = admin.getRoleUid();
            Role role = roleService.getById(roleUid);

            Map<String, String> adminMap = new HashMap<>();
            adminMap.put("uid", uid);
            adminMap.put("username", username);
            adminMap.put("roleName", role.getRoleName());

            adminMapList.add(adminMap);

        }

        redisUtil.setEx(RedisConf.USER_ADMIN_INFO_KEY, JsonUtils.objectToJson(adminMapList), 4l, TimeUnit.MINUTES);

        log.info("更新用户所有信息结束");

    }

    @Scheduled(cron = " 0 0/5 * * * ?")
    public void updateOrderInfo() {

        log.info("更新所有订单开始");

        List<OrderInfo> orderInfos = orderService.list();

        for (OrderInfo orderInfo : orderInfos) {

            String orderUid = orderInfo.getUid();

            log.info("更新订单uid:{}", orderUid);
            ProductVO productVO = orderService.getMaterialVOByUid(orderUid);

            redisUtil.setEx(RedisConf.ORDER_UID_PRE + orderUid, JsonUtils.objectToJson(productVO), 8, TimeUnit.MINUTES);

        }

        log.info("更新所有订单开始");
    }

}
