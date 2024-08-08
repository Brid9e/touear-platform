package com.touear.manage.feign;



import com.touear.core.tool.api.R;
import com.touear.manage.entity.BaseAccountEntity;
import org.springframework.stereotype.Component;


/**
 * @Title: IBaseAccountClientFallback.java
 * @Description: IBaseAccountClientFallback
 * @version 1.0
 */
@Component
public class IBaseAccountClientFallback implements BaseAccountClient{


    @Override
    public R<BaseAccountEntity> queryById(Long id) {
        return null;
    }
}
