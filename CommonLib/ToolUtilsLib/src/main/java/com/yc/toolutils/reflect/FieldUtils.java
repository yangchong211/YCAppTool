package com.yc.toolutils.reflect;


import android.text.TextUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     email : yangchong211@163.com
 *     time  : 2018/11/9
 *     desc  : 获取对象的变量
 *     revise: 之前搜车封装库
 *             getField() 方法获取的是非私有属性，并且 getField() 在当前 Class 获取不到时会向祖先类获取
 *             getDeclaredField() 获取的是 Class 中被 private 修饰的属性。
 * </pre>
 */
public final class FieldUtils {

    private static final Map<String, Field> sFieldCache = new HashMap<>();

    /**
     * Field[] allFields = class1.getDeclaredFields();//获取class对象的所有属性
     * Field[] publicFields = class1.getFields();//获取class对象的public属性
     * Field ageField = class1.getDeclaredField("age");//获取class指定属性
     * Field desField = class1.getField("des");//获取class指定的public属性
     */
    private FieldUtils() {

    }

    /**
     * 获取自身的所有的 public 属性，包括从父类继承下来的
     * @param cls           cls
     * @return
     */
    public static Field[] getFields(Class<?> cls){
        return cls.getFields();
    }

    /**
     * 获取所有的属性，但不包括从父类继承下来的属性
     * @param cls           cls
     * @return
     */
    public static Field[] getDeclaredFields(Class<?> cls){
        return cls.getDeclaredFields();
    }

    /**
     * 获取class指定属性
     * getDeclaredField() 获取的是 Class 中被 private 修饰的属性
     * @param cls           cls
     * @param fieldName     属性名称
     * @return
     */
    public static Field getDeclaredField(Class<?> cls, String fieldName){
        return getDeclaredField(cls,fieldName,true);
    }

    /**
     * 获取class指定属性
     * @param cls           cls
     * @param fieldName     属性名称
     * @return
     */
    public static Field getDeclaredField(Class<?> cls, String fieldName, boolean forceAccess) {
        assertTrue(cls != null, "The class must not be null");
        assertTrue(!TextUtils.isEmpty(fieldName), "The field name must not be blank/empty");
        try {
            Field field = cls.getDeclaredField(fieldName);
            if (!MemberUtils.isAccessible(field)) {
                if (!forceAccess) {
                    return null;
                }
                field.setAccessible(true);
            }
            return field;
        } catch (NoSuchFieldException exception) {
            return null;
        }
    }

    /**
     * 获取class指定的public属性
     * getField() 方法获取的是非私有属性，并且 getField() 在当前 Class 获取不到时会向祖先类获取
     * @param cls           cls
     * @param fieldName     属性名称
     * @return
     */
    public static Field getField(Class<?> cls, String fieldName) {
        return getField(cls, fieldName, true);
    }

    /**
     * 获取filed属性的类型
     * @param field     field
     * @return
     */
    public static Class<?> getFiledType(Field field){
        // 直接使用getType()取出Field类型只对普通类型的Field有效
        Class<?> aClassType = field.getType();
        return aClassType;
    }

    /**
     * 获取filed属性的泛型类型
     * @param field     field
     * @return
     */
    public static Type[] getGenericType(Field field){
        Type gType = field.getGenericType();
        // 如果gType类型是ParameterizedType对象
        if(gType instanceof ParameterizedType){
            // 强制类型转换
            ParameterizedType pType = (ParameterizedType)gType;
            // 获取原始类型
            //Type rType = pType.getRawType();
            // 取得泛型类型的泛型参数
            Type[] tArgs = pType.getActualTypeArguments();
            return tArgs;
        } else{
            //获取泛型类型出错
            return null;
        }
    }


    /**
     * 获取class指定的public属性
     * @param cls           cls
     * @param fieldName     属性名称
     * @return
     */
    private static Field getField(Class<?> cls, String fieldName, boolean forceAccess) {
        assertTrue(cls != null, "The class must not be null");
        assertTrue(!TextUtils.isEmpty(fieldName), "The field name must not be blank/empty");
        String key = getKey(cls, fieldName);
        Field cachedField;
        synchronized(sFieldCache) {
            cachedField = sFieldCache.get(key);
        }
        if (cachedField != null) {
            if (forceAccess && !cachedField.isAccessible()) {
                cachedField.setAccessible(true);
            }
            return cachedField;
        } else {
            for(Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
                try {
                    Field field = acls.getDeclaredField(fieldName);
                    if (!Modifier.isPublic(field.getModifiers())) {
                        if (!forceAccess) {
                            continue;
                        }
                        field.setAccessible(true);
                    }
                    synchronized(sFieldCache) {
                        sFieldCache.put(key, field);
                    }
                    return field;
                } catch (NoSuchFieldException e) {
                }
            }
            Field match = null;
            for (Class<?> aClass : ReflectUtils.getAllInterfaces(cls)) {
                try {
                    Field test = aClass.getField(fieldName);
                    assertTrue(match == null, "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.", fieldName, cls);
                    match = test;
                } catch (NoSuchFieldException exception) {
                }
            }
            synchronized(sFieldCache) {
                sFieldCache.put(key, match);
                return match;
            }
        }
    }

