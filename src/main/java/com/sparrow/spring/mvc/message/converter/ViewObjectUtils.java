/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.spring.mvc.message.converter;

import com.sparrow.protocol.DTO;
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
        return DTO.class.isAssignableFrom(clazz);
    }

    public static boolean isViewObjectList(List<?> objects) {
        if (CollectionsUtility.isNullOrEmpty(objects)) {
            return false;
        }
        Object o = objects.get(0);
        return DTO.class.isAssignableFrom(o.getClass());
    }
}
