package com.bingo.erp.xo.order.global;

import com.bingo.erp.base.global.BaseRedisConf;

public class RedisConf extends BaseRedisConf {

    /**
     * 表单重复提交
     */
    public final static String AVOID_REPEATABLE_COMMIT = "AVOID_REPEATABLE_COMMIT";

    /**
     * 管理员访问菜单
     */
    public final static String ADMIN_VISIT_MENU = "ADMIN_VISIT_MENU";

    /**
     * 权限菜单
     */
    public final static String ALL_ROLE_LIST = "ALL_ROLE_LIST";

    public final static String ORDER_UID_PRE = "ORDER_UID-";
}
