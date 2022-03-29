package com.yc.spi.loader;

import com.yc.spi.annotation.ServiceProviderInterface;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;

/**
 * <pre>
 *     @author yangchong
 *     email  : https://github.com/yangchong211/
 *     time  : 2020/7/10
 *     desc  : apt
 *     revise: 使用javaopet库，然后通过拼接类的路径，类的修饰符，返回值，参数等多个元素，最后用jarvFile写到指定的目录文件。
 * </pre>
 */
@AutoService(Processor.class)
public class ServiceLoaderProcessor extends AbstractProcessor {

    private Elements utils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ServiceProviderInterface.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.utils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        for (final Element element : roundEnv.getElementsAnnotatedWith(ServiceProviderInterface.class)) {
            if (element instanceof TypeElement) {
                final TypeElement typeElement = (TypeElement) element;
                final String packageName = getPackageName(typeElement);
                log("package name : " + packageName);
                final ClassName clazzSpi = ClassName.get(typeElement);
                log("class name spi : " + clazzSpi.packageName());
                String name = getClass().getPackage().getName();
                log("get class package name : " + packageName);
                final ClassName clazzLoader = ClassName.get(name, "ServiceLoader");
                String serviceName = getServiceName(typeElement);
                log("get service name : " + serviceName);
                final ClassName clazzService = ClassName.get(packageName,serviceName);
                String simpleName = clazzService.simpleName();
                log("get simple name : " + serviceName);
                final ClassName clazzSingleton = ClassName.get(packageName, simpleName, "Singleton");
                log("get clazz singleton name : " + clazzSingleton);
                final TypeSpec.Builder tsb = TypeSpec.classBuilder(clazzService)
                        // 写入注释
                        .addJavadoc("Represents the service of {@link $T}\n", clazzSpi)
                        //写入 public final
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        //写入
                        .addSuperinterface(clazzSpi)
                        //写入方法
                        .addType(getClazzTypeSpec(clazzSingleton,clazzService))
                        //添加成员属性
                        .addField(FieldSpec.builder(clazzSpi, "mDelegate", Modifier.PRIVATE, Modifier.FINAL)
                                .initializer("$T.load($T.class).get()", clazzLoader, clazzSpi)
                                .build())
                        //添加私有方法
                        .addMethod(MethodSpec.constructorBuilder()
                                .addModifiers(Modifier.PRIVATE)
                                .build())
                        .addMethod(MethodSpec.methodBuilder("getInstance")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                .addStatement("return $T.INSTANCE", clazzSingleton)
                                .returns(clazzService)
                                .build());

                log("generate name : " + clazzService.toString());

                for (final ExecutableElement method : ElementFilter.methodsIn(typeElement.getEnclosedElements())) {
                    System.out.println(" + " + method);
                    final String methodName = method.getSimpleName().toString();
                    final TypeMirror returnType = method.getReturnType();
                    final MethodSpec.Builder msb = MethodSpec.methodBuilder(methodName)
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .addAnnotation(Override.class)
                            .returns(TypeName.get(returnType));

                    for (final TypeMirror thrownType : method.getThrownTypes()) {
                        msb.addException(ClassName.get(thrownType));
                    }

                    final StringBuilder args = new StringBuilder();
                    final List<? extends VariableElement> parameterTypes = method.getParameters();
                    for (int i = 0, n = parameterTypes.size(); i < n; i++) {
                        final String argName = "arg" + i;
                        msb.addParameter(TypeName.get(parameterTypes.get(i).asType()), argName, Modifier.FINAL);
                        args.append(argName).append(i < n - 1 ? ", " : "");
                    }

                    switch (returnType.getKind()) {
                        //布尔类型
                        case BOOLEAN:
                            msb.addStatement("return null != this.mDelegate && this.mDelegate.$L($L)", methodName, args);
                            break;
                        //字节
                        case BYTE:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : (byte) 0", methodName, args);
                            break;
                        //short类型
                        case SHORT:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : (short) 0", methodName, args);
                            break;
                        //int，float，long，double类型
                        case INT:
                        case FLOAT:
                        case LONG:
                        case DOUBLE:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : 0", methodName, args);
                            break;
                        //char字符类型
                        case CHAR:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : '\0'", methodName, args);
                            break;
                        //void，无返回值类型
                        case VOID:
                            msb.beginControlFlow("if (null != this.mDelegate)")
                                    .addStatement("this.mDelegate.$L($L)", methodName, args)
                                    .endControlFlow();
                            break;
                        default:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : null", methodName, args);
                            break;
                    }

                    tsb.addMethod(msb.build());
                }

                try {
                    Filer filer = processingEnv.getFiler();
                    log("write java filer content : " + filer.toString());
                    JavaFile.builder(getPackageName(typeElement), tsb.build())
                            .indent("    ")
                            .addFileComment("\nAutomatically generated file. DO NOT MODIFY\n")
                            .skipJavaLangImports(true)
                            .build()
                            .writeTo(filer);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private TypeSpec getClazzTypeSpec(ClassName clazzSingleton,ClassName clazzService){
        TypeSpec instance = TypeSpec.classBuilder(clazzSingleton.simpleName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .addField(FieldSpec.builder(clazzService, "INSTANCE", Modifier.STATIC, Modifier.FINAL)
                        .initializer("new $T()", clazzService)
                        .build())
                .build();
        String name = instance.name;
        log("get clazz type spec name : " + name);
        String s = instance.toString();
        log("get clazz type spec to string : " + s);
        return instance;
    }

    private String getServiceName(final TypeElement typeElement) {
        final String simpleName = typeElement.getSimpleName().toString();
        log("type element simple name : " + simpleName);
        if (simpleName.endsWith("ServiceProvider")) {
            return simpleName.substring(0, simpleName.length() - 8);
        }
        //注意：这个必须是包名
        return simpleName + "Service";
    }

    private String getPackageName(final TypeElement typeElement) {
        return this.utils.getPackageOf(typeElement).getQualifiedName().toString();
    }

    private void log(String string){
        System.out.println("processor log : " + string);
    }

}
