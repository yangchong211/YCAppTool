package com.yc.methodmonitor.plugin.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class TransformClassWriter extends ClassWriter {

    private ClassLoader mClassLoader;

    public TransformClassWriter(ClassReader classReader, int flags, ClassLoader mClassLoader) {
        super(classReader, flags);
        this.mClassLoader = mClassLoader;
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        Class<?> class1;
        Class<?> class2;
        try {
            class1 = Class.forName(type1.replace('/', '.'), false, mClassLoader);
            class2 = Class.forName(type2.replace('/', '.'), false, mClassLoader);
        } catch (Exception | Error e) {
            throw new RuntimeException(e.toString());
        }
        if (class1.isAssignableFrom(class2))
            return type1;
        if (class2.isAssignableFrom(class1))
            return type2;
        if ((class1.isInterface()) || (class2.isInterface()))
            return "java/lang/Object";
        do
            class1 = class1.getSuperclass();
        while (!class1.isAssignableFrom(class2));
        return class1.getName().replace('.', '/');
    }
}
