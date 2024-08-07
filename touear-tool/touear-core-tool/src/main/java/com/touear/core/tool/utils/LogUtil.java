package com.touear.core.tool.utils;

import org.slf4j.Logger;

/**
 * @Title: LogUtil
 * @Description: 日志输出级别隔离
 * @author wangqq
 * @date 2022-06-08 17:54:50
 * @version 1.0
 */
@SuppressWarnings("unused")
public class LogUtil {

    // info日志
    public static void info(Logger log, String msg) {
        if (log != null && log.isInfoEnabled()) {
            log.info(msg);
        }
    }
    public static void info(Logger log, String format, Object ... params) {
        if (log != null && log.isInfoEnabled()) {
            log.info(format, params);
        }
    }
    public static void info(Logger log, String msg, Throwable e) {
        if (log != null && log.isInfoEnabled()) {
            log.info(msg, e);
        }
    }
    // error日志
    public static void error(Logger log, String msg) {
        if (log != null && log.isErrorEnabled()) {
            log.error(msg);
        }
    }
    public static void error(Logger log, String format, Object ... params) {
        if (log != null && log.isErrorEnabled()) {
            log.error(format, params);
        }
    }
    public static void error(Logger log, String msg, Throwable e) {
        if (log != null && log.isErrorEnabled()) {
            log.error(msg, e);
        }
    }
    // debug日志
    public static void debug(Logger log, String msg) {
        if (log != null && log.isDebugEnabled()) {
            log.debug(msg);
        }
    }
    public static void debug(Logger log, String format, Object ... params) {
        if (log != null && log.isDebugEnabled()) {
            log.debug(format, params);
        }
    }
    public static void debug(Logger log, String msg, Throwable e) {
        if (log != null && log.isDebugEnabled()) {
            log.debug(msg, e);
        }
    }
    // warn日志
    public static void warn(Logger log, String msg) {
        if (log != null && log.isWarnEnabled()) {
            log.warn(msg);
        }
    }
    public static void warn(Logger log, String format, Object ... params) {
        if (log != null && log.isWarnEnabled()) {
            log.warn(format, params);
        }
    }
    public static void warn(Logger log, String msg, Throwable e) {
        if (log != null && log.isWarnEnabled()) {
            log.warn(msg, e);
        }
    }

}
