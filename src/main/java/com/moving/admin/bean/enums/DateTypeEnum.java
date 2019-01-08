package com.moving.admin.bean.enums;

import java.util.HashMap;
import java.util.Map;

public enum DateTypeEnum {
    TODAY(0, "today"),
    YESTERDAY(1, "yesterday"),
    THIS_WEEK(2, "week"),
    THIS_MONTH(3, "mouth");

    private static Map<Integer, DateTypeEnum> map = new HashMap<>();

    static {
        for (DateTypeEnum dateTypeEnum : DateTypeEnum.values()) {
            map.put(dateTypeEnum.getCode(), dateTypeEnum);
        }
    }

    private Integer code;
    private String msg;

    DateTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static DateTypeEnum getEnumByCode(Integer code) {
        return map.get(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
