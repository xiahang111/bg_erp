package com.bingo.erp.xo.order.service.impl;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bingo.erp.base.exception.MessageException;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.xo.order.global.MessageConf;
import com.bingo.erp.xo.order.mapper.AdminMapper;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.vo.AdminVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Service
public class AdminServiceImpl extends SuperServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public void changePwd(String adminUid, String oldPwd, String newPwd) throws Exception{

        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd)) {
           throw new MessageException(MessageConf.PARAM_INCORRECT);
        }
        Admin admin = this.getById(adminUid);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isPassword = encoder.matches(oldPwd, admin.getPassWord());
        if (isPassword) {
            admin.setPassWord(encoder.encode(newPwd));
            admin.setUpdateTime(new Date());
            admin.updateById();
        } else {
            throw new MessageException(MessageConf.ERROR_PASSWORD);
        }

    }


    @Override
    public Admin getMe(String adminUid) throws Exception {

        Admin admin = this.getById(adminUid);
        //清空密码，防止泄露
        admin.setPassWord(null);
        return admin;
    }

    @Override
    public void editMe(AdminVO adminVO) {

        Admin admin = this.getById(adminVO.getUid());

        admin.setUserName(adminVO.getUserName());
        //todo 暂时不弄图片
        //admin.setAvatar(adminVO.getAvatar());
        admin.setNickName(adminVO.getNickName());
        admin.setGender(adminVO.getGender());
        admin.setEmail(adminVO.getEmail());
        admin.setQqNumber(adminVO.getQqNumber());
        admin.setOccupation(adminVO.getOccupation());
        admin.setMobile(adminVO.getMobile());
        admin.setSummary(adminVO.getSummary());
        // 无法直接修改密码，只能通过重置密码完成密码修改
        admin.setPassWord(null);
        admin.updateById();

        this.updateById(admin);
    }
}
