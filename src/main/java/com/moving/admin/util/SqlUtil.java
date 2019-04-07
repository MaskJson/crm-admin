package com.moving.admin.util;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SqlUtil {

    public static String getIn(List<Long> ids, String key) {
        int len = ids.size();
        if (len == 0) {
            return "";
        }
        String str = key + " in(";
        for (int i = 0; i < len; i++) {
            str = str + (i<(len - 1) ? ids.get(i).toString() + "," : ids.get(i).toString());
        }
        str = str + ")";
        return str;
    }

}
