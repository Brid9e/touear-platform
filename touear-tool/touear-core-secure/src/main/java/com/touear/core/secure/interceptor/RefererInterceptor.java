package com.touear.core.secure.interceptor;

import com.touear.core.secure.constant.Referer;
import com.touear.core.tool.api.R;
import com.touear.core.tool.api.ResultCode;
import com.touear.core.tool.constant.TouearConstant;
import com.touear.core.tool.jackson.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Title: RefererInterceptor
 * @Description: 接口访问来源拦截器
 * @author wangqq
 * @date 2022-04-13 09:10:49
 * @version 1.0
 */
@Slf4j
@AllArgsConstructor
public class RefererInterceptor extends HandlerInterceptorAdapter implements Filter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String referer = request.getHeader(Referer.Constants.REFERER);
        if (!Referer.Enum.conform(referer)) {
            log.warn("接口访问来源验证失败，请求接口：{}", request.getRequestURI());
            R<String> result = R.fail(ResultCode.UN_AUTHORIZED, "权限不足，拒绝当前访问");
            response.setCharacterEncoding(TouearConstant.UTF_8);
            response.setHeader(TouearConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
            return false;
        }
        return true;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("==============================");
    }

    @Override
    public void destroy() {

    }
}
