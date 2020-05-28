package com.bingo.erp.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * mapper 父类，注意这个类不要让 mybatis-plus 扫描到！！
 */
public interface SuperMapper<T> extends BaseMapper<T> {
}
