package com.yc.objectequallib;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2019/07/22
 *     desc  : 基于属性的比对器
 *     revise: https://github.com/yangchong211/
 * </pre>
 */
public class FieldBaseEquator extends AbstractEquator {

    private static final Map<Class<?>, Map<String, Field>> CACHE = new ConcurrentHashMap<>();

    public FieldBaseEquator() {
    }

    public FieldBaseEquator(boolean bothExistFieldOnly) {
        super(bothExistFieldOnly);
    }

    public FieldBaseEquator(List<String> includeFields, List<String> excludeFields, boolean bothExistFieldOnly) {
        super(includeFields, excludeFields, bothExistFieldOnly);
    }

    /**
     * 指定包含或排除某些字段
     *
     * @param includeFields 包含字段，若为 null 或空集，则不指定
     * @param excludeFields 排除字段，若为 null 或空集，则不指定
     */
    public FieldBaseEquator(List<String> includeFields, List<String> excludeFields) {
        super(includeFields, excludeFields);
    }

    /**
     * {@inheritDoc}
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<FieldInfo> getDiffFields(Object first, Object second) {
        if (first == second) {
            return Collections.emptyList();
        }
        // 先尝试判断是否为简单数据类型
        if (isSimpleField(first, second)) {
            return compareSimpleField(first, second);
        }
        Set<String> allFieldNames;
        // 获取所有字段
        Map<String, Field> firstFields = getAllFields(first);
        Map<String, Field> secondFields = getAllFields(second);
        if (first == null) {
            allFieldNames = secondFields.keySet();
        } else if (second == null) {
            allFieldNames = firstFields.keySet();
        } else {
            allFieldNames = getAllFieldNames(firstFields.keySet(), secondFields.keySet());
        }
        List<FieldInfo> diffFields = new LinkedList<>();
        for (String fieldName : allFieldNames) {
            try {
                Field firstField = firstFields.getOrDefault(fieldName, null);
                Field secondField = secondFields.getOrDefault(fieldName, null);
                Object firstVal = null;
                Class<?> firstType = null;
                Class<?> secondType = null;
                Object secondVal = null;
                if (firstField != null) {
                    firstField.setAccessible(true);
                    firstVal = firstField.get(first);
                    firstType = firstField.getType();
                }
                if (secondField != null) {
                    secondField.setAccessible(true);
                    secondVal = secondField.get(second);
                    secondType = secondField.getType();
                }
                FieldInfo fieldInfo = new FieldInfo(fieldName, firstType, secondType);
                fieldInfo.setFirstVal(firstVal);
                fieldInfo.setSecondVal(secondVal);
                if (!isFieldEquals(fieldInfo)) {
                    diffFields.add(fieldInfo);
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("获取属性进行比对发生异常: " + fieldName, e);
            }
        }
        return diffFields;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Map<String, Field> getAllFields(Object obj) {
        if (obj == null) {
            return Collections.emptyMap();
        }
        return CACHE.computeIfAbsent(obj.getClass(), k -> {
            Map<String, Field> fieldMap = new HashMap<>(8);
            Class<?> cls = k;
            while (cls != Object.class) {
                Field[] fields;
                if (cls != null) {
                    fields = cls.getDeclaredFields();
                    for (Field field : fields) {
                        // 一些通过字节码注入改写类的框架会合成一些字段，如 jacoco 的 $jacocoData 字段
                        // 正常情况下这些字段都需要被排除掉
                        if (!field.isSynthetic()) {
                            fieldMap.put(field.getName(), field);
                        }
                    }
                    cls = cls.getSuperclass();
                }
            }
            return fieldMap;
        });
    }
}
