package com.touear.core.secure.interceptor;

import com.touear.core.secure.User;
import com.touear.core.secure.annotation.InterfaceLevel;
import com.touear.core.secure.enums.InterfaceLevelEnum;
import com.touear.core.secure.utils.SecureUtil;
import com.touear.core.tool.api.R;
import com.touear.core.tool.api.ResultCode;
import com.touear.core.tool.constant.TouearConstant;
import com.touear.core.tool.jackson.JsonUtil;
import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.StringPool;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Title: InterfaceLevelInterceptor
 * @Description: 接口访问级别
 * @author wangqq
 * @date 2021-11-04 10:12:59
 * @version 1.0
 */
@Slf4j
@AllArgsConstructor
public class InterfaceLevelInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try{
            User user = SecureUtil.getUser();
            if (user != null) {
                // 获取当前用户的访问接口级别
                InterfaceLevelEnum userlevelEnum;

                    if (user.getLoginFrom() .equals("manage")) {
                        userlevelEnum = InterfaceLevelEnum.MANAGE;
                    } else {
                        userlevelEnum = InterfaceLevelEnum.USER;
                    }

                // 获取目标方法
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                //获取自定义的注解对象【方法】
                InterfaceLevel interfaceLevelMethod = handlerMethod.getMethod().getAnnotation(InterfaceLevel.class);
                //当自定义的注解对象中的权限级别与解析出来的用户级别不匹配时
                if(interfaceLevelMethod != null && !interfaceLevelMethod.value().equals(userlevelEnum)){
                    //访问权限不足（方法的访问权限）
                    returnError(request, response);
                    return false;
                }
                // 获取自定义的注解对象【类】
                InterfaceLevel interfaceLevelClass = handlerMethod.getBeanType().getAnnotation(InterfaceLevel.class);
                // 当自定义的注解对象中的权限级别与解析出来的用户级别不匹配时
                if(interfaceLevelClass !=null && !interfaceLevelClass.value().equals(userlevelEnum)){
                    //访问权限不足（类的访问权限）
                    returnError(request, response);
                    return false;
                }
            }
            return true;
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return true;
    }

    private static void returnError(HttpServletRequest request, HttpServletResponse response) {
        log.warn("接口级别权限验证失败，请求接口：{}", request.getRequestURI());
        R<String> result = R.fail(ResultCode.UN_AUTHORIZED, "权限不足，拒绝当前用户访问");
        response.setCharacterEncoding(TouearConstant.UTF_8);
        response.setHeader(TouearConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

}
