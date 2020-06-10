package com.bingo.erp.xo.mapper;


import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
public interface AdminMapper extends SuperMapper<Admin> {

    /**
     * 通过uid获取管理员
     *
     * @return
     */
    public Admin getAdminByUid(@Param("uid") String uid);
}
