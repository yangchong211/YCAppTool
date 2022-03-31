package com.yc.spi.loader.gradle;


import com.android.build.gradle.AppExtension;
import com.google.common.base.Strings;
import com.yc.spi.loader.gradle.task.ServiceLoaderTask;
import com.yc.spi.loader.gradle.task.ServiceLoaderUtils;

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
        String spiLoaderName = "com.github.yangchong211.YCAppTool:ServiceLoaderApi:1.4.2.4";
        project.getDependencies().add("implementation", spiLoaderName);
        project.afterEvaluate(p -> {
            if (!project.getPlugins().hasPlugin("com.android.application")) {
                return;
            }
            final AppExtension android = project.getExtensions().getByType(AppExtension.class);
            android.getApplicationVariants().forEach(variant -> {
                final File spiRoot = project.file(project.getBuildDir() + File.separator + "intermediates"
                        + File.separator + "spi" + File.separator + variant.getDirName() + File.separator);
                ServiceLoaderUtils.log("spi root file absolute path : " + spiRoot.getAbsolutePath());
                ServiceLoaderUtils.log("spi root file path : " + spiRoot.getPath());
                List<File> bootClasspath = android.getBootClasspath();
                FileCollection classpath = variant.getJavaCompile().getClasspath();
                ServiceLoaderUtils.log("spi file classpath : " + classpath.getAsPath());
                File destinationDir = variant.getJavaCompile().getDestinationDir();
                ServiceLoaderUtils.log("spi destination dir file path : " + destinationDir.getPath());
                final FileCollection spiClasspath = project.files(bootClasspath, classpath,destinationDir);

                TaskContainer tasks = project.getTasks();
                String capitalize = capitalize(variant.getName());
                ServiceLoaderUtils.log("spi capitalize name : " + capitalize);
                final ServiceLoaderTask generateTask = tasks.create("generateServiceRegistry" + capitalize, ServiceLoaderTask.class, task -> {
                    task.setDescription("Generate ServiceRegistry for " + capitalize(variant.getName()));
                    task.setClasspath(task.getClasspath().plus(spiClasspath));
                    task.setSourceDir(new File(spiRoot, "src"));
                    task.setServicesDir(new File(spiRoot, "services"));
                    task.getOutputs().upToDateWhen(it -> false);
                });

                final JavaCompile compileGeneratedTask = project.getTasks().create("compileGenerated" + capitalize(variant.getName()), JavaCompile.class, task -> {
                    task.setDescription("Compile ServiceRegistry for " + capitalize(variant.getName()));
                    task.setSource(new File(spiRoot, "src"));
                    task.include("**/*.java");
                    task.setClasspath(spiClasspath);
                    task.setDestinationDir(variant.getJavaCompile().getDestinationDir());
                    task.setSourceCompatibility("1.5");
                    task.setTargetCompatibility("1.5");
                });

                generateTask.mustRunAfter(variant.getJavaCompile());
                compileGeneratedTask.mustRunAfter(generateTask);
                variant.getAssemble().dependsOn(generateTask, compileGeneratedTask);
                variant.getInstall().dependsOn(generateTask, compileGeneratedTask);
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
