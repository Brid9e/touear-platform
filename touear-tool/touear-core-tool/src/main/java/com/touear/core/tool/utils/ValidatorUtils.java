package com.touear.core.tool.utils;

import com.touear.core.tool.api.IResultCode;
import com.touear.core.tool.api.ResultCode;
import com.touear.core.tool.exception.ServiceException;

/**
 * @Title: ValidatorUtils
 * @Description: 验证器
 * @author wangqq
 * @date 2022-09-20 16:36:24
 * @version 1.0
 */
public class ValidatorUtils {


    public static void checkService(boolean expression, String errorMessage) {
        if (!expression) {
            throw new ServiceException(errorMessage);
        }
    }

    public static void checkService(boolean expression, Integer code, String errorMessage) {
        if (!expression) {
            throw new ServiceException(code, errorMessage);
        }
    }

    public static void checkService(boolean expression, IResultCode resultBean, String... params) {
        if (!expression) {
            throw new ServiceException(resultBean, params);
        }
    }

    public static void checkService(boolean expression, String errorMessage, String... params) {
        if (!expression) {
            throw new ServiceException(ResultCode.FAILURE.getCode(), errorMessage, params);
        }
    }

    public static void checkService(boolean expression, IResultCode resultBean) {
        if (!expression) {
            throw new ServiceException(resultBean);
        }
    }

}