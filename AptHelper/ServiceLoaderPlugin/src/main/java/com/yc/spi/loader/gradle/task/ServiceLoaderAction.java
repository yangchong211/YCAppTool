package com.yc.spi.loader.gradle.task;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;
import com.yc.spi.annotation.ServiceProvider;
import com.yc.spi.loader.ServiceLoader;

import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.lang.model.element.Modifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.MemberValue;


class ServiceLoaderAction {

    private final FileCollection classpath;

    private final File serviceDir;

    private final File sourcesDir;

    private final ClassPool pool;

    public ServiceLoaderAction(final FileCollection classpath, final File servicesDir, final File sourceDir) {
        this.classpath = classpath;
        this.serviceDir = servicesDir;
        this.sourcesDir = sourceDir;
        this.pool = new ClassPool(true) {
            @Override
            public ClassLoader getClassLoader() {
                return new Loader(this);
            }
        };
    }

    private List<CtClass> loadClasses() throws NotFoundException, IOException {
        final List<CtClass> classes = new LinkedList<CtClass>();

        for (final File file : this.classpath) {
            this.pool.appendClassPath(file.getAbsolutePath());
        }

        for (final File file : this.classpath) {
            ServiceLoaderUtils.loadClasses(this.pool, classes, file);
        }

        return classes;
    }

    public boolean execute() {
        ServiceLoaderUtils.delete(this.serviceDir);
        ServiceLoaderUtils.delete(this.sourcesDir);

        try {
            final List<CtClass> classes = loadClasses();
            if (null != classes && classes.size() > 0) {
                for (final CtClass cc : classes) {
                    processClass(cc);
                }
            }

            generateSourceCode();
        } catch (Exception e) {
            throw new GradleException("Could not generate ServiceRegistry", e);
        }

        return true;
    }

