package com.touear.core.secure.constant;

import com.touear.core.tool.utils.Func;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Title: Referer
 * @Description: 接口访问来源枚举和常数类
 * @author wangqq
 * @date 2022-04-13 09:10:49
 * @version 1.0
 */
public class Referer {

    @Getter
    @AllArgsConstructor
    public enum Enum {

        INTERNAL("touear.internal.feign", "feign调用"),
        EXTERNAL("touear.external.gateway", "网关调用");

        /** 类型 */
        private final String type;

        /** 类型描述 */
        private final String description;


        /**
         * 是否符合调用条件
         *
         * @param type      header中的Referer参数值
         * @return {@link boolean}
         * @author wangqq
         * @date 2022-04-13 10:15:28
         */
        public static boolean conform(String type) {
            if (Func.isNotBlank(type)) {
                Enum[] enums = Enum.values();
                for (Enum referer : enums){
                    if (type.equals(referer.getType())){
                        return true;
                    }
                }
            }
            return false;
        }

    }

    public static class Constants {

        public static final String REFERER = "Referer";

    }

}
