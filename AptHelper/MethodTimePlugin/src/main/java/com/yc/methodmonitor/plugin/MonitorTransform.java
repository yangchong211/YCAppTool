package com.yc.methodmonitor.plugin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.yc.methodmonitor.plugin.asm.ASMMonitorProcessor;
import com.yc.methodmonitor.plugin.inter.IMonitorProcessor;

import org.gradle.api.Project;

import java.io.IOException;
import java.util.Set;

public class MonitorTransform extends Transform {

    private Project mProject;
    private String[] mPackagePrefixsWhiteList;
    private String[] mPackagePrefixsBlackList;

    public MonitorTransform(Project mProject, String[] mPackagePrefixsWhiteList, String[] mPackagePrefixsBlackList) {
        this.mProject = mProject;
        this.mPackagePrefixsWhiteList = mPackagePrefixsWhiteList;
        this.mPackagePrefixsBlackList = mPackagePrefixsBlackList;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        IMonitorProcessor processor = new ASMMonitorProcessor(mPackagePrefixsWhiteList, mPackagePrefixsBlackList);
        processor.process(mProject, transformInvocation);
    }
}
