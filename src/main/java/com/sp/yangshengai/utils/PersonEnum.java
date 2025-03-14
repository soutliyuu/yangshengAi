package com.sp.yangshengai.utils;

import lombok.Getter;

@Getter
public enum PersonEnum {


    SUGER("SUGER", "血糖"),
    WEIGHT("WEIGHT", "本月"),
    URIC("URIC", "本周");


    PersonEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    private final String key;
    private final String desc;
}
