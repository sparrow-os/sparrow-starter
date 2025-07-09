package com.sparrow.spring.starter;

import com.sparrow.protocol.EnumUniqueName;
import com.sparrow.protocol.KeyValue;
import com.sparrow.utility.EnumUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumsContainer {
    private Map<String, List<KeyValue<Integer, String>>> enums = new HashMap<>();

    public EnumsContainer(List<String> enums) throws ClassNotFoundException {
        for (String enumsClass : enums) {
            Class<?> c = Class.forName(enumsClass);
            String enumName = c.getSimpleName();
            if (c.isAnnotationPresent(EnumUniqueName.class)) {
                EnumUniqueName uniqueName = c.getAnnotation(EnumUniqueName.class);
                enumName = uniqueName.name();
            }
            List<KeyValue<Integer, String>> kvs = EnumUtility.getNames(c);
            this.enums.put(enumName, kvs);
        }
    }

    public List<KeyValue<Integer, String>> getEnums(String enumName) {
        return this.enums.get(enumName);
    }

    public List<String> getEnumsNames() {
        return new ArrayList<>(this.enums.keySet());
    }
}
