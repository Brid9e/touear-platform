package com.touear.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author Administrator
 */
public class SearchPage<T> extends Page<T> {


    private Long maxSize;

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public SearchPage() {
    }

    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public SearchPage(long current, long size) {
        super(current, size, 0);
    }

    public SearchPage(long current, long size, long total) {
        super(current, size, total, true);
    }

    public SearchPage(long current, long size, boolean isSearchCount) {
        super(current, size, 0, isSearchCount);
    }

    public SearchPage(long current, long size, long total, boolean isSearchCount) {
        super(current,size,total,isSearchCount);
    }
}
