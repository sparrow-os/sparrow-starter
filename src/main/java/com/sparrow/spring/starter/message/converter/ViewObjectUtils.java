package com.sparrow.spring.starter.message.converter;

import com.sparrow.protocol.VO;
import com.sparrow.utility.CollectionsUtility;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class ViewObjectUtils {
    public static boolean isBasicType(Class clazz) {
        if (clazz == byte.class || clazz == Byte.class) {
            return true;
        }

        if (clazz == char.class || clazz == Character.class) {
            return true;
        }

        if (clazz == short.class || clazz == Short.class) {
            return true;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return true;
        }

        if (clazz == long.class || clazz == Long.class) {
            return true;
        }

        if (clazz == float.class || clazz == Float.class) {
            return true;
        }

        if (clazz == boolean.class || clazz == Boolean.class) {
            return true;
        }

        if (clazz == double.class || clazz == Double.class) {
            return true;
        }

        if (clazz == Date.class) {
            return true;
        }

        if (clazz == Timestamp.class) {
            return true;
        }

        if (clazz == BigDecimal.class) {
            return true;
        }
        return false;
    }

    public static boolean isViewObject(Class clazz) {
        return VO.class.isAssignableFrom(clazz);
    }

    public static boolean isViewObjectList(List<?> objects) {
        if (CollectionsUtility.isNullOrEmpty(objects)) {
            return false;
        }
        Object o = objects.get(0);
        return VO.class.isAssignableFrom(o.getClass());
    }
}
