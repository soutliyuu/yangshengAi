package com.sp.yangshengai.utils;

import cn.hutool.core.date.LocalDateTimeUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 时间范围查询工具
 */
@Data
@NoArgsConstructor
public class StartAndEnd {

    private LocalDateTime start;
    private LocalDateTime end = LocalDateTime.now();
    private String sqlFormat = "YYYY MM DD HH24";
    // 用于计算时间间隔的单位
    private ChronoUnit chronoUnit;
    private String str;
    //private QueryTime queryTime;

    private TimeEnum timeEnum;


    public StartAndEnd(LocalDateTime start, LocalDateTime end) {
        initDateTime(start, end);
        resetStartTime();
    }


    public StartAndEnd(TimeEnum timeEnum) {
        this.timeEnum = timeEnum;
        if (timeEnum == TimeEnum.TY) {
            this.chronoUnit = ChronoUnit.YEARS;
            this.str = "year";
            this.sqlFormat = "YYYY MM";
            this.start = end.minusMonths(11);
        } else if (timeEnum == TimeEnum.TM) {
            this.str = "month";
            this.chronoUnit = ChronoUnit.DAYS;
            this.sqlFormat = "YYYY MM DD";
            this.start = end.minusDays(30);

        } else {
            this.str = "week";
            this.chronoUnit = ChronoUnit.DAYS;
            this.sqlFormat = "YYYY MM DD";
            this.start = end.minusDays(6);
        }

    }

    public StartAndEnd(String str) {
        this.str = str;
        switch (str) {
            case "day":
                //过滤当前小时
                this.start = end.minusHours(24);
                end = end.minusHours(1);
                this.sqlFormat = "YYYY MM DD HH24";
                this.chronoUnit = ChronoUnit.HOURS;
                break;
            case "week":
                //过滤当天
                this.start = end.minusDays(7);
                end = end.minusDays(1);
                this.chronoUnit = ChronoUnit.DAYS;
                this.sqlFormat = "YYYY MM DD";
                break;
            case "month":
                //过滤当天
                this.start = end.minusDays(30);
                end = end.minusDays(1);
                this.chronoUnit = ChronoUnit.DAYS;
                this.sqlFormat = "YYYY MM DD";
                break;
            case "year":
                //过滤当月
                this.start = end.minusMonths(12);
                end = end.minusMonths(1);
                this.chronoUnit = ChronoUnit.MONTHS;
                this.sqlFormat = "YYYY MM";
                break;
            default:
                // , 分隔
                List<String> timeList = StringUtils.splitList(str, ",");
                LocalDateTime start = LocalDateTimeUtil.parse(timeList.get(0), "yyyy-MM-dd HH:mm:ss");
                LocalDateTime end = LocalDateTimeUtil.parse(timeList.get(1), "yyyy-MM-dd HH:mm:ss");
                initDateTime(start, end);
                break;
        }
        resetStartTime();
    }

    private void initDateTime(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
        //
        long days = ChronoUnit.DAYS.between(this.start, this.end); // 相差天
        long months = ChronoUnit.MONTHS.between(this.start, this.end);
        String format;
        if (days < 7) { // 小于1周小时显示
            format = "YYYY MM DD HH24";
            this.chronoUnit = ChronoUnit.HOURS;
        } else if (months < 6) { // 小于6个月，按天统计
            format = "YYYY MM DD";
            this.chronoUnit = ChronoUnit.DAYS;
        } else { // 按月统计
            format = "YYYY MM";
            this.chronoUnit = ChronoUnit.MONTHS;
        }
        this.sqlFormat = format;
    }

    private void resetStartTime() {
        // 重置 startTime
        switch (this.chronoUnit) {
            case DAYS -> this.start = start.with(LocalTime.MIN); // 按天查询
            case MONTHS -> this.start = LocalDateTime.of(start.getYear(), start.getMonth(), 1, 0, 0); // 月
            default -> this.start = LocalDateTime.of(start.toLocalDate(), LocalTime.of(start.getHour(), 0)); // 小时
        }
    }

