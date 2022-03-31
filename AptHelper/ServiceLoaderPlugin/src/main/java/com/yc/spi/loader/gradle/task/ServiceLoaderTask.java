package com.yc.spi.loader.gradle.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;


public class ServiceLoaderTask extends DefaultTask {

    private File sourceDir;

    private File servicesDir;

    private FileCollection classpath;

    public ServiceLoaderTask() {
        this.classpath = this.getProject().files();
    }

    @InputFiles
    public FileCollection getClasspath() {
        return this.classpath;
    }

    public void setClasspath(final FileCollection classpath) {
        this.classpath = classpath;
    }

    @OutputDirectory
    public File getSourceDir() {
        return this.sourceDir;
    }

    public void setSourceDir(final File sourceDir) {
        this.sourceDir = sourceDir;
    }

    @OutputDirectory
    public File getServicesDir() {
        return this.servicesDir;
    }

    public void setServicesDir(final File servicesDir) {
        this.servicesDir = servicesDir;
    }

    @TaskAction
    protected void generate() {
        this.setDidWork(new ServiceLoaderAction(this.classpath, this.servicesDir, this.sourceDir).execute());
    }
}
