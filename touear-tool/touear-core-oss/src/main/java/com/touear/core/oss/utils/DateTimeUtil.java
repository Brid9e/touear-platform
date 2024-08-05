package com.touear.core.oss.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * DateTime 工具类
 *
 * @author L.cm
 */
public class DateTimeUtil {
	public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATETIME);
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATE);
	public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(DateUtil.PATTERN_TIME);

	/**
	 * 日期时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatDateTime(TemporalAccessor temporal) {
		return DATETIME_FORMAT.format(temporal);
	}

	/**
	 * 日期时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatDate(TemporalAccessor temporal) {
		return DATE_FORMAT.format(temporal);
	}

	/**
	 * 时间格式化
	 *
	 * @param temporal 时间
	 * @return 格式化后的时间
	 */
	public static String formatTime(TemporalAccessor temporal) {
		return TIME_FORMAT.format(temporal);
	}

	/**
	 * 日期格式化
	 *
	 * @param temporal 时间
	 * @param pattern  表达式
	 * @return 格式化后的时间
	 */
	public static String format(TemporalAccessor temporal, String pattern) {
		return DateTimeFormatter.ofPattern(pattern).format(temporal);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return DateTimeUtil.parseDateTime(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr, DateTimeFormatter formatter) {
		return LocalDateTime.parse(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalDateTime parseDateTime(String dateStr) {
		return DateTimeUtil.parseDateTime(dateStr, DateTimeUtil.DATETIME_FORMAT);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static LocalDate parseDate(String dateStr, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return DateTimeUtil.parseDate(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalDate parseDate(String dateStr, DateTimeFormatter formatter) {
		return LocalDate.parse(dateStr, formatter);
	}

	/**
	 * 将字符串转换为日期
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalDate parseDate(String dateStr) {
		return DateTimeUtil.parseDate(dateStr, DateTimeUtil.DATE_FORMAT);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 时间正则
	 * @return 时间
	 */
	public static LocalTime parseTime(String dateStr, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return DateTimeUtil.parseTime(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr   时间字符串
	 * @param formatter DateTimeFormatter
	 * @return 时间
	 */
	public static LocalTime parseTime(String dateStr, DateTimeFormatter formatter) {
		return LocalTime.parse(dateStr, formatter);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @return 时间
	 */
	public static LocalTime parseTime(String dateStr) {
		return DateTimeUtil.parseTime(dateStr, DateTimeUtil.TIME_FORMAT);
	}

	/**
	 * 时间转 Instant
	 *
	 * @param dateTime 时间
	 * @return Instant
	 */
	public static Instant toInstant(LocalDateTime dateTime) {
		return dateTime.atZone(ZoneId.systemDefault()).toInstant();
	}

	/**
	 * Instant 转 时间
	 *
	 * @param instant Instant
	 * @return Instant
	 */
	public static LocalDateTime toDateTime(Instant instant) {
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * 转换成 date
	 *
	 * @param dateTime LocalDateTime
	 * @return Date
	 */
	public static Date toDate(LocalDateTime dateTime) {
		return Date.from(DateTimeUtil.toInstant(dateTime));
	}

	/**
	 * 比较2个时间差，跨度比较小
	 *
	 * @param startInclusive 开始时间
	 * @param endExclusive   结束时间
	 * @return 时间间隔
	 */
	public static Duration between(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive);
	}

	/**
	 * 比较2个时间差，跨度比较大，年月日为单位
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return 时间间隔
	 */
	public static Period between(LocalDate startDate, LocalDate endDate) {
		return Period.between(startDate, endDate);
	}
	
	/**
	 * 
	 * 两个时间段取交集【数组中的所有元素的时间格式应保持一致】
	 *
	 * @param date1    时间集合1，格式为：[开始时间，结束时间]
	 * @param date2    时间集合2，格式为：[开始时间，结束时间]
	 * @param format   时间格式，默认为yyyy-MM-dd HH:mm:ss，参数可不传
	 * @return         时间交集的数组
	 * @author wangqq
	 * @date 2020-08-26 17:45:10
	 */
    public static String[] getAlphalDate(String[] date1, String[] date2, String ... format) {
        if(format == null || format.length == 0) {
            format = new String[]{"yyyy-MM-dd HH:mm:ss"};
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format[0]);
            long sd1 = 0l, ed1 = 0l, sd2 = 0l, ed2 = 0l;
            
            if (date1 != null && date1.length != 0) {
                if (StringUtil.isNotBlank(date1[0])) {
                    sd1 = sdf.parse(date1[0]).getTime();
                } else {
                    sd1 = Long.MIN_VALUE;
                }
                if (StringUtil.isNotBlank(date1[1])) {
                    ed1 = sdf.parse(date1[1]).getTime();
                } else {
                    ed1 = Long.MAX_VALUE;
                }
            }
            if (date2 != null && date2.length != 0) {
                if (StringUtil.isNotBlank(date2[0])) {
                    sd2 = sdf.parse(date2[0]).getTime();
                } else {
                    sd2 = Long.MIN_VALUE;
                }
                if (StringUtil.isNotBlank(date2[1])) {
                    ed2 = sdf.parse(date2[1]).getTime();
                } else {
                    ed2 = Long.MAX_VALUE;
                }
            }
            if ((sd2 <= sd1 && sd1 <= ed2) || (sd2 >= sd1 && sd2 <= ed1)) {
                long startL = Math.max(sd2, sd1);
                long endL = Math.min(ed2, ed1);
                String start = null;
                String end = null;
                if (startL != Long.MIN_VALUE) {
                    start = sdf.format(startL);
                }
                if (endL != Long.MAX_VALUE) {
                    end = sdf.format(endL);
                }
                return new String[]{start, end};
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
