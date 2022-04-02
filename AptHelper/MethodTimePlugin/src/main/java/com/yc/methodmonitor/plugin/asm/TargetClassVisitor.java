package com.yc.methodmonitor.plugin.asm;

import org.objectweb.asm.ClassVisitor;

public class TargetClassVisitor extends ClassVisitor {

    private String mClassName;

    public TargetClassVisitor() {
        super(ASMMonitorProcessor.ASM_VERSION);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClassName = name;
    }

    public boolean isMatchPackagePrefix(String[] packagePrefixWhiteList, String[] packagePrefixBlackList) {
        if (packagePrefixWhiteList == null || packagePrefixWhiteList.length == 0) {
            return false;
        }

        for (String pkgWhite : packagePrefixWhiteList) {
            if (mClassName.startsWith(pkgWhite)) {
                boolean isMatchBlack = false;
                for (String pkgBlack : packagePrefixBlackList) {
                    if (mClassName.startsWith(pkgBlack)) {
                        isMatchBlack = true;
                        break;
                    }
                }
                return !isMatchBlack;
            }
        }
        return false;
    }
}
