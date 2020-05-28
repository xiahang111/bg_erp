package com.bingo.erp.base.vo;

import lombok.Data;

@Data
public class PageInfo<T> {
    /**
     * 关键字
     */
    private String keyword;

    /**
     * 当前页
     */
    private Long currentPage;

    /**
     * 页大小
     */
    private Long pageSize;
}