    /**
     * 读取对象中成员变量
     * @param target            目标对象
     * @param fieldName         变量名称
     * @return                  对象
     * @throws IllegalAccessException       异常
     */
    public static Object readField(Object target, String fieldName) throws IllegalAccessException {
        assertTrue(target != null, "target object must not be null");
        Class<?> cls = target.getClass();
        Field field = getField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate field %s on %s", fieldName, cls);
        return readField(field, target, false);
    }

    /**
     * 读取对象中成员变量
     * @param target            目标对象
     * @param fieldName         变量名称
     * @param forceAccess       是否是暴力访问
     * @return                  对象
     * @throws IllegalAccessException       异常
     */
    public static Object readField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
        assertTrue(target != null, "target object must not be null");
        Class<?> cls = target.getClass();
        Field field = getField(cls, fieldName, forceAccess);
        assertTrue(field != null, "Cannot locate field %s on %s", fieldName, cls);
        return readField(field, target, forceAccess);
    }

    private static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {
        assertTrue(field != null, "The field must not be null");
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            MemberUtils.setAccessibleWorkaround(field);
        }
        return field.get(target);
    }

    /**
     * 修改对象中成员变量
     * @param target            目标对象
     * @param fieldName         变量名称
     * @param value             变量值
     * @throws IllegalAccessException       异常
     */
    public static void writeField(Object target, String fieldName, Object value) throws IllegalAccessException {
        writeField(target, fieldName, value, true);
    }

    /**
     * 修改对象中成员变量
     * @param target            目标对象
     * @param fieldName         变量名称
     * @param value             变量值
     * @param forceAccess           是否是暴力访问
     * @throws IllegalAccessException       异常
     */
    public static void writeField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException {
        assertTrue(target != null, "target object must not be null");
        Class<?> cls = target.getClass();
        Field field = getField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
        writeField(field, target, value, forceAccess);
    }

    /**
     * 修改对象中成员变量【私有】
     * @param target            目标对象
     * @param fieldName         变量名称
     * @param value             变量值
     * @throws IllegalAccessException       异常
     */
    public static void writeDeclaredField(Object target, String fieldName, Object value) throws IllegalAccessException {
        Class<?> cls = target.getClass();
        Field field = getDeclaredField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
        writeField(field, target, value, false);
    }


    private static void writeField(Field field, Object target, Object value, boolean forceAccess) throws IllegalAccessException {
        assertTrue(field != null, "The field must not be null");
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            MemberUtils.setAccessibleWorkaround(field);
        }
        field.set(target, value);
    }


    public static Object readStaticField(Class<?> cls, String fieldName) throws IllegalAccessException {
        Field field = getField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate field '%s' on %s", fieldName, cls);
        return readStaticField(field, true);
    }

    private static Object readStaticField(Field field, boolean forceAccess) throws IllegalAccessException {
        assertTrue(field != null, "The field must not be null");
        assertTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", field.getName());
        return readField((Field)field, (Object)null, forceAccess);
    }

    public static void writeStaticField(Class<?> cls, String fieldName, Object value) throws IllegalAccessException {
        Field field = getField(cls, fieldName, true);
        assertTrue(field != null, "Cannot locate field %s on %s", fieldName, cls);
        writeStaticField(field, value, true);
    }

    private static void writeStaticField(Field field, Object value, boolean forceAccess) throws IllegalAccessException {
        assertTrue(field != null, "The field must not be null");
        assertTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static", field.getDeclaringClass().getName(), field.getName());
        writeField((Field)field, (Object)null, value, forceAccess);
    }

    /**
     * 校验
     * @param expression        判断对象是否为空
     * @param message           message
     * @param values            values
     */
    private static void assertTrue(boolean expression, String message, Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }

    /**
     * 生成key值
     * @param cls           cls
     * @param fieldName     属性名称
     * @return
     */
    private static String getKey(Class<?> cls, String fieldName) {
        StringBuilder sb = new StringBuilder();
        sb.append(cls.toString())
                .append("@")
                .append(cls.getClassLoader())
                .append("#")
                .append(fieldName);
        return sb.toString();
    }

}

