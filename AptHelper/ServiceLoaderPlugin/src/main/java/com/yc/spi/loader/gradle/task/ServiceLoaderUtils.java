package com.yc.spi.loader.gradle.task;


import com.android.ddmlib.Log;
import com.yc.spi.annotation.ServiceProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

public final class ServiceLoaderUtils {

    public static Annotation getServiceProviderAnnotation(final ClassFile cf) {
        final AnnotationsAttribute visibleAttr = (AnnotationsAttribute)
                cf.getAttribute(AnnotationsAttribute.visibleTag);
        if (null != visibleAttr) {
            //找到修饰了注解 ServiceProvider 的类
            final Annotation sp = visibleAttr.getAnnotation(ServiceProvider.class.getName());
            if (null != sp) {
                return sp;
            }
        }
        final AnnotationsAttribute invisibleAttr = (AnnotationsAttribute)
                cf.getAttribute(AnnotationsAttribute.invisibleTag);
        if (null != invisibleAttr) {
            //找到修饰了注解 ServiceProvider 的类
            final Annotation sp = invisibleAttr.getAnnotation(ServiceProvider.class.getName());
            if (null != sp) {
                return sp;
            }
        }
        return null;
    }

    public static List<CtClass> loadClasses(final ClassPool pool, final List<CtClass> classes,
                                      final File file) throws IOException {
        final Stack<File> stack = new Stack<File>();
        stack.push(file);
        while (!stack.isEmpty()) {
            final File f = stack.pop();
            if (f.isDirectory()) {
                final File[] files = f.listFiles();
                if (null != files) {
                    for (final File child : files) {
                        stack.push(child);
                    }
                }
            } else if (f.getName().endsWith(".class")) {
                FileInputStream stream = null;
                try {
                    stream = new FileInputStream(f);
                    classes.add(pool.makeClass(stream));
                } finally {
                    if (null != stream) {
                        try {
                            stream.close();
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (f.getName().endsWith(".jar")) {
                loadClasses(pool, classes, new JarFile(f));
            }
        }
        return classes;
    }


    private static List<CtClass> loadClasses(final ClassPool pool, final List<CtClass> classes,
                                      final JarFile jar) throws IOException {
        InputStream stream = null;
        final Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                try {
                    stream = jar.getInputStream(entry);
                    classes.add(pool.makeClass(stream));
                } finally {
                    if (null != stream) {
                        stream.close();
                    }
                }
            }
        }
        return classes;
    }

    public static void delete(final File file) {
        final Stack<File> stack = new Stack<>();
        stack.push(file);
        while (!stack.isEmpty()) {
            final File f = stack.pop();
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                final File[] files = f.listFiles();
                if (null != files && files.length > 0) {
                    for (int i = 0, n = files.length; i < n; i++) {
                        stack.push(files[i]);
                    }
                }
            }
        }
    }

    public static void log(String string){
        Log.d("spi plugin log : ",string);
        System.out.println("system out spi plugin log : " + string);
    }
}
