package com.yc.methodmonitor.plugin.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class MonitorMethodVisitor extends MethodVisitor {

    // 不要修改
    private static final String OWNER = "com/yc/methodmonitor/business/MethodTimeManager";
    private static final String METHOD_GETINSTANCE = "getInstance";
    private static final String DESC_GETINSTANCE = "()Lcom/yc/methodmonitor/business/MethodTimeManager;";

    private static final String METHOD_START = "onMethodStart";
    private static final String DESC_START = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V";

    private static final String METHOD_END = "onMethodEnd";
    private static final String DESC_END = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V";

    private String mClassName;
    private String mMethodName;
    private String mMethodDesc;

    public MonitorMethodVisitor(MethodVisitor methodVisitor, String mClassName, String mMethodName, String mMethodDesc) {
        super(ASMMonitorProcessor.ASM_VERSION, methodVisitor);
        this.mClassName = mClassName;
        this.mMethodName = mMethodName;
        this.mMethodDesc = mMethodDesc;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        // 方法开始插入代码
        if (mv != null) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, OWNER, METHOD_GETINSTANCE, DESC_GETINSTANCE, false);
            mv.visitLdcInsn(mClassName);
            mv.visitLdcInsn(mMethodName);
            mv.visitLdcInsn(mMethodDesc);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, OWNER, METHOD_START, DESC_START, false);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        // 在 RETURN 之前插入代码
        if (((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) && mv != null) {
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, OWNER, METHOD_GETINSTANCE, DESC_GETINSTANCE, false);
            mv.visitLdcInsn(mClassName);
            mv.visitLdcInsn(mMethodName);
            mv.visitLdcInsn(mMethodDesc);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, OWNER, METHOD_END, DESC_END, false);
            super.visitInsn(opcode);
            mv.visitMaxs(4, 2);
            return;
        }
        super.visitInsn(opcode);
    }
}
