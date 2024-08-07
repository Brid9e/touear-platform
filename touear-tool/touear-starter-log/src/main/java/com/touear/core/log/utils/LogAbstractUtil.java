package com.touear.core.log.utils;



import com.touear.core.launch.server.ServerInfo;
import com.touear.core.log.model.LogAbstract;
import com.touear.core.secure.utils.SecureUtil;
import com.touear.core.tool.utils.*;


import javax.servlet.http.HttpServletRequest;

/**
 * Log 相关工具
 *
 * @author Chenl
 */
public class LogAbstractUtil {

	/**
	 * 向log中添加补齐request的信息
	 *
	 * @param request     请求
	 * @param logAbstract 日志基础类
	 */
	public static void addRequestInfoToLog(HttpServletRequest request, LogAbstract logAbstract) {
		if (ObjectUtil.isNotEmpty(request)) {
			logAbstract.setRemoteIp(WebUtil.getIP(request));
			logAbstract.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
			logAbstract.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
			logAbstract.setMethod(request.getMethod());
//			logAbstract.setParams(WebUtil.getRequestContent(request));
			logAbstract.setCreateBy(SecureUtil.getUserAccount());
		}
	}

	/**
	 * 向log中添加补齐其他的信息（eg：server等）
	 *
	 * @param logAbstract     日志基础类
	 * @param serverInfo      服务信息
	 */
	public static void addOtherInfoToLog(LogAbstract logAbstract, ServerInfo serverInfo) {
		logAbstract.setServerHost(serverInfo.getHostName());
		logAbstract.setServerIp(serverInfo.getIpWithPort());
		logAbstract.setCreateTime(DateUtil.now());
		if (logAbstract.getParams() == null) {
			logAbstract.setParams(StringPool.EMPTY);
		}
	}
}
