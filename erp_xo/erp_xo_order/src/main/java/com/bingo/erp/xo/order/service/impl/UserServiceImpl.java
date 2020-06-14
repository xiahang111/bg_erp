package com.bingo.erp.xo.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.Role;
import com.bingo.erp.utils.JsonUtils;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.global.SysConf;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.service.RoleService;
import com.bingo.erp.xo.order.service.UserService;
import com.bingo.erp.xo.order.vo.AddUserVO;
import com.bingo.erp.xo.order.vo.UserPageVO;
import com.bingo.erp.xo.order.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    RedisUtil redisUtil;

    @Resource
    private AdminService adminService;

    @Resource
    private RoleService roleService;



    @Override
    public IPage<UserVO> getAllUser(String adminUid, UserPageVO userPageVO) throws Exception {

        if (!validateAdmin(adminUid)) {
            throw new MessageException("权限不够，无法获取用户信息");
        }

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

        if (null != userPageVO.getKeyword() && StringUtils.isNotEmpty(userPageVO.getKeyword())) {
            queryWrapper.eq("user_name", userPageVO.getKeyword());
        }

        Page<Admin> adminPage = new Page<>();
        adminPage.setCurrent(userPageVO.getCurrentPage());
        adminPage.setSize(userPageVO.getPageSize());
        IPage<Admin> adminIPage = adminService.page(adminPage, queryWrapper);

        String roleListJson = redisUtil.get(RedisConf.ALL_ROLE_LIST);

        if (StringUtils.isBlank(roleListJson)) {
            log.error("获取权限列表缓存失败");
            throw new MessageException("获取权限列表失败");
        }

        ArrayList roleList = JsonUtils.jsonArrayToArrayList(roleListJson);

        List<UserVO> userVOS = new ArrayList<>();

        /**
         * 整合用户权限信息
         */
        adminIPage.getRecords().stream().forEach(admin1 -> {

            UserVO userVO = new UserVO();
            String userRoleUid = admin1.getRoleUid();
            Role userRole = roleService.getById(userRoleUid);

            userVO.setAvaterUrl(admin1.getAvatar());
            userVO.setUsername(admin1.getUserName());
            userVO.setAdminUid(admin1.getUid());
            userVO.setRoleUid(admin1.getRoleUid());
            userVO.setRoleName(userRole.getRoleName());
            userVO.setRealName(admin1.getNickName());
            userVO.setLoginCount(admin1.getLoginCount());
            userVO.setMobile(admin1.getMobile());
            userVO.setLastLoginIp(admin1.getLastLoginIp());
            userVO.setLastLoginTime(admin1.getLastLoginTime());

            userVOS.add(userVO);

        });

        Page<UserVO> userVOPage = new Page<>();
        userVOPage.setRecords(userVOS);
        userVOPage.setSize(adminPage.getSize());
        userVOPage.setCurrent(adminPage.getCurrent());
        userVOPage.setOrders(adminPage.orders());
        userVOPage.setTotal(adminPage.getTotal());

        return userVOPage;
    }

    @Override
    public List<Role> getAllRole(String adminUid) throws Exception {

        if (!validateAdmin(adminUid)) {
            throw new MessageException("权限不够，无法获取用户信息");
        }

        String roleListJson = redisUtil.get(RedisConf.ALL_ROLE_LIST);

        if (StringUtils.isBlank(roleListJson)) {
            log.error("获取权限列表缓存失败");
            throw new MessageException("获取权限列表失败");
        }

        ArrayList roleList = JsonUtils.jsonArrayToArrayList(roleListJson);

        return roleList;
    }

    @Override
    public void AddUser(String adminUid, AddUserVO addUserVO) throws Exception{

        if (!validateAdmin(adminUid)) {
            throw new MessageException("权限不够，无法获取用户信息");
        }

        Admin addUser = new Admin();

        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("role_name",addUserVO.getRoleName());

        Role role = roleService.getOne(queryWrapper);

        if (null == role){
            throw new MessageException("没有此权限，请联系开发人员");
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = encoder.encode(addUserVO.getPassword());

        addUser.setPassWord(password);
        addUser.setUserName(addUserVO.getUsername());
        addUser.setRoleUid(role.getUid());
        addUser.setNickName(addUserVO.getRealName());
        addUser.setMobile(addUserVO.getMobile());

        adminService.save(addUser);
    }

    private boolean validateAdmin(String adminUid){

        Admin admin = adminService.getById(adminUid);

        String roleUid = admin.getRoleUid();

        Role role = roleService.getById(roleUid);

        if (SysConf.ADMIN_ROLE.equals(role.getRoleName())){
            return true;
        }

        return false;
    }
}
