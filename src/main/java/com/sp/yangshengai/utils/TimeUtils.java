package com.sp.yangshengai.utils;



import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtils {

    /**
     * 将时间差转换为字符串 **天**小时**分钟
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getBetweenTimeStr(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null) {
            return "-";
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        StringBuilder sb = new StringBuilder();
        Duration duration = Duration.between(startTime, endTime);
        long days = duration.toDays();
        if (days > 0) {
            sb.append(days).append("天");
            duration = duration.minusDays(days);
        }
        long hours = duration.toHours();
        if (hours > 0) {
            sb.append(hours).append("小时");
            duration = duration.minusHours(hours);
        } else if (!sb.isEmpty()) {
            sb.append("0小时");
        }
        long minutes = duration.toMinutes();
        sb.append(minutes).append("分钟");
        return sb.toString();
    }

    /**
     * 将时间差转换为字符串 **d **h **m
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getBetweenTimeStrSecond(LocalDateTime startTime, LocalDateTime endTime) {
        String betweenTimeStr = getBetweenTimeStr(startTime, endTime);
        return betweenTimeStr.replace("天", "d ").replace("小时", "h ").replace("分钟", "m");
    }

    /**
     * 空格分隔的时间进行排序
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int compareStr(String str1, String str2) {
        String[] arr1 = StringUtils.split(str1, " ");
        String[] arr2 = StringUtils.split(str2, " ");
        int compare = 0;
        for (int i = 0; i < arr1.length; i++) {
            String a1 = arr1[i];
            String a2 = arr2[i];
            compare = Integer.valueOf(a1).compareTo(Integer.valueOf(a2));
            if (compare != 0) {
                return compare;
            }
        }
        return compare;
    }

}
