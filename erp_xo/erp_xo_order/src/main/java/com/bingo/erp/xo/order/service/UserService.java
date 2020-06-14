package com.bingo.erp.xo.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bingo.erp.commons.entity.Role;
import com.bingo.erp.xo.order.vo.AddUserVO;
import com.bingo.erp.xo.order.vo.UserPageVO;
import com.bingo.erp.xo.order.vo.UserVO;

import java.util.List;

public interface UserService {

    IPage<UserVO> getAllUser(String adminUid, UserPageVO userPageVO) throws Exception;

    List<Role> getAllRole(String adminUid)throws Exception;

    void AddUser(String adminUid, AddUserVO addUserVO) throws Exception;

}
