package com.yc.methodmonitor.plugin.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class MonitorClassVisitor extends ClassVisitor {

    private String mClassName;
    // 插桩相关的业务类不能插桩，否则会造成栈溢出
    private static String WHIATE_LIST = "com/yc/methodmonitor/business";

    public MonitorClassVisitor(ClassVisitor classVisitor) {
        super(ASMMonitorProcessor.ASM_VERSION, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClassName = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && !mClassName.startsWith(WHIATE_LIST)) {
            System.out.println(mClassName + "." + name + " 开始插桩");
            return new MonitorMethodVisitor(mv, mClassName, name, descriptor);
        }
        return mv;
    }

}
