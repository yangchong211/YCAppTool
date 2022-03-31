package com.yc.time.loader.gradle.task;

import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.jetbrains.annotations.NotNull;

/**
 * 可以用 BuildListener 来监听整个构建是否完成，在构建完成后，输出所有执行过的 task 信息，以及每个 task 的执行时长
 */
public class AppBuildListener implements BuildListener {
    @Override
    public void buildStarted(@NotNull Gradle gradle) {

    }

    @Override
    public void settingsEvaluated(@NotNull Settings settings) {

    }

    @Override
    public void projectsLoaded(@NotNull Gradle gradle) {

    }

    @Override
    public void projectsEvaluated(@NotNull Gradle gradle) {

    }

    @Override
    public void buildFinished(@NotNull BuildResult buildResult) {

    }
}
