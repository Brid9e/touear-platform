package com.touear.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.touear.core.tool.utils.BeanUtil;
import com.touear.core.tool.utils.Func;

import java.util.Map;

/**
 * 分页工具
 *
 * @author Chen
 */
public class Condition {

	/**
	 * 转化成mybatis plus中的Page
	 *
	 * @param query
	 * @return
	 */
	public static <T> IPage<T> getPage(Query query) {
		Page<T> page = new Page<>(Func.toInt(query.getCurrent(), 1), Func.toInt(query.getSize(), 10));
		page.setAsc(Func.toStrArray(SqlKeyword.filter(query.getAscs())));
		page.setDesc(Func.toStrArray(SqlKeyword.filter(query.getDescs())));
		return page;
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param entity
	 * @param <T>
	 * @return
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(T entity) {
		return new QueryWrapper<>(entity);
	}

	/**
	 * 获取mybatis plus中的QueryWrapper
	 *
	 * @param query
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> QueryWrapper<T> getQueryWrapper(Map<String, Object> query, Class<T> clazz) {
		query.remove("Touear-Auth");
		query.remove("current");
		query.remove("size");
		QueryWrapper<T> qw = new QueryWrapper<>();
		qw.setEntity(BeanUtil.newInstance(clazz));
		SqlKeyword.buildCondition(query, qw);
		return qw;
	}

}
