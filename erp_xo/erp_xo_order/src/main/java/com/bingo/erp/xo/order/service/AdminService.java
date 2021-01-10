package com.bingo.erp.xo.order.service;

import com.bingo.erp.base.service.SuperService;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.xo.order.vo.AdminVO;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
public interface AdminService extends SuperService<Admin> {

    /**
     * 修改密码
     *
     * @return
     */
    void changePwd(String adminUid, String oldPwd, String newPwd) throws Exception;

    /**
     * 获取当前管理员
     *
     * @return
     */
    Admin getMe(String adminUid) throws Exception;

    /**
     * 编辑当前管理员信息
     *
     * @return
     */
    void editMe(AdminVO adminVO);


}
