package com.touear.wechat.validate.util;

import com.touear.core.tool.exception.ServiceException;
import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.ReflectUtil;
import com.touear.wechat.validate.annotation.Validate;
import com.touear.wechat.validate.annotation.Validates;
import com.touear.wechat.validate.service.Add;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @Title: ValidateUtil.java
 * @Description: Field注解解析
 * @author wangqq
 * @date 2019年12月6日 上午8:49:58
 * @version 1.0
 */
public class ValidateUtil {

    public static <T> void check(T t) {
        check(t, Add.class);
    }
    
    /**
     * 
     * 根据实体类的属性值规则校验注解校验对象中的属性值是否满足注解要求
     * 若满足，则代码顺利执行
     * 若不满足，则会抛出BusinessException异常
     *
     * @param t     校验对象
     * @author wangqq
     * @date 2020-06-03 10:14:45
     */
    public static <T> void check(T t, Class<?> clazz) {
        if (Func.isNotEmpty(t)) {
            // 获取目标对象的所有属性对象
            Field[] fields = t.getClass().getDeclaredFields();
            if (Func.isNotEmpty(fields)) {
                for (Field field : fields) {
                    // 设置可以使用私有属性
                    field.setAccessible(true);
                    // 获取参数校验规则注解，并做进一步的封装
                    Validate[] validateArray = getAnnotation(field);
                    // 若参数校验规则注解不为空，则遍历该注解集合
                    if (validateArray != null && validateArray.length != 0) {
                        // 定义参数值【不一定用得到，等确定要使用的时候再初始化内容】
                        Object value = null;
                        for (Validate validate : validateArray) {
                            // 当该注解不为null并且该注解的group属性匹配当前class时，进入校验环节
                            if (validate != null && isGroup(validate, clazz)) {
                                // 通过反射获取当前的参数值
                                if (value == null) {
                                    value = ReflectUtil.invokeGetMethod(t, field.getName());
                                }
                                // 获取参数的名称【优先级为：validate注解的name属性值>validates注解的name属性值>参数名称】，该数据为错误提示使用
                                String fieldName = getFieldName(field);
                                // 不为空
                                notNull(validate, value, fieldName);
                                // 长度
                                length(validate, value, fieldName);
                                // 参数值是属于某个数组之内
                                in(validate, value, fieldName);
                                // 符合某个正则表达式
                                regex(validate, value, fieldName);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 
     * 获取属性值规则校验注解数组
     *
     * @param field     当前属性对象
     * @return          该属性对应的注解集合
     * @author wangqq
     * @date 2020-06-03 10:33:49
     */
    private static Validate[] getAnnotation(Field field) {
        // 定义注解容器
        Validate[] validateArray = null;
        // 获取多重注解
        Validates validates = field.getAnnotation(Validates.class);
        // 获取单重注解
        Validate validate = field.getAnnotation(Validate.class);
        // 多重注解不为空
        if (validates != null) {
            // 判断单重注解是否为空，若不为空，将之添加进注解容器中
            if (validate != null) {
                // 初始化注解容器
                validateArray = new Validate[validates.value().length + 1];
                System.arraycopy(validates.value(), 0, validateArray, 0, validates.value().length);
                validateArray[validates.value().length] = validate;
            } else {
                // 初始化注解容器
                validateArray = new Validate[validates.value().length];
                System.arraycopy(validates.value(), 0, validateArray, 0, validates.value().length);
            }
        } else {
            // 当多重注解为空时，直接判断单重注解是否为空，
            // 若不为空，初始化注解容器，并将单重注解封装至容器内
            if (validate != null) {
                validateArray = new Validate[1];
                validateArray[0] = validate;
            }
        }
        return validateArray;
    }

    /**
     * 
     * 是否是当前需要被验证的接口
     * 
     * @param validate     当前注解对象
     * @return             是否需要被校验
     * @author wangqq
     * @date 2020-06-03 10:04:59
     */
    private static boolean isGroup(Validate validate, Class<?> clazz) {
        // 获取配置的分组
        Class<?>[] groups = validate.groups();
        // 当配置不为空时，判断当前的是否在分组中
        if (Func.isNotEmpty(groups)) {
            return Arrays.asList(groups).contains(clazz);
        } else {
            return true;
        }
    }
    
    /**
     * 
     * 获取属性的名称【优先级为：param注解的name属性值>params注解的name属性值>field属性名称】
     *
     * @param field     获取被注解的属性中文名称
     * @return          属性中文名称
     * @author wangqq
     * @date 2020-06-03 10:33:10
     */
    private static String getFieldName(Field field) {
        // 获取多重注解和单重注解name属性值
        Validates validates = field.getAnnotation(Validates.class);
        Validate validate = field.getAnnotation(Validate.class);
        String fieldName = null;
        String fieldsName = null;
        if (validates != null) {
            fieldsName = validates.name();
        }
        if (validate != null) {
            fieldName = validate.name();
        }
        // 按照优先级返回属性的名称
        return StringUtils.isBlank(fieldName)
            ? (StringUtils.isBlank(fieldsName) ? field.getName() : fieldsName) : fieldName;
    }

    /**
     *
     * 数据校验【不为空的验证】
     *
     * @param paramAnntation    属性值规则校验注解
     * @param value             属性值
     * @param fieldName         属性名称
     * @author wangqq
     * @date 2020-06-03 10:50:28
     */
    private static void notNull(Validate paramAnntation, Object value, String fieldName) {
        // 注解设置了不为空，但属性值为空，抛出异常
        if (paramAnntation.notNull() && Func.isEmpty(value)) {
            throw new ServiceException(500, fieldName + "不能为空");
        }
    }

    /**
     *
     * 数据校验【长度的验证】
     *
     * @param paramAnntation    属性值规则校验注解
     * @param value             属性值
     * @param fieldName         属性名称
     * @author wangqq
     * @date 2020-06-03 10:50:28
     */
    private static void length(Validate paramAnntation, Object value, String fieldName) {
        // 当注解设置了长度限制，但是属性值的长度却超出之后
        if (paramAnntation.length() != -1 && Func.isNotEmpty(value)
                && paramAnntation.length() < value.toString().length()) {
            // 返回长度超出的异常
            throw new ServiceException(500, fieldName + "长度不符合要求");
        }
    }

    /**
     *
     * 数据校验【属性值是属于某个数组之内】
     *
     * @param paramAnntation    属性值规则校验注解
     * @param value             属性值
     * @param fieldName         属性名称
     * @author wangqq
     * @date 2020-06-03 10:51:54
     */
    private static void in(Validate paramAnntation, Object value, String fieldName) {
        // 当注解设置的属性值范围数组存在且属性值不为空时
        boolean flag = Func.isNotEmpty(value) && Func.isNotEmpty(paramAnntation.in());
        if (flag) {
            // 判断该属性值是否存在于配置的范围数组中
            flag = Arrays.asList(paramAnntation.in()).contains(value.toString());
            // 若不存在，抛出异常
            if (!flag) {
//                throw new ServiceException(ResultEnum.PARAM_IN, fieldName);
                throw new ServiceException(500, fieldName + "不在指定范围内");
            }
        }
    }

    /**
     *
     * 数据校验【属性值符合某个正则表达式】
     *
     * @param paramAnntation    属性值规则校验注解
     * @param value             属性值
     * @param fieldName         属性名称
     * @author wangqq
     * @date 2020-06-03 10:56:59
     */
    private static void regex(Validate paramAnntation, Object value, String fieldName) {
        // 当属性值和正则表达式不为空时
        if (Func.isNotEmpty(value) && StringUtils.isNotBlank(paramAnntation.regex())) {
            // 验证该属性值是否符合目标正则表达式
            boolean flag = Pattern.matches(paramAnntation.regex(), value.toString());
            // 若不符合，抛出异常
            if (!flag) {
//                throw new ServiceException(ResultEnum.PARAM_REGEX, fieldName);
                throw new ServiceException(500, fieldName + "格式不符合要求");
            }
        }
    }
}
