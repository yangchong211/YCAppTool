package com.yc.methodmonitor.plugin.asm;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.yc.methodmonitor.plugin.utils.CompressUtils;
import com.yc.methodmonitor.plugin.inter.IMonitorProcessor;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ASMMonitorProcessor implements IMonitorProcessor {

    public static final int ASM_VERSION = Opcodes.ASM7;

    private final String[] mPackagePrefixsWhiteList;
    private final String[] mPackagePrefixsBlackList;
    private final List<URL> mUrlPaths = new ArrayList<>();

    public ASMMonitorProcessor(String[] mPackagePrefixsWhiteList, String[] mPackagePrefixsBlackList) {
        this.mPackagePrefixsWhiteList = mPackagePrefixsWhiteList;
        this.mPackagePrefixsBlackList = mPackagePrefixsBlackList;
    }

    @Override
    public void process(Project project, TransformInvocation transformInvocation) {
        insertJarAndDirectoryClasspath(transformInvocation);
        ClassLoader classLoader = getCustomClassLoader();
        hookJarAndDirectory(transformInvocation, classLoader);
    }

    /**
     * 将项目中依赖的jar和自身编写的class（存放在directory）插入到依赖中
     */
    private void insertJarAndDirectoryClasspath(TransformInvocation transformInvocation) {
        for (TransformInput input : transformInvocation.getInputs()) {
            if (input.getJarInputs() != null) {
                for (JarInput jarInput : input.getJarInputs()) {
                    File jarFile = jarInput.getFile();
                    try {
                        mUrlPaths.add(jarFile.toURI().toURL());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (input.getDirectoryInputs() != null) {
                for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                    File directoryFile = directoryInput.getFile();
                    try {
                        mUrlPaths.add(directoryFile.toURI().toURL());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ClassLoader getCustomClassLoader() {
        URL urls[] = new URL[mUrlPaths.size()];
        urls = mUrlPaths.toArray(urls);
        // 返回新建的ClassLoader用于判断ClassWriter
        return new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
    }

    private void hookJarAndDirectory(TransformInvocation transformInvocation, ClassLoader classLoader) {
        for (TransformInput input : transformInvocation.getInputs()) {
            if (input.getJarInputs() != null) {
                for (JarInput jarInput : input.getJarInputs()) {
                    // 输入文件
                    File srcFile = jarInput.getFile();
                    String srcPath = srcFile.getAbsolutePath();
                    // 输出文件
                    File destFile = transformInvocation.getOutputProvider().getContentLocation(
                            jarInput.getName(), jarInput.getContentTypes(),
                            jarInput.getScopes(), Format.JAR);
                    //判断是否是指定的包名
                    if (!isInPackages(srcFile)) {
                        try {
                            FileUtils.copyFile(srcFile, destFile);
                        } catch (Throwable e2) {
                        }
                        continue;
                    }
                    try {
                        long timeMillis = System.currentTimeMillis();
                        String deCompressPath = srcFile.getParent() + File.separator + timeMillis
                                + File.separator + "decompress" + File.separator;
                        // 先解压
                        CompressUtils.deCompress(srcPath, deCompressPath);
                        // 处理class文件
                        hook(deCompressPath, classLoader);
                        // 再压缩到目标文件
                        CompressUtils.compress(deCompressPath, destFile.getAbsolutePath());
                    } catch (Throwable e) {
                        e.printStackTrace();
                        // 出现异常，直接将原目录拷贝过去
                        try {
                            FileUtils.copyFile(srcFile, destFile);
                        } catch (Throwable e2) {
                        }
                    }
                }
            }
            if (input.getDirectoryInputs() != null) {
                for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                    // 输入文件
                    File srcFile = directoryInput.getFile();
                    String srcPath = srcFile.getAbsolutePath();
                    // 输出文件
                    File destFile = transformInvocation.getOutputProvider().getContentLocation(
                            directoryInput.getName(), directoryInput.getContentTypes(),
                            directoryInput.getScopes(), Format.DIRECTORY);
                    try {
                        // 处理class文件
                        hook(srcPath, classLoader);
                        // 拷贝文件
                        FileUtils.copyDirectory(srcFile, destFile);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        // 出现异常，直接将原目录拷贝过去
                        try {
                            FileUtils.copyDirectory(srcFile, destFile);
                        } catch (Throwable e2) {
                        }
                    }
                }
            }
        }
    }

    private boolean isInPackages(File file) {
        if (mPackagePrefixsWhiteList == null || mPackagePrefixsWhiteList.length == 0) {
            return false;
        }
        try {
            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                //读取文件后缀名为.java的文件
                if (!entry.isDirectory() && entryName.endsWith(".class")) {
                    for (String pkgWhite : mPackagePrefixsWhiteList) {
                        if (entryName.startsWith(pkgWhite)) {
                            boolean isMatchBlack = false;
                            for (String pkgBlack : mPackagePrefixsBlackList) {
                                if (entryName.startsWith(pkgBlack)) {
                                    isMatchBlack = true;
                                    break;
                                }
                            }
                            return !isMatchBlack;
                        }
                    }
                }
            }
        } catch (Throwable e) {
        }
        return false;
    }

    private void hook(String path, ClassLoader classLoader) {
        File dir = new File(path);
        Collection<File> files = FileUtils.listFiles(dir, new String[]{"class"}, true);
        for (File file : files) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                ClassReader reader = new ClassReader(in);
                TargetClassVisitor visitor = new TargetClassVisitor();
                reader.accept(visitor, ClassReader.SKIP_DEBUG);
                // hook onclick方法
                hookClickListener(visitor, file, classLoader);
            } catch (Throwable e) {
                e.printStackTrace();
                System.out.println("hook 发生了未知异常，跳过当前文件：" + file.getAbsolutePath());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void hookClickListener(TargetClassVisitor targetClassVisitor, File file, ClassLoader classLoader) {
        if (targetClassVisitor.isMatchPackagePrefix(mPackagePrefixsWhiteList, mPackagePrefixsBlackList)) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                ClassReader reader = new ClassReader(in);
                TransformClassWriter writer = new TransformClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS, classLoader);
                MonitorClassVisitor visitor = new MonitorClassVisitor(writer);
                reader.accept(visitor, ClassReader.SKIP_DEBUG);
                FileUtils.writeByteArrayToFile(file, writer.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