    public String getStartStr() {
        return LocalDateTimeUtil.format(this.start, "yyyy-MM-dd HH:mm:ss");
    }

    public String getEndStr() {
        return LocalDateTimeUtil.format(this.end, "yyyy-MM-dd HH:mm:ss");
    }

    public String getXAxisTimeStr(String timeStr) {
        String[] split = timeStr.split(" ");
        String time;
        switch (this.chronoUnit) {
            case HOURS -> time = "%s时".formatted(split[3]);
            case DAYS -> {
                if ("week".equals(this.str)) {
                    LocalDateTime dateTime = LocalDateTime.of(
                        Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 0, 0);
                    time = "%s".formatted(getYAxisTimeStr(dateTime.getDayOfWeek().getValue()));
                } else {
                    time = "%s日".formatted(split[2]);
                }
            }
            case MONTHS -> time = "%s月".formatted(split[1]);
            case YEARS -> time = "%s月".formatted(split[1]);
            default -> time = "";
        }
        return time;
    }

    public String localDateTimeFormat(LocalDateTime dateTime) {
        return switch (this.chronoUnit) {
            case HOURS -> LocalDateTimeUtil.format(dateTime, "yyyy MM dd HH");
            case DAYS -> LocalDateTimeUtil.format(dateTime, "yyyy MM dd");
            case MONTHS -> LocalDateTimeUtil.format(dateTime, "yyyy MM");
            default -> "";
        };
    }

    public List<String> getTimeListBetween() {
        List<String> timeList = new ArrayList<>();
        LocalDateTime current = this.start;
        while (!current.isAfter(this.end)) {
            switch (this.chronoUnit) {
                case YEARS -> {
                    // 按年统计时，格式化为 "年 月" 的格式字符串
                    String formattedTime = current.getYear() + " " + String.format("%02d", current.getMonthValue());
                    timeList.add(formattedTime);
                    current = current.plusMonths(1);
                }
                case MONTHS -> {
                    // 按月统计时，格式化为 "年 月 日" 的格式字符串
                    String formattedTime = current.getYear() + " " + String.format("%02d", current.getMonthValue()) + " " + String.format("%02d", current.getDayOfMonth());
                    timeList.add(formattedTime);
                    current = current.plusMonths(1);
                }
                case DAYS -> {
                    // 按天统计（包含按周的情况，因为按周也是按天来遍历时间范围），格式化为 "年 月 日" 的格式字符串
                    String formattedTime = current.getYear() + " " + String.format("%02d", current.getMonthValue()) + " " + String.format("%02d", current.getDayOfMonth());
                    timeList.add(formattedTime);
                    current = current.plusDays(1);
                }
                case HOURS -> {
                    // 按小时统计时，格式化为 "年 月 日 时" 的格式字符串
                    String formattedTime = current.getYear() + " " + current.getMonthValue() + " " + current.getDayOfMonth() + " " + current.getHour();
                    timeList.add(formattedTime);
                    current = current.plusHours(1);
                }
            }
        }
        return timeList;
    }


    public static String getYAxisTimeStr(int dayOfWeek) {
        return WEEK_MAP.get(dayOfWeek);
    }

    private static final Map<Integer, String> WEEK_MAP = Map
        .of(1, "周一", 2, "周二", 3, "周三", 4, "周四", 5, "周五", 6, "周六", 7, "周日");


    public static void main(String[] args) {
        StartAndEnd day = new StartAndEnd("day");
        StartAndEnd week = new StartAndEnd("week");
        StartAndEnd month = new StartAndEnd("month");
        System.out.println(day.getStartStr() + " - " + day.getEndStr() + " - " + Duration.between(day.getStart(), day.getEnd()).toHours());
        System.out.println(week.getStartStr() + " - " + week.getEndStr() + " - " + Duration.between(week.getStart(), week.getEnd()).toDays());
        System.out.println(month.getStartStr() + " - " + month.getEndStr() + " - " + Duration.between(month.getStart(), month.getEnd()).toDays());
    }

}
