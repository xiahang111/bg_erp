package com.bingo.erp.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.Role;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.service.RoleService;
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

    /**
     *
     */
    @Scheduled(cron = " 0 0 0/2 * * ?")
    public void saveRoleList() {

        log.info("更新权限信息到redis开始");
        List<Role> roles = roleService.list();

        redisUtil.setEx(RedisConf.ALL_ROLE_LIST, JsonUtils.objectToJson(roles), 10l, TimeUnit.MINUTES);
        log.info("更新权限信息到redis结束");

    }

    @Scheduled(cron = " 0 0/1 * * * ?")
    public void saveAdminNameAndUid() {
        log.info("更新用户和uid到redis开始");
        List<Admin> admins = adminService.list();

        Map<String, String> resultMap = new HashMap<>();

        for (Admin admin : admins) {

            resultMap.put(admin.getUid(), admin.getUserName());
        }

        redisUtil.setEx(RedisConf.ALL_ADMIN_ADMINUID, JsonUtils.objectToJson(resultMap), 2l, TimeUnit.MINUTES);

    }
}