    private void generateSourceCode() throws IOException {
        final String packageName = ServiceLoader.class.getPackage().getName();
        final TypeVariableName s = TypeVariableName.get("S");
        final TypeVariableName p = TypeVariableName.get("P", s);
        final WildcardTypeName any = WildcardTypeName.subtypeOf(Object.class);
        final WildcardTypeName subTypeOfS = WildcardTypeName.subtypeOf(s);
        final ClassName java_lang_Class = ClassName.get(Class.class);
        final ClassName java_util_Collections = ClassName.get("java.util", "Collections");
        final ClassName java_util_Map = ClassName.get("java.util", "Map");
        final ClassName java_util_Set = ClassName.get("java.util", "Set");
        final ClassName java_util_LinkedHashMap = ClassName.get("java.util", "LinkedHashMap");
        final ClassName java_util_LinkedHashSet = ClassName.get("java.util", "LinkedHashSet");
        final ClassName instantiator = ClassName.get("java.util.concurrent", "Callable");
        final TypeName classOfAny = ParameterizedTypeName.get(java_lang_Class, any);
        final TypeName instantiatorOfAny = ParameterizedTypeName.get(instantiator, any);
        final TypeName instantiatorOfP = ParameterizedTypeName.get(instantiator, p);
        final TypeName classOfS = ParameterizedTypeName.get(java_lang_Class, s);
        final TypeName classOfP = ParameterizedTypeName.get(java_lang_Class, p);
        final TypeName classOfSubTypeOfS = ParameterizedTypeName.get(java_lang_Class, subTypeOfS);
        final TypeName setOfClass = ParameterizedTypeName.get(java_util_Set, classOfAny);
        final TypeName setOfClassOfSubTypeOfS = ParameterizedTypeName.get(java_util_Set, classOfSubTypeOfS);
        final TypeName linkedHashSetOfClass = ParameterizedTypeName.get(java_util_LinkedHashSet, classOfAny);
        final TypeName mapOfClassToSetOfClass = ParameterizedTypeName.get(java_util_Map, classOfAny, setOfClass);
        final TypeName mapOfClassToInstantiator = ParameterizedTypeName.get(java_util_Map, classOfAny, instantiatorOfAny);
        final TypeName linkedHashMapOfClassToSetOfClass = ParameterizedTypeName.get(java_util_LinkedHashMap, classOfAny, setOfClass);
        final TypeName linkedHashMapOfClassToInstantializer = ParameterizedTypeName.get(java_util_LinkedHashMap, classOfAny, instantiatorOfAny);
        final TypeSpec tsServiceRegistry = TypeSpec.classBuilder("ServiceRegistry")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(FieldSpec.builder(mapOfClassToSetOfClass, "sServices")
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("new $T()", linkedHashMapOfClassToSetOfClass)
                        .build())
                .addField(FieldSpec.builder(mapOfClassToInstantiator, "sInstantiators")
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("new $T()", linkedHashMapOfClassToInstantializer)
                        .build())
                .addStaticBlock(generateStaticInitializer())
                .addMethod(MethodSpec.methodBuilder("register")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SYNCHRONIZED)
                        .addTypeVariable(s)
                        .addTypeVariable(p)
                        .addParameter(classOfS, "serviceClass", Modifier.FINAL)
                        .addParameter(classOfP, "providerClass", Modifier.FINAL)
                        .returns(TypeName.VOID)
                        .addCode(CodeBlock.builder()
                                .beginControlFlow("if (null == serviceClass)")
                                .addStatement("throw new IllegalArgumentException($S)", "service class is null")
                                .endControlFlow()
                                .beginControlFlow("if (null == providerClass)")
                                .addStatement("throw new IllegalArgumentException($S)", "provider class is null")
                                .endControlFlow()
                                .addStatement("register(serviceClass, providerClass, new $T() { @Override public $T call() throws Exception { return providerClass.newInstance(); }})", instantiatorOfP, p)
                                .build())
                        .build())
                .addMethod(MethodSpec.methodBuilder("register")
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.SYNCHRONIZED)
                        .addTypeVariable(s)
                        .addTypeVariable(p)
                        .addParameter(classOfS, "serviceClass", Modifier.FINAL)
                        .addParameter(classOfP, "providerClass", Modifier.FINAL)
                        .addParameter(instantiatorOfP, "instantiator", Modifier.FINAL)
                        .returns(TypeName.VOID)
                        .addCode(CodeBlock.builder()
                                .addStatement("$T providers = sServices.get(serviceClass)", setOfClass)
                                .beginControlFlow("if (null == providers)")
                                .addStatement("providers = new $T()", linkedHashSetOfClass)
                                .endControlFlow()
                                .addStatement("providers.add(providerClass)")
                                .addStatement("sServices.put(serviceClass, providers)")
                                .addStatement("sInstantiators.put(providerClass, instantiator)")
                                .build())
                        .build())
                .addMethod(MethodSpec.methodBuilder("get")
                        .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                                .addMember("value", "$S", "unchecked")
                                .build())
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.SYNCHRONIZED)
                        .addTypeVariable(s)
                        .addParameter(classOfS, "clazz", Modifier.FINAL)
                        .returns(setOfClassOfSubTypeOfS)
                        .addCode(CodeBlock.builder()
                                .addStatement("final $T providers = sServices.get(clazz)", setOfClass)
                                .addStatement("return ($T) (null == providers ? $T.emptySet() : $T.unmodifiableSet(providers))", setOfClassOfSubTypeOfS, java_util_Collections, java_util_Collections)
                                .build())
                        .build())
                .addMethod(MethodSpec.methodBuilder("newProvider")
                        .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                                .addMember("value", "$S", "unchecked")
                                .build())
                        .addModifiers(Modifier.STATIC, Modifier.SYNCHRONIZED)
                        .addTypeVariable(s)
                        .addParameter(classOfSubTypeOfS, "clazz", Modifier.FINAL)
                        .addException(ClassName.get(Exception.class))
                        .returns(s)
                        .addCode(CodeBlock.builder()
                                .addStatement("return ($T) sInstantiators.get(clazz).call()", s)
                                .build())
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .build();
        JavaFile.builder(packageName, tsServiceRegistry)
                .build()
                .writeTo(this.sourcesDir);
    }

    private CodeBlock generateStaticInitializer() throws IOException {
        final CodeBlock.Builder cinitBuilder = CodeBlock.builder();
        final File[] spis = this.serviceDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                return file.isFile();
            }
        });

        if (null != spis && spis.length > 0) {
            Arrays.sort(spis, new Comparator<File>() {
                @Override
                public int compare(final File lhs, final File rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });

            for (final File spi : spis) {
                final List<SpiElement> elements = new ArrayList<SpiElement>();
                final BufferedReader reader = new BufferedReader(new FileReader(spi));

                try {
                    for (String line; null != (line = reader.readLine()); ) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) {
                            continue;
                        }

                        final StringTokenizer tokenizer = new StringTokenizer(line);
                        final SpiElement ele = new SpiElement(tokenizer.nextToken(), tokenizer.hasMoreTokens() ? SpiElement.parsePriority(tokenizer.nextToken()) : 0);
                        if (!elements.contains(ele)) {
                            elements.add(ele);
                        }
                    }
                } finally {
                    reader.close();
                }

                Collections.sort(elements); // sort by priority asc

                for (final SpiElement se : elements) {
                    cinitBuilder.addStatement("register($L, $L, new Callable<$L>() { @Override public $L call() throws Exception { return new $L(); }})", spi.getName() + ".class", se.name + ".class", se.name, se.name, se.name);
                }
            }
        }

        return cinitBuilder.build();
    }

    private void processClass(final CtClass cc) throws IOException {
        if (!cc.hasAnnotation(ServiceProvider.class)) {
            return;
        }

        final ClassFile cf = cc.getClassFile();
        final Annotation annotation = ServiceLoaderUtils.getServiceProviderAnnotation(cf);
        if (null == annotation) {
            return;
        }

        //获取 ServiceProvider 的属性值
        final ArrayMemberValue value = (ArrayMemberValue) annotation.getMemberValue("value");
        final IntegerMemberValue priority = (IntegerMemberValue) annotation.getMemberValue("priority");
        final int priorityValue = null != priority ? priority.getValue() : 0;

        for (final MemberValue mv : value.getValue()) {
            final ClassMemberValue cmv = (ClassMemberValue) mv;
            final String serviceName = cmv.getValue();
            final File spi = new File(this.serviceDir, serviceName);

            if (!spi.exists()) {
                spi.createNewFile();
            }

            final PrintWriter out = new PrintWriter(new FileWriter(spi, true));
            out.printf("%s %d", cc.getName().replace('$', '.'), priorityValue).println();
            out.flush();
            out.close();
        }
    }

}
