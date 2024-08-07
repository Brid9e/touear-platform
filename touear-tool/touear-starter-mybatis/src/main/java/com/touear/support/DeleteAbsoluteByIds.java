package com.touear.support;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class DeleteAbsoluteByIds extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String method = "deleteAbsoluteByIds";
        SqlMethod sqlMethod = SqlMethod.DELETE_BATCH_BY_IDS;
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), tableInfo.getKeyColumn(), SqlScriptUtils.convertForeach(SqlScriptUtils.convertChoose("@org.apache.ibatis.type.SimpleTypeRegistry@isSimpleType(item.getClass())", "#{item}", "#{item." + tableInfo.getKeyProperty() + "}"), "coll", (String) null, "item", ","));
        SqlSource sqlSource = this.languageDriver.createSqlSource(this.configuration, sql, Object.class);
        return this.addDeleteMappedStatement(mapperClass, method, sqlSource);
    }
}