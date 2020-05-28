package com.bingo.erp.web.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.Role;
import com.bingo.erp.xo.global.SQLConf;
import com.bingo.erp.xo.service.AdminService;
import com.bingo.erp.xo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SQLConf.USER_NAME, username);
        Admin admin = adminService.getOne(queryWrapper);

        if (admin == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            //查询出角色信息封装到admin中
            List<String> roleNames = new ArrayList<>();
            Role role = roleService.getById(admin.getRoleUid());
            roleNames.add(role.getRoleName());

            admin.setRoleNames(roleNames);

            return SecurityUserFactory.create(admin);
        }
    }
}
