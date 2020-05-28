package com.bingo.erp.xo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.bingo.erp.base.enums.EStatus;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.Role;
import com.bingo.erp.utils.RedisUtil;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.xo.global.MessageConf;
import com.bingo.erp.xo.global.RedisConf;
import com.bingo.erp.xo.global.SQLConf;
import com.bingo.erp.xo.mapper.RoleMapper;
import com.bingo.erp.xo.service.AdminService;
import com.bingo.erp.xo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author limbo
 * @since 2018-09-30
 */
@Service
public class RoleServiceImpl extends SuperServiceImpl<RoleMapper, Role> implements RoleService {


}
