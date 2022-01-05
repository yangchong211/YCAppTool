package com.yc.spi.gradle

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile


class ServiceProviderInterfacePlugin implements Plugin<Project> {

    @Override
    void apply(final Project project) {
        project.afterEvaluate {
            try {
                if (!project.plugins.hasPlugin(Class.forName('com.android.build.gradle.AppPlugin'))) {
                    return;
                }
            } catch (final ClassNotFoundException e) {
                throw new GradleException("Android gradle plugin is required", e)
            }

            project.android.applicationVariants.all { variant ->
                def spiSourceDir = project.file("${project.buildDir}/intermediates/spi/${variant.dirName}/src")
                def spiServicesDir = project.file("${project.buildDir}/intermediates/spi/${variant.dirName}/services")
                def spiClasspath = project.files(project.android.bootClasspath, variant.javaCompile.classpath, variant.javaCompile.destinationDir)

                def generateTask = project.task(
                        "generateServiceRegistry${variant.name.capitalize()}",
                        type: ServiceRegistryGenerationTask) {
                    description = "Generate ServiceRegistry for ${variant.name.capitalize()}"
                    classpath += spiClasspath
                    sourceDir = spiSourceDir
                    servicesDir = spiServicesDir
                    outputs.upToDateWhen { false }
                }

                def compileGeneratedTask = project.task("compileGenerated${variant.name.capitalize()}", type: JavaCompile) {
                    description = "Compile ServiceRegistry for ${variant.name.capitalize()}"
                    source = spiSourceDir
                    include '**/*.java'
                    classpath = spiClasspath
                    destinationDir = variant.javaCompile.destinationDir
                    sourceCompatibility = '1.8'
                    targetCompatibility = '1.8'
                }

                generateTask.mustRunAfter(variant.javaCompile)
                compileGeneratedTask.mustRunAfter(generateTask)
                variant.assemble.dependsOn(generateTask, compileGeneratedTask)
            }
        }
    }

}
