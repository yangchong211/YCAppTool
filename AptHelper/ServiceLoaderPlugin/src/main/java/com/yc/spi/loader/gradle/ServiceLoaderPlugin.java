package com.yc.spi.loader.gradle;


import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.google.common.base.Strings;
import com.yc.spi.loader.gradle.task.ServiceLoaderTask;
import com.yc.spi.loader.gradle.task.ServiceLoaderUtils;

import org.gradle.api.DomainObjectSet;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.compile.JavaCompile;
import java.io.File;
import java.util.List;


public class ServiceLoaderPlugin implements Plugin<Project> {

    @Override
    @SuppressWarnings("deprecation")
    public void apply(final Project project) {
        String spiLoaderName = "com.github.yangchong211.YCAppTool:ServiceLoaderApi:1.4.4";
        project.getDependencies().add("implementation", spiLoaderName);
        project.afterEvaluate(p -> {
            //判断是否有 'com.android.application' 依赖的插件
            if (!project.getPlugins().hasPlugin("com.android.application")) {
                return;
            }
            /*
             * 什么是AppExtension？直接看下面注释例子
             * apply plugin: 'com.android.application'
             * android { // 这个就是AppExtension
             * }
             */
            final AppExtension android = project.getExtensions().getByType(AppExtension.class);
            // 里面的compileSdkVersion、defaultConfig、buildTypes都是AppExtension类的方法。
            // applicationVariants也是他的方法，原方法名是getApplicationVariants
            DomainObjectSet<ApplicationVariant> applicationVariants = android.getApplicationVariants();
            applicationVariants.forEach(variant -> {
                final File spiRoot = project.file(project.getBuildDir() + File.separator + "intermediates"
                        + File.separator + "spi" + File.separator + variant.getDirName() + File.separator);
                ///Users/didi/yc/github/LifeHelper/app/build/intermediates/spi/debug
                ServiceLoaderUtils.log("spi root file absolute path : " + spiRoot.getAbsolutePath());
                ///Users/didi/yc/github/LifeHelper/app/build/intermediates/spi/debug
                ServiceLoaderUtils.log("spi root file path : " + spiRoot.getPath());
                List<File> bootClasspath = android.getBootClasspath();
                FileCollection classpath = variant.getJavaCompile().getClasspath();
                //ServiceLoaderUtils.log("spi file classpath : " + classpath.getAsPath());
                File destinationDir = variant.getJavaCompile().getDestinationDir();
                ///Users/didi/yc/github/LifeHelper/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes
                ServiceLoaderUtils.log("spi destination dir file path : " + destinationDir.getPath());
                final FileCollection spiClasspath = project.files(bootClasspath, classpath,destinationDir);
                //ServiceLoaderUtils.log("spi file collection path : " + spiClasspath.getAsPath());
                String capitalize = capitalize(variant.getName());
                //Debug
                ServiceLoaderUtils.log("spi capitalize name : " + capitalize);
                final ServiceLoaderTask generateTask = project.getTasks().create("generateServiceRegistry"
                        + capitalize, ServiceLoaderTask.class, task -> {
                    task.setDescription("Generate ServiceRegistry for " + capitalize(variant.getName()));
                    task.setClasspath(task.getClasspath().plus(spiClasspath));
                    task.setSourceDir(new File(spiRoot, "src"));
                    task.setServicesDir(new File(spiRoot, "services"));
                    task.getOutputs().upToDateWhen(it -> false);
                    ServiceLoaderUtils.log("spi task generateServiceRegistry finish");
                });

                final JavaCompile compileGeneratedTask = project.getTasks().create("compileGenerated"
                        + capitalize, JavaCompile.class, task -> {
                    task.setDescription("Compile ServiceRegistry for " + capitalize(variant.getName()));
                    task.setSource(new File(spiRoot, "src"));
                    task.include("**/*.java");
                    task.setClasspath(spiClasspath);
                    task.setDestinationDir(variant.getJavaCompile().getDestinationDir());
                    task.setSourceCompatibility("1.6");
                    task.setTargetCompatibility("1.6");
                    ServiceLoaderUtils.log("spi task compileGenerated finish");
                });
                ServiceLoaderUtils.log("spi task generateTask : " + generateTask);
                ServiceLoaderUtils.log("spi task compileGeneratedTask : " + compileGeneratedTask);
                JavaCompile javaCompile = variant.getJavaCompile();
                generateTask.mustRunAfter(javaCompile);
                compileGeneratedTask.mustRunAfter(generateTask);
                //返回此变量所有输出的组装任务
                variant.getAssemble().dependsOn(generateTask, compileGeneratedTask);
                //返回该变量的安装任务
                //variant.getInstall().dependsOn(generateTask, compileGeneratedTask);
            });
        });
    }


    public static String capitalize(String string) {
        if (Strings.isNullOrEmpty(string)) {
            return string;
        }
        char ch = string.charAt(0);
        if (Character.isTitleCase(ch)) {
            return string;
        }
        return Character.toTitleCase(ch) + string.substring(1);
    }

}
