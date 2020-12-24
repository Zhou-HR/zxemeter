/*
 * Copyright Notice:
 *      Copyright  1998-2008, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */
package com.gdiot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ZhouHR
 */
public class StringUtil {

    public static boolean strIsNullOrEmpty(String s) {
        return (null == s || s.trim().length() < 1);
    }

    public static boolean isEmpty(String s) {
        return (s == null || s.trim().length() < 1);
    }

    public static String getTimeCodeStr() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ssmmHH");
        String sb = "0241" + sdf.format(now) +
                "00";
        return sb;
    }

}
