package com.yc.api.compiler.route;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.yc.api.route.RouteConstants;
import com.yc.api.route.RouteImpl;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
@SupportedAnnotationTypes(RouteConstants.INTERFACE_NAME_ROUTE_IMPL)
public class RouteImplProcessor extends AbstractProcessor {

    /**
     * 文件生成器 类/资源
     */
    private Filer filer;
    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elements;
    private MyAnAnnotationValueVisitor annotationValueVisitor;

    /**
     * 初始化方法
     * @param processingEnvironment                 获取信息
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //文件生成器 类/资源
        filer = processingEnv.getFiler();
        //节点工具类 (类、函数、属性都是节点)
        elements = processingEnv.getElementUtils();
        annotationValueVisitor = new MyAnAnnotationValueVisitor();
    }


    /**
     * 所有的注解处理都是从这个方法开始的，你可以理解为，当APT找到所有需要处理的注解后，会回调这个方法，
     * 你可以通过这个方法的参数，拿到你所需要的信息。
     *
     * 参数Set<? extends TypeElement> annotations：将返回所有由该Processor处理，并待处理的Annotations。
     * (属于该Processor处理的注解，但并未被使用，不存在与这个集合里)
     *
     * 参数 RoundEnvironment roundEnv ：表示当前或是之前的运行环境，可以通过该对象查找找到的注解。
     * @param set                       annotations
     * @param roundEnvironment          roundEnvironment
     * @return                          返回值 表示这组 annotations 是否被这个 Processor 接受，
     *                                  如果接受true后续子的 Processor不会再对这个Annotations进行处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        /*
         * 1. set：携带getSupportedAnnotationTypes()中的注解类型，一般不需要用到。
         * 2. roundEnvironment：processor将扫描到的信息存储到roundEnvironment中，从这里取出所有使用注解的字段。
         */
        if (set==null || set.size()==0){
            return false;
        }
        for (TypeElement typeElement : set) {
            //返回用给定注释类型注释的元素。
            //注释可以直接显示，也可以继承。
            //只有在这轮注释处理中包含</i>的包元素和类型元素<i>，或者在这些元素中声明的成员、构造函数、参数或类型参数才会返回。
            //包含的类型元素是{@linkplain #getRootElements根类型}以及嵌套在根类型中的任何成员类型。
            //包中的元素不会被认为包含，因为为该包创建了一个{@code package-info}文件。
            //注意断点打印：com.zwwl.moduleb.ShowDialogImpl
            Set<? extends Element> annotated = roundEnvironment.getElementsAnnotatedWith(typeElement);
            for (Element apiImplElement : annotated) {
                //被 RouteImpl 注解的节点集合
                //注意断点打印：@com.yc.api.route.RouteImpl(value=com.zwwl.moduleinterface.IShowDialogManager)
                RouteImpl annotation = apiImplElement.getAnnotation(RouteImpl.class);
                if (annotation == null || !(apiImplElement instanceof TypeElement)) {
                    continue;
                }
                //节点工具类 (类、函数、属性都是节点)
                //apiImplElement 表示
                RouteContract<ClassName> apiNameContract = ElementTool.getApiClassNameContract(elements,
                        annotationValueVisitor,(TypeElement) apiImplElement);
                if (RouteConstants.LOG){
                    System.out.println("RouteImplProcessor--------process-------apiNameContract---"+apiNameContract);
                }
                //生成注解类相关代码
                TypeSpec typeSpec = buildClass(apiNameContract);
                String s = typeSpec.toString();
                if (RouteConstants.LOG){
                    System.out.println("RouteImplProcessor--------process-------typeSpec---"+s);
                }
                try {
                    //指定路径：com.yc.api.contract
                    String packageName = RouteConstants.PACKAGE_NAME_CONTRACT;
                    JavaFile.builder(packageName, typeSpec)
                            .build()
                            //文件生成器 类/资源
                            .writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private TypeSpec buildClass(RouteContract<ClassName> apiNameContract) {
        //获取接口的路径
        String simpleName = apiNameContract.getApi().simpleName();
        if (RouteConstants.LOG){
            //注意断点打印：IShowDialogManager
            System.out.println("RouteImplProcessor--------buildClass-------simpleName---"+simpleName);
        }
        //获取 com.yc.api.route.IRouteContract 信息，也就是IRouteContract接口的路径
        TypeElement typeElement = elements.getTypeElement(RouteConstants.INTERFACE_NAME_CONTRACT);
        ClassName className = ClassName.get(typeElement);
        if (RouteConstants.LOG){
            System.out.println("RouteImplProcessor--------buildClass-------className---"+className);
        }
        //自定义注解生成类的类名
        //例如：
        String name = simpleName + RouteConstants.SEPARATOR + RouteConstants.CONTRACT;
        if (RouteConstants.LOG){
            System.out.println("RouteImplProcessor--------buildClass-------name---"+name);
        }
        //这里面又有添加方法注解，添加修饰符，添加参数规格，添加函数题，添加返回值等等
        MethodSpec methodSpec = buildMethod(apiNameContract);
        //创建类名
        return TypeSpec.classBuilder(name)
                //添加super接口
                .addSuperinterface(className)
                //添加修饰符
                .addModifiers(Modifier.PUBLIC)
                //添加方法【然后这里面又有添加方法注解，添加修饰符，添加参数规格，添加函数题，添加返回值等等】
                .addMethod(methodSpec)
                //创建
                .build();
    }

    private MethodSpec buildMethod(RouteContract<ClassName> apiNameContract) {
        ClassName api = apiNameContract.getApi();
        ClassName apiImpl = apiNameContract.getApiImpl();
        if (RouteConstants.LOG){
            System.out.println("RouteImplProcessor--------buildMethod-------api---"+api + "----apiImpl---" + apiImpl);
        }
        String format = RouteConstants.INSTANCE_NAME_REGISTER + "." +
                RouteConstants.METHOD_NAME_REGISTER + "($T.class, $T.class)";
        if (RouteConstants.LOG){
            System.out.println("RouteImplProcessor--------buildMethod-------format---"+format);
        }
        ParameterSpec parameterSpec = buildParameterSpec();
        return MethodSpec.methodBuilder(RouteConstants.METHOD_NAME_REGISTER)
                //添加注解
                .addAnnotation(Override.class)
                //添加修饰符
                .addModifiers(Modifier.PUBLIC)
                //添加参数规格
                .addParameter(parameterSpec)
                //添加函数体
                .addStatement(format, api,apiImpl)
                .build();
    }

    private ParameterSpec buildParameterSpec() {
        //获取 com.yc.api.route.IRegister 信息，也就是IRegister接口的路径
        TypeElement typeElement = elements.getTypeElement(RouteConstants.INTERFACE_TYPE_REGISTER);
        ClassName className = ClassName.get(typeElement);
        if (RouteConstants.LOG){
            System.out.println("RouteImplProcessor--------ParameterSpec-------className---"+className);
        }
        //添加参数规格
        return ParameterSpec
                .builder(className, RouteConstants.INSTANCE_NAME_REGISTER)
                .build();
    }

}
