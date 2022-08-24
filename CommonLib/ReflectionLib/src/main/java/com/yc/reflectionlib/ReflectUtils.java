package com.yc.reflectionlib;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public final class ReflectUtils {

    static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    private ReflectUtils() {

    }

    static boolean isSameLength(Object[] array1, Object[] array2) {
        return (array1 != null || array2 == null || array2.length <= 0)
                && (array2 != null || array1 == null || array1.length <= 0)
                && (array1 == null || array2 == null || array1.length == array2.length);
    }

    static Class<?>[] toClass(Object... array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return EMPTY_CLASS_ARRAY;
        } else {
            Class<?>[] classes = new Class[array.length];
            for(int i = 0; i < array.length; ++i) {
                classes[i] = array[i] == null ? null : array[i].getClass();
            }
            return classes;
        }
    }

    static Class<?>[] nullToEmpty(Class<?>[] array) {
        return array != null && array.length != 0 ? array : EMPTY_CLASS_ARRAY;
    }

    static Object[] nullToEmpty(Object[] array) {
        return array != null && array.length != 0 ? array : EMPTY_OBJECT_ARRAY;
    }

    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        if (cls == null) {
            return null;
        } else {
            LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<>();
            getAllInterfaces(cls, interfacesFound);
            return new ArrayList<>(interfacesFound);
        }
    }

    private static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
        while(cls != null) {
            Class<?>[] interfaces = cls.getInterfaces();
            int length = interfaces.length;
            for(int i = 0; i < length; ++i) {
                Class<?> aClass = interfaces[i];
                if (interfacesFound.add(aClass)) {
                    getAllInterfaces(aClass, interfacesFound);
                }
            }
            cls = cls.getSuperclass();
        }

    }


    public static final Class<?>[] EMPTY_PARAM_TYPES = new Class<?>[0];
    public static final Object[] EMPTY_PARAMS = new Object[0];

    /* ************************************************** 字段相关的方法 ******************************************************* */
    /**
     * 从指定的类中获取指定的字段
     * @param sourceClass 指定的类
     * @param fieldName 要获取的字段的名字
     * @param isFindDeclaredField 是否查找Declared字段
     * @param isUpwardFind 是否向上去其父类中寻找
     * @return
     */
    public static Field getField(Class<?> sourceClass, String fieldName, boolean isFindDeclaredField, boolean isUpwardFind){
        Field field = null;
        try {
            field = isFindDeclaredField ? sourceClass.getDeclaredField(fieldName) : sourceClass.getField(fieldName);
        } catch (NoSuchFieldException e1) {
            if(isUpwardFind){
                Class<?> classs = sourceClass.getSuperclass();
                while(field == null && classs != null){
                    try {
                        field = isFindDeclaredField ? classs.getDeclaredField(fieldName) : classs.getField(fieldName);
                    } catch (NoSuchFieldException e11) {
                        classs = classs.getSuperclass();
                    }
                }
            }
        }
        return field;
    }
    /**
     * 从指定的类中获取指定的字段，默认获取Declared类型的字段、向上查找
     * @param sourceClass 指定的类
     * @param fieldName 要获取的字段的名字
     * @return
     */
    public static Field getField(Class<?> sourceClass, String fieldName){
        return getField(sourceClass, fieldName, true, true);
    }

    /**
     * 获取给定类的所有字段
     * @param sourceClass 给定的类
     * @param isGetDeclaredField 是否需要获取Declared字段
     * @param isGetParentField 是否需要把其父类中的字段也取出
     * @param isGetAllParentField 是否需要把所有父类中的字段全取出
     * @param isDESCGet 在最终获取的列表里，父类的字段是否需要排在子类的前面。只有需要把其父类中的字段也取出时此参数才有效
     * @return 给定类的所有字段
     */
    public static List<Field> getFields(Class<?> sourceClass, boolean isGetDeclaredField, boolean isGetParentField, boolean isGetAllParentField, boolean isDESCGet){
        List<Field> fieldList = new ArrayList<Field>();
        //如果需要从父类中获取
        if(isGetParentField){
            //获取当前类的所有父类
            List<Class<?>> classList = null;
            if(isGetAllParentField){
                classList = getSuperClasss(sourceClass, true);
            }else{
                classList = new ArrayList<Class<?>>(2);
                classList.add(sourceClass);
                Class<?> superClass = sourceClass.getSuperclass();
                if(superClass != null){
                    classList.add(superClass);
                }
            }

            //如果是降序获取
            if(isDESCGet){
                for(int w = classList.size()-1; w > -1; w--){
                    for(Field field : isGetDeclaredField ? classList.get(w).getDeclaredFields() : classList.get(w).getFields()){
                        fieldList.add(field);
                    }
                }
            }else{
                for(int w = 0; w < classList.size(); w++){
                    for(Field field : isGetDeclaredField ? classList.get(w).getDeclaredFields() : classList.get(w).getFields()){
                        fieldList.add(field);
                    }
                }
            }
        }else{
            for(Field field : isGetDeclaredField ? sourceClass.getDeclaredFields() : sourceClass.getFields()){
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    /**
     * 获取给定类的所有字段
     * @param sourceClass 给定的类
     * @return 给定类的所有字段
     */
    public static List<Field> getFields(Class<?> sourceClass){
        return getFields(sourceClass, true, true, true, true);
    }

    /**
     * 设置给定的对象中给定名称的字段的值
     * @param object 给定的对象
     * @param fieldName 要设置的字段的名称
     * @param newValue 要设置的字段的值
     * @param isFindDeclaredField 是否查找Declared字段
     * @param isUpwardFind 如果在当前类中找不到的话，是否取其父类中查找
     * @return 设置是否成功。false：字段不存在或新的值与字段的类型不一样，导致转型失败
     */
    public static boolean setField(Object object, String fieldName, Object newValue, boolean isFindDeclaredField, boolean isUpwardFind){
        boolean result = false;
        Field field = getField(object.getClass(), fieldName, isFindDeclaredField, isUpwardFind);
        if(field != null){
            try {
                field.setAccessible(true);
                field.set(object, newValue);
                result = true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }


    /* ************************************************** 方法相关的方法 ******************************************************* */
    /**
     * 从指定的类中获取指定的方法
     * @param sourceClass 给定的类
     * @param isFindDeclaredMethod 是否查找Declared字段
     * @param isUpwardFind 是否向上去其父类中寻找
     * @param methodName 要获取的方法的名字
     * @param methodParameterTypes 方法参数类型
     * @return 给定的类中给定名称以及给定参数类型的方法
     */
    public static Method getMethod(Class<?> sourceClass, boolean isFindDeclaredMethod, boolean isUpwardFind, String methodName, Class<?>... methodParameterTypes){
        Method method = null;
        try {
            method = isFindDeclaredMethod ? sourceClass.getDeclaredMethod(methodName, methodParameterTypes) : sourceClass.getMethod(methodName, methodParameterTypes);
        } catch (NoSuchMethodException e1) {
            if(isUpwardFind){
                Class<?> classs = sourceClass.getSuperclass();
                while(method == null && classs != null){
                    try {
                        method = isFindDeclaredMethod ? classs.getDeclaredMethod(methodName, methodParameterTypes) : classs.getMethod(methodName, methodParameterTypes);
                    } catch (NoSuchMethodException e11) {
                        classs = classs.getSuperclass();
                    }
                }
            }
        }
        return method;
    }

    /**
     * 从指定的类中获取指定的方法，默认获取Declared类型的方法、向上查找
     * @param sourceClass 指定的类
     * @param methodName 方法名
     * @param methodParameterTypes 方法参数类型
     * @return
     */
    public static Method getMethod(Class<?> sourceClass, String methodName, Class<?>... methodParameterTypes){
        return getMethod(sourceClass, true, true, methodName, methodParameterTypes);
    }

    /**
     * 从指定的类中获取指定名称的不带任何参数的方法，默认获取Declared类型的方法并且向上查找
     * @param sourceClass 指定的类
     * @param methodName 方法名
     * @return
     */
    public static Method getMethod(Class<?> sourceClass, String methodName){
        return getMethod(sourceClass, methodName, EMPTY_PARAM_TYPES);
    }

    /**
     * 获取给定类的所有方法
     * @param clas 给定的类
     * @param isGetDeclaredMethod 是否需要获取Declared方法
     * @param isFromSuperClassGet 是否需要把其父类中的方法也取出
     * @param isDESCGet 在最终获取的列表里，父类的方法是否需要排在子类的前面。只有需要把其父类中的方法也取出时此参数才有效
     * @return 给定类的所有方法
     */
    public static List<Method> getMethods(Class<?> clas, boolean isGetDeclaredMethod, boolean isFromSuperClassGet, boolean isDESCGet){
        List<Method> methodList = new ArrayList<Method>();
        //如果需要从父类中获取
        if(isFromSuperClassGet){
            //获取当前类的所有父类
            List<Class<?>> classList = getSuperClasss(clas, true);

            //如果是降序获取
            if(isDESCGet){
                for(int w = classList.size()-1; w > -1; w--){
                    for(Method method : isGetDeclaredMethod ? classList.get(w).getDeclaredMethods() : classList.get(w).getMethods()){
                        methodList.add(method);
                    }
                }
            }else{
                for(int w = 0; w < classList.size(); w++){
                    for(Method method : isGetDeclaredMethod ? classList.get(w).getDeclaredMethods() : classList.get(w).getMethods()){
                        methodList.add(method);
                    }
                }
            }
        }else{
            for(Method method : isGetDeclaredMethod ? clas.getDeclaredMethods() : clas.getMethods()){
                methodList.add(method);
            }
        }
        return methodList;
    }

    /**
     * 获取给定类的所有方法
     * @param sourceClass 给定的类
     * @return 给定类的所有方法
     */
    public static List<Method> getMethods(Class<?> sourceClass){
        return getMethods(sourceClass, true, true, true);
    }

    /**
     * 获取给定的类中指定参数类型的ValuOf方法
     * @param sourceClass 给定的类
     * @param methodParameterTypes 方法参数类型
     * @return 给定的类中给定名称的字段的GET方法
     */
    public static Method getValueOfMethod(Class<?> sourceClass, Class<?>... methodParameterTypes){
        return getMethod(sourceClass, true, true, "valueOf", methodParameterTypes);
    }

    /**
     * 调用不带参数的方法
     * @param method
     * @param object
     * @return
     * @throws Exception
     */
    public static Object invokeMethod(Method method, Object object) throws
            Exception {
        return method.invoke(object, EMPTY_PARAMS);
    }

    /* ************************************************** 构造函数相关的方法 ******************************************************* */
    /**
     * 获取给定的类中给定参数类型的构造函数
     * @param sourceClass 给定的类
     * @param isFindDeclaredConstructor 是否查找Declared构造函数
     * @param isUpwardFind 是否向上去其父类中寻找
     * @param constructorParameterTypes 构造函数的参数类型
     * @return 给定的类中给定参数类型的构造函数
     */
    public static Constructor<?> getConstructor(Class<?> sourceClass, boolean isFindDeclaredConstructor, boolean isUpwardFind, Class<?>... constructorParameterTypes){
        Constructor<?> method = null;
        try {
            method = isFindDeclaredConstructor ? sourceClass.getDeclaredConstructor(constructorParameterTypes) : sourceClass.getConstructor(constructorParameterTypes);
        } catch (NoSuchMethodException e1) {
            if(isUpwardFind){
                Class<?> classs = sourceClass.getSuperclass();
                while(method == null && classs != null){
                    try {
                        method = isFindDeclaredConstructor ? sourceClass.getDeclaredConstructor(constructorParameterTypes) : sourceClass.getConstructor(constructorParameterTypes);
                    } catch (NoSuchMethodException e11) {
                        classs = classs.getSuperclass();
                    }
                }
            }
        }
        return method;
    }

    /**
     * 获取给定的类中所有的构造函数
     * @param sourceClass 给定的类
     * @param isFindDeclaredConstructor 是否需要获取Declared构造函数
     * @param isFromSuperClassGet 是否需要把其父类中的构造函数也取出
     * @param isDESCGet 在最终获取的列表里，父类的构造函数是否需要排在子类的前面。只有需要把其父类中的构造函数也取出时此参数才有效
     * @return 给定的类中所有的构造函数
     */
    public static List<Constructor<?>> getConstructors(Class<?> sourceClass, boolean isFindDeclaredConstructor, boolean isFromSuperClassGet, boolean isDESCGet){
        List<Constructor<?>> constructorList = new ArrayList<Constructor<?>>();
        //如果需要从父类中获取
        if(isFromSuperClassGet){
            //获取当前类的所有父类
            List<Class<?>> classList = getSuperClasss(sourceClass, true);

            //如果是降序获取
            if(isDESCGet){
                for(int w = classList.size()-1; w > -1; w--){
                    for(Constructor<?> constructor : isFindDeclaredConstructor ? classList.get(w).getDeclaredConstructors() : classList.get(w).getConstructors()){
                        constructorList.add(constructor);
                    }
                }
            }else{
                for(int w = 0; w < classList.size(); w++){
                    for(Constructor<?> constructor : isFindDeclaredConstructor ? classList.get(w).getDeclaredConstructors() : classList.get(w).getConstructors()){
                        constructorList.add(constructor);
                    }
                }
            }
        }else{
            for(Constructor<?> constructor : isFindDeclaredConstructor ? sourceClass.getDeclaredConstructors() : sourceClass.getConstructors()){
                constructorList.add(constructor);
            }
        }
        return constructorList;
    }


    /* ************************************************** 父类相关的方法 ******************************************************* */
    /**
     * 获取给定的类所有的父类
     * @param sourceClass 给定的类
     * @param isAddCurrentClass 是否将当年类放在最终返回的父类列表的首位
     * @return 给定的类所有的父类
     */
    public static List<Class<?>> getSuperClasss(Class<?> sourceClass, boolean isAddCurrentClass){
        List<Class<?>> classList = new ArrayList<Class<?>>();
        Class<?> classs;
        if(isAddCurrentClass){
            classs = sourceClass;
        }else{
            classs = sourceClass.getSuperclass();
        }
        while(classs != null){
            classList.add(classs);
            classs = classs.getSuperclass();
        }
        return classList;
    }


    /* ************************************************** 其它的辅助方法 ******************************************************* */
    /**
     * 获取给定的类的名字
     * @param sourceClass 给定的类
     * @return 给定的类的名字
     */
    public static String getClassName(Class<?> sourceClass){
        String classPath = sourceClass.getName();
        return classPath.substring(classPath.lastIndexOf('.')+1);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObjectByFieldName(Object object, String fieldName, Class<T> clas){
        if(object != null && fieldName!=null && clas != null){
            try {
                Field field = ReflectUtils.getField(object.getClass(), fieldName, true, true);
                if(field != null){
                    field.setAccessible(true);
                    return (T) field.get(object);
                }else{
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * 判断给定字段是否是type类型的数组
     * @param field
     * @param type
     * @return
     */
    public static final boolean isArrayByType(Field field, Class<?> type){
        Class<?> fieldType = field.getType();
        return fieldType.isArray() && type.isAssignableFrom(fieldType.getComponentType());
    }

    /**
     * 判断给定字段是否是type类型的collectionType集合，例如collectionType=List.class，type=Date.class就是要判断给定字段是否是Date类型的List
     * @param field
     * @param collectionType
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static final boolean isCollectionByType(Field field, Class<? extends Collection> collectionType, Class<?> type){
        Class<?> fieldType = field.getType();
        if(collectionType.isAssignableFrom(fieldType)){
            Class<?>
                    first = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
            return type.isAssignableFrom(first);
        }else{
            return false;
        }
    }
}
