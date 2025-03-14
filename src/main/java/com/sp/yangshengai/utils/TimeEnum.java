package com.sp.yangshengai.utils;

import lombok.Getter;

@Getter
public enum TimeEnum {


    TY("YEAR", "本年"),
    TM("MONTH", "本月"),
    TW("WEEK", "本周");


    TimeEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    private final String key;
    private final String desc;
}
