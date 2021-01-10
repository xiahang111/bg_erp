package com.bingo.erp.xo.order.mapper;


import com.bingo.erp.base.mapper.SuperMapper;
import com.bingo.erp.commons.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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


    @Select({
            "<script>",
            "select a.uid,a.user_name,r.role_name from t_admin a inner JOIN (select * from t_role where role_name in",
            "<foreach collection='roleNames' item='item' index='index' open='(' separator=',' close=')'>",
            "(#{item})",
            "</foreach>",
            ") r on a.role_uid = r.uid",
            "</script>"
    })
    List<String > getAdminUidByRoleNames(@Param(value = "roleNames") List<String > roleNames);
}
