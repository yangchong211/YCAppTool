package com.yc.spi.gradle;


import com.android.build.gradle.AppExtension;
import com.google.common.base.Strings;
import com.yc.spi.gradle.task.ServiceLoaderTask;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.compile.JavaCompile;
import java.io.File;


public class ServiceLoaderPlugin implements Plugin<Project> {

    @Override
    @SuppressWarnings("deprecation")
    public void apply(final Project project) {
        String spiLoaderName = "com.github.yangchong211.spi:spi-loader:1.0.0";
        project.getDependencies().add("implementation", spiLoaderName);
        project.afterEvaluate(p -> {
            if (!project.getPlugins().hasPlugin("com.android.application")) {
                return;
            }
            final AppExtension android = project.getExtensions().getByType(AppExtension.class);
            android.getApplicationVariants().forEach(variant -> {
                final File spiRoot = project.file(project.getBuildDir() + File.separator + "intermediates" + File.separator + "spi" + File.separator + variant.getDirName() + File.separator);
                final FileCollection spiClasspath = project.files(android.getBootClasspath(), variant.getJavaCompile().getClasspath(), variant.getJavaCompile().getDestinationDir());

                final ServiceLoaderTask generateTask = project.getTasks().create("generateServiceRegistry" + capitalize(variant.getName()), ServiceLoaderTask.class, task -> {
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
