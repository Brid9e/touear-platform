package com.touear.core.tool.utils;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.WeekFields;
import java.util.*;

/**
 * 日期工具类
 *
 * @author L.cm
 */
@UtilityClass
public class DateUtil {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    /**
     * 格式化
     */
    public static final ConcurrentDateFormat DATETIME_FORMAT = ConcurrentDateFormat.of(PATTERN_DATETIME);
    public static final ConcurrentDateFormat DATE_FORMAT = ConcurrentDateFormat.of(PATTERN_DATE);
    public static final ConcurrentDateFormat TIME_FORMAT = ConcurrentDateFormat.of(PATTERN_TIME);

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取今天的日期
     *
     * @return 时间
     */
    public static String today() {
        return format(new Date(), "yyyyMMdd");
    }

    /**
     * 添加年
     *
     * @param date       时间
     * @param yearsToAdd 添加的年数
     * @return 设置后的时间
     */
    public static Date plusYears(Date date, int yearsToAdd) {
        return DateUtil.plus(date, Calendar.YEAR, yearsToAdd);
    }

    /**
     * 添加月
     *
     * @param date        时间
     * @param monthsToAdd 添加的月数
     * @return 设置后的时间
     */
    public static Date plusMonths(Date date, int monthsToAdd) {
        return DateUtil.plus(date, Calendar.MONTH, monthsToAdd);
    }

    /**
     * 添加周
     *
     * @param date       时间
     * @param weeksToAdd 添加的周数
     * @return 设置后的时间
     */
    public static Date plusWeeks(Date date, int weeksToAdd) {
        return DateUtil.plus(date, Calendar.DAY_OF_WEEK, weeksToAdd);
    }

    /**
     * 添加天
     *
     * @param date      时间
     * @param daysToAdd 添加的天数
     * @return 设置后的时间
     */
    public static Date plusDays(Date date, int daysToAdd) {
        return DateUtil.plus(date, Calendar.DATE, daysToAdd);
    }

    /**
     * 添加小时
     *
     * @param date       时间
     * @param hoursToAdd 添加的小时数
     * @return 设置后的时间
     */
    public static Date plusHours(Date date, int hoursToAdd) {
        return DateUtil.plus(date, Calendar.HOUR, hoursToAdd);
    }

    /**
     * 添加分钟
     *
     * @param date         时间
     * @param minutesToAdd 添加的分钟数
     * @return 设置后的时间
     */
    public static Date plusMinutes(Date date, int minutesToAdd) {
        return DateUtil.plus(date, Calendar.MINUTE, minutesToAdd);
    }

    /**
     * 添加秒
     *
     * @param date         时间
     * @param secondsToAdd 添加的秒数
     * @return 设置后的时间
     */
    public static Date plusSeconds(Date date, int secondsToAdd) {
        return DateUtil.plus(date, Calendar.SECOND, secondsToAdd);
    }

    /**
     * 添加毫秒
     *
     * @param date        时间
     * @param millisToAdd 添加的毫秒数
     * @return 设置后的时间
     */
    public static Date plusMillis(Date date, int millisToAdd) {
        return DateUtil.plus(date, Calendar.MILLISECOND, millisToAdd);
    }

