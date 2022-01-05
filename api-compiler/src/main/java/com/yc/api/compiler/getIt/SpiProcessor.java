package com.yc.api.compiler.getIt;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.yc.api.getIt.ServiceProviderInterface;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
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


@AutoService(Processor.class)
public class SpiProcessor extends AbstractProcessor {

    private Elements utils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ServiceProviderInterface.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
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
                final ClassName clazzSpi = ClassName.get(typeElement);
                final ClassName clazzLoader = ClassName.get(getClass().getPackage().getName(), "ServiceLoader");
                final ClassName clazzService = ClassName.get(packageName, getServiceName(typeElement));
                final ClassName clazzSingleton = ClassName.get(packageName, clazzService.simpleName(), "Singleton");
                final TypeSpec.Builder tsb = TypeSpec.classBuilder(clazzService)
                        .addJavadoc("Represents the service of {@link $T}\n", clazzSpi)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                        .addSuperinterface(clazzSpi)
                        .addType(TypeSpec.classBuilder(clazzSingleton.simpleName())
                                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                                .addField(FieldSpec.builder(clazzService, "INSTANCE", Modifier.STATIC, Modifier.FINAL)
                                        .initializer("new $T()", clazzService)
                                        .build())
                                .build())
                        .addField(FieldSpec.builder(clazzSpi, "mDelegate", Modifier.PRIVATE, Modifier.FINAL)
                                .initializer("$T.load($T.class).get()", clazzLoader, clazzSpi)
                                .build())
                        .addMethod(MethodSpec.constructorBuilder()
                                .addModifiers(Modifier.PRIVATE)
                                .build())
                        .addMethod(MethodSpec.methodBuilder("getInstance")
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                                .addStatement("return $T.INSTANCE", clazzSingleton)
                                .returns(clazzService)
                                .build());

                System.out.println("Generate " + clazzService.toString());

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
                        case BOOLEAN:
                            msb.addStatement("return null != this.mDelegate && this.mDelegate.$L($L)", methodName, args);
                            break;
                        case BYTE:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : (byte) 0", methodName, args);
                            break;
                        case SHORT:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : (short) 0", methodName, args);
                            break;
                        case INT:
                        case FLOAT:
                        case LONG:
                        case DOUBLE:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : 0", methodName, args);
                            break;
                        case CHAR:
                            msb.addStatement("return null != this.mDelegate ? this.mDelegate.$L($L) : '\0'", methodName, args);
                            break;
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
                    JavaFile.builder(getPackageName(typeElement), tsb.build())
                            .indent("    ")
                            .addFileComment("\nAutomatically generated file. DO NOT MODIFY\n")
                            .skipJavaLangImports(true)
                            .build()
                            .writeTo(processingEnv.getFiler());
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private String getServiceName(final TypeElement typeElement) {
        final String simpleName = typeElement.getSimpleName().toString();
        if (simpleName.endsWith("ServiceProvider")) {
            return simpleName.substring(0, simpleName.length() - 8);
        }

        return simpleName + "Service";
    }

    private String getPackageName(final TypeElement typeElement) {
        return this.utils.getPackageOf(typeElement).getQualifiedName().toString();
    }

}
