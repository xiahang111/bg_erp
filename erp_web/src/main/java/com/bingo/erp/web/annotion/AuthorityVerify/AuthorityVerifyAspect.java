package com.bingo.erp.web.annotion.AuthorityVerify;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.bingo.erp.base.enums.EStatus;
import com.bingo.erp.base.global.ECode;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.Role;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.global.MessageConf;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 权限校验 切面实现
 *
 * @author: 陌溪
 * @create: 2020-03-06-19:05
 */
@Aspect
@Component
@Slf4j
public class AuthorityVerifyAspect {

    @Resource
    RoleService roleService;

    @Resource
    AdminService adminService;

    @Autowired
    RedisUtil redisUtil;

    @Pointcut(value = "@annotation(authorityVerify)")
    public void pointcut(AuthorityVerify authorityVerify) {

    }

    @Around(value = "pointcut(authorityVerify)")
    public Object doAround(ProceedingJoinPoint joinPoint, AuthorityVerify authorityVerify) throws Throwable {

        ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attribute.getRequest();

        //获取请求路径
        String url = request.getRequestURI();

        String adminUid = request.getAttribute(SysConf.ADMIN_UID).toString();

        String visitUrl = redisUtil.get(RedisConf.ADMIN_VISIT_MENU + RedisConf.SEGMENTATION + adminUid);

        List<String> roleList = new ArrayList<>();

        if (StringUtils.isNotEmpty(visitUrl)) {
            // 从Redis中获取
            roleList = JsonUtils.jsonToList(visitUrl, String.class);
        } else {

            // 查询数据库获取
            Admin admin = adminService.getById(adminUid);

            String roleUid = admin.getRoleUid();

            Role role = roleService.getById(roleUid);

            roleList.add(role.getRoleName());

        }

        // 判断该角色是否能够访问该接口
        Boolean flag = false;
        for (String item : roleList) {
            if ("admin".equals(item)) {
                flag = true;
                log.info("用户拥有操作权限，访问的路径: {}，拥有的权限接口：{}", url, item);
                break;
            }
        }
        if (!flag) {
            log.info("用户不具有操作权限，访问的路径: {}", url);
            return ResultUtil.result(ECode.NO_OPERATION_AUTHORITY, MessageConf.RESTAPI_NO_PRIVILEGE);
        }

        //执行业务
        Object result = joinPoint.proceed();
        return result;
    }

}
