package com.bingo.erp.xo.order.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.base.serviceImpl.SuperServiceImpl;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.xo.order.mapper.AdminMapper;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.vo.UserVO;
import org.springframework.stereotype.Service;

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


}
