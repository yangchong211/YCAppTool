package com.yc.methodmonitor.plugin.inter;

import com.android.build.api.transform.TransformInvocation;

import org.gradle.api.Project;

public interface IMonitorProcessor {

    void process(Project project, TransformInvocation transformInvocation);

}
