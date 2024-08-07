package com.touear.config;

import com.touear.support.CustomSqlInjector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.touear.support.ModelMetaObjectHandler;

/**
 * @Description: 分页插件
 * @date 2019年8月28日
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * @Description: 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(100000);
        return paginationInterceptor;
    }
    
    /**
     * @Description: Sequence主键自增
     */
	@Bean
	public OracleKeyGenerator oracleKeyGenerator(){
		return new OracleKeyGenerator();
	}
	/**
	 * 自动填充模型数据
	 * 
	 * @return
	 * @author chenl
	 * @date 2020-08-28 16:22:17
	 */
    @Bean
    @ConditionalOnMissingBean(ModelMetaObjectHandler.class)
    public ModelMetaObjectHandler modelMetaObjectHandler() {
        return new ModelMetaObjectHandler();
    }

    @Bean
    public CustomSqlInjector sqlInjector() {
        return new CustomSqlInjector();
    }
}