    /**
     * 日期添加时间量
     *
     * @param date   时间
     * @param amount 时间量
     * @return 设置后的时间
     */
    public static Date plus(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, amount);
        return c.getTime();
    }

    /**
     * 日期时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    /**
     * 日期格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * 时间格式化
     *
     * @param date 时间
     * @return 格式化后的时间
     */
    public static String formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }

    /**
     * 日期格式化
     *
     * @param date    时间
     * @param pattern 表达式
     * @return 格式化后的时间
     */
    public static String format(Date date, String pattern) {
        return ConcurrentDateFormat.of(pattern).format(date);
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            // 如果解析失败，尝试使用备选模式进行解析
            String[] alternatePatterns = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss"};
            for (String alternatePattern : alternatePatterns) {
                if (!alternatePattern.equals(pattern)) {
                    try {
                        SimpleDateFormat alternateFormat = new SimpleDateFormat(alternatePattern);
                        return alternateFormat.parse(dateStr);
                    } catch (ParseException ex) {
                        continue; // 如果备选模式也失败，则继续尝试下一个备选模式
                    }
                }
            }
            // 如果所有备选模式都失败，则抛出异常
            throw new RuntimeException("Unable to parse date string: " + dateStr);
        }
    }

    /**
     * 将字符串转换为时间
     *
     * @param dateStr 时间字符串
     * @param format  ConcurrentDateFormat
     * @return 时间
     */
    public static Date parse(String dateStr, ConcurrentDateFormat format) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 转换成java8 时间
     *
     * @param date Date
     * @return LocalDateTime
     */
    public static LocalDateTime toDateTime(Date date) {
        return DateTimeUtil.toDateTime(date.toInstant());
    }

    /**
     * 比较2个 时间差
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间间隔
     */
    public static Duration between(Date startDate, Date endDate) {
        return Duration.between(startDate.toInstant(), endDate.toInstant());
    }

    /**
     * date1和date2相差天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        // 同一年
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {// 闰年
                    timeDistance += 366;
                    // 不是闰年
                } else {
                    timeDistance += 365;
                }
            }
            return timeDistance + Math.abs(day2 - day1);
        } else {
            return Math.abs(day2 - day1);
        }
    }

    /**
     * @Description: 根据日期获取当周周日（周日为每周的第一天）
     * @author xuegang
     * @date 2019年8月13日
     */
    public static Date getFirstDayOfThisWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    /**
     * @Description: 根据日期获取当月的一号
     * @author xuegang
     * @date 2019年8月13日
     */
    public static Date getFirstDayOfThisMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取当天的开始时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:15:01
     */
    public static java.util.Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当天的结束时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:15:11
     */
    public static java.util.Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取昨天的开始时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:15:20
     */
    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取昨天的结束时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:15:27
     */
    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取明天的开始时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:15:34
     */
    public static Date getBeginDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取明天的结束时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:15:41
     */
    public static Date getEndDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 获取本周的开始时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:15:51
     */
    @SuppressWarnings("unused")
    public static Date getBeginDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now());
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本周的结束时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:16:03
     */
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取上周的开始时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:16:10
     */
    public static Date getBeginDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now());
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取N天后的日期
     *
     * @param date
     * @param n
     * @return
     */
    public static Date ndaydate(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        c.add(Calendar.DATE, n);
        return c.getTime();
    }

    /**
     * 获取上周的结束时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:16:19
     */
    public static Date getEndDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取本月的开始时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:16:27
     */
    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月的结束时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:16:34
     */
    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取上月的开始时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:16:42
     */
    public static Date getBeginDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取上月的结束时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:16:49
     */
    public static Date getEndDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取本年的开始时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:16:58
     */
    public static Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本年的结束时间
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:17:07
     */
    public static java.util.Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param date
     * @return
     * @author chenl
     * @date 2020-01-14 16:17:15
     */
    public static Timestamp getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取某个日期的结束时间
     *
     * @param date
     * @return
     * @author chenl
     * @date 2020-01-14 16:17:23
     */
    public static Timestamp getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取今年是哪一年
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:17:31
     */
    public static Integer getNowYear() {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(now());
        return Integer.valueOf(gc.get(1));
    }

    /**
     * 获取本月是哪一月
     *
     * @return
     * @author chenl
     * @date 2020-01-14 16:17:38
     */
    public static int getNowMonth() {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(now());
        return gc.get(2) + 1;
    }

    /**
     * 两个日期相减得到的天数
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author chenl
     * @date 2020-01-14 16:17:46
     */
    public static int getDiffDays(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }
        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);
        int days = new Long(diff).intValue();
        return days;
    }

    /**
     * 两个日期相减得到的毫秒数
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author chenl
     * @date 2020-01-14 16:17:55
     */
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }

    /**
     * 获取两个日期中的最大日期
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author chenl
     * @date 2020-01-14 16:18:05
     */
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }

    /**
     * 获取两个日期中的最小日期
     *
     * @param beginDate
     * @param endDate
     * @return
     * @author chenl
     * @date 2020-01-15 08:43:00
     */
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }

    /**
     * 返回某月该季度的第一个月
     *
     * @param date
     * @return
     * @author chenl
     * @date 2020-01-15 08:43:09
     */
    public static Date getFirstSeasonDate(Date date) {
        final int[] season = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int sean = season[cal.get(Calendar.MONTH)];
        cal.set(Calendar.MONTH, sean * 3 - 3);
        return cal.getTime();
    }

    /**
     * 返回某个日期下几天的日期
     *
     * @param date
     * @param i
     * @return
     * @author chenl
     * @date 2020-01-15 08:43:18
     */
    public static Date getNextDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + i);
        return cal.getTime();
    }

    /**
     * 返回某个日期前几天的日期
     *
     * @param date
     * @param i
     * @return
     * @author chenl
     * @date 2020-01-15 08:43:25
     */
    public static Date getFrontDay(Date date, int i) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - i);
        return cal.getTime();
    }

    /**
     * 获取某年某月到某年某月按天的切片日期集合(间隔天数的集合)
     *
     * @param beginYear
     * @param beginMonth
     * @param endYear
     * @param endMonth
     * @param k
     * @return
     * @author chenl
     * @date 2020-01-15 08:43:34
     */
    public static List getTimeList(int beginYear, int beginMonth, int endYear,
                                   int endMonth, int k) {
        List list = new ArrayList();
        if (beginYear == endYear) {
            for (int j = beginMonth; j <= endMonth; j++) {
                list.add(getTimeList(beginYear, j, k));
            }
        } else {
            {
                for (int j = beginMonth; j < 12; j++) {
                    list.add(getTimeList(beginYear, j, k));
                }
                for (int i = beginYear + 1; i < endYear; i++) {
                    for (int j = 0; j < 12; j++) {
                        list.add(getTimeList(i, j, k));
                    }
                }
                for (int j = 0; j <= endMonth; j++) {
                    list.add(getTimeList(endYear, j, k));
                }
            }
        }
        return list;
    }

    /**
     * 获取月的周
     *
     * @param dateStr
     * @return
     * @throws ParseException
     * @author chenl
     * @date 2020-08-26 09:02:33
     */
    public static int getWeekOfMonth(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        int weeks = 6;
        Date s;
        try {
            s = sdf.parse(dateStr);
            Calendar ca = Calendar.getInstance();
            ca.setTime(s);
            ca.setFirstDayOfWeek(Calendar.MONDAY);
            weeks = ca.getActualMaximum(Calendar.WEEK_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return weeks;
    }

    /**
     * 根据日期字符串判断当月第几周
     *
     * @param dateStr
     * @return
     * @throws Exception
     */
    public Integer calculateWeekInMonth(String dateStr) {
        int day = Func.toInt(dateStr.replaceAll("-", ""));
        if (day < 19700101 || day > 99999999) {
            throw new RuntimeException("时间不正确");
        }
        // ISO算法：每个月的第一周至少4天，如果小于4天，算出来是第0周
        // WeekFields weekFields = WeekFields.ISO;
        // 以周一作为一周的开始，每周至少一天
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        int monthInWeek = LocalDateTime.of(day / 10000, (day / 100) % 100, day % 100, 0, 0, 0).atZone(ZoneOffset.ofHours(8)).get(weekFields.weekOfMonth());
        return monthInWeek;
    }

    /**
     * 日期转时间戳
     *
     * @param date
     * @return
     */
    public static String transForMilliSecond(Date date) {
        if (date == null) return null;
        return Func.toStr(date.getTime());
    }

    /**
     * 根据日期获取当天是周几
     *
     * @param datetime 日期
     * @return 周几
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = sdf.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[w];
    }

    /**
     * 获取某年某月按天切片日期集合(某个月间隔多少天的日期集合)
     *
     * @param beginYear
     * @param beginMonth
     * @param k
     * @return
     * @author chenl
     * @date 2020-01-15 08:43:44
     */
    public static List getTimeList(int beginYear, int beginMonth, int k) {
        List list = new ArrayList();
        Calendar begincal = new GregorianCalendar(beginYear, beginMonth, 1);
        int max = begincal.getActualMaximum(Calendar.DATE);
        for (int i = 1; i < max; i = i + k) {
            list.add(begincal.getTime());
            begincal.add(Calendar.DATE, k);
        }
        begincal = new GregorianCalendar(beginYear, beginMonth, max);
        list.add(begincal.getTime());
        return list;
    }

    /**
     * 获取当前时间戳（yyyyMMddHHmmss）
     *
     * @return nowTimeStamp
     */
    public static long getCurrentTimestamp() {
        long nowTimeStamp = Long.parseLong(getCurrentTimestampStr());
        return nowTimeStamp;
    }

    /**
     * 获取当前时间戳（yyyyMMddHHmmss）
     *
     * @return
     */
    public static String getCurrentTimestampStr() {
        return format(new Date(), "yyyyMMddHHmmss");
    }
    
    public static Integer getYear(Date startTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return cal.get(Calendar.YEAR);
    }
    public static Integer getMonth(Date startTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return cal.get(Calendar.MONTH) + 1;
    }
    public static Integer getHourOfDay(Date startTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    public static Integer getMinute(Date startTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return cal.get(Calendar.MINUTE);
    }
    public static Integer getSecond(Date startTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return cal.get(Calendar.SECOND);
    }
    public static Integer getDayOfMonth(Date startTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    public static Integer getMaxDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DATE);
    }
    public static String getWeekString(Integer... dayOfWeeks) {
        if (dayOfWeeks != null && dayOfWeeks.length != 0) {
            StringBuffer cronExp = new StringBuffer("");
            for (Integer integer : dayOfWeeks) {
                cronExp.append(integer).append(", ");
            }
            return cronExp.toString();
        }
        return null;
    }
    public static String getMonthString(Integer... dayOfWeeks) {
        if (dayOfWeeks != null && dayOfWeeks.length != 0) {
            StringBuffer cronExp = new StringBuffer("");
            for (Integer integer : dayOfWeeks) {
                cronExp.append(integer).append(", ");
            }
            return cronExp.toString();
        }
        return null;
    }
    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     * @param startTimeStr
     * @param endTimeStr
     * @param pattern
     * @return  【0：当前时间在时间范围内，-1：之前；1：之后】
     */
    public static int isEffectiveDateInt(String startTimeStr, String endTimeStr, String... pattern) {
        SimpleDateFormat sdf = null;
        if (pattern == null || pattern.length == 0) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat(pattern[0]);
        }
        Date nowTime = null;
        Date startTime = null;
        Date endTime = null;
        if (startTimeStr == null || "".equals(startTimeStr)) {
            try {
                String format = sdf.format(new Date());
                nowTime = sdf.parse(format);
                endTime = sdf.parse(endTimeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (nowTime.getTime() == endTime.getTime()) {
                return 0;
            }
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);
            Calendar end = Calendar.getInstance();
            end.setTime(endTime);
            if (date.before(end)) {
                return 0;
            } else {
                return 1;
            }
        }
        if (endTimeStr == null || "".equals(endTimeStr)) {
            try {
                String format = sdf.format(new Date());
                nowTime = sdf.parse(format);
                startTime = sdf.parse(startTimeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (nowTime.getTime() == startTime.getTime()) {
                return 0;
            }
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);
            Calendar begin = Calendar.getInstance();
            begin.setTime(startTime);
            if (date.after(begin)) {
                return 0;
            } else {
                return -1;
            }
        }
        try {
            String format = sdf.format(new Date());
            nowTime = sdf.parse(format);
            startTime = sdf.parse(startTimeStr);
            endTime = sdf.parse(endTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
            return 0;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return 0;
        } else if (date.after(begin) && date.after(end)) {
            return 1;
        } else {
            return -1;
        }
    }
    /**
     * 时区 时间转换方法:将传入的时间（可能为其他时区）转化成目标时区对应的时间
     * @param sourceTime 时间格式必须为：yyyy-MM-dd HH:mm:ss
     * @param sourceId 入参的时间的时区id 比如：+08:00
     * @param targetId 要转换成目标时区id 比如：+09:00
     * @param reFormat 返回格式 默认：yyyy-MM-dd HH:mm:ss
     * @return string 转化时区后的时间
     */
    public static String timeConvert(String sourceTime, String sourceId,
                                     String targetId,String reFormat){
        //校验入参是否合法
        if (null == sourceId || "".equals(sourceId) || null == targetId
                || "".equals(targetId) || null == sourceTime
                || "".equals(sourceTime)){
            return null;
        }

        if(reFormat == null || "".equals(reFormat)){
            reFormat = "yyyy-MM-dd HH:mm:ss";
        }

        //校验 时间格式必须为：yyyy-MM-dd HH:mm:ss
        String reg = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$";
        if (!sourceTime.matches(reg)){
            return null;
        }

        try{
            //时间格式
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //根据入参原时区id，获取对应的timezone对象
            TimeZone sourceTimeZone = TimeZone.getTimeZone("GMT"+sourceId);
            //设置SimpleDateFormat时区为原时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成原时区对应的date对象
            df.setTimeZone(sourceTimeZone);
            //将字符串sourceTime转化成原时区对应的date对象
            java.util.Date sourceDate = df.parse(sourceTime);

            //开始转化时区：根据目标时区id设置目标TimeZone
            TimeZone targetTimeZone = TimeZone.getTimeZone("GMT"+targetId);
            //设置SimpleDateFormat时区为目标时区（否则是本地默认时区），目的:用来将字符串sourceTime转化成目标时区对应的date对象
            df.setTimeZone(targetTimeZone);
            //得到目标时间字符串
            String targetTime = df.format(sourceDate);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = sdf.parse(targetTime);
            sdf = new SimpleDateFormat(reFormat);

            return sdf.format(date);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当月天数
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     *
     * @param date yyyy-MM-dd
     * @return
     */
    public boolean compareGreaterToDay(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = new Date();
            Date dt2 = df.parse(date);
            if (dt1.getTime() > dt2.getTime()) {
                return true;
            } else if (dt1.getTime() < dt2.getTime()) {
                return false;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 获取两个月份。日期之间的月份/日期
     * @param startTime 开始
     * @param endTime 结束
     * @return List<String>
     */
    public static List<String> getMonthOrDayBetweenDate(String startTime, String endTime,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime()<=endDate.getTime()){
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                if("yyyy-MM".equals(pattern)){
                    calendar.add(Calendar.MONTH, 1);
                }else if ("yyyy-MM-dd".equals(pattern)) {
                    calendar.add(Calendar.DATE, 1);
                }else {
                    return null;
                }
                // 获取增加后的日期
                startDate=calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

}
