package com.yc.memoryleakupload;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.Exclusion;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.LeakTrace;
import com.squareup.leakcanary.LeakTraceElement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class UploadLeakService extends DisplayLeakService {

    public static final String TAG = "UploadLeakService";

    @Override
    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
        super.afterDefaultHandling(heapDump, result, leakInfo);
        String name = result.className;
        long size = result.retainedHeapSize;
        if (0 == size) {
            return;
        }
        String leakSize = getLeakSize(size);
        LeakCanaryUtils.debug(TAG, "name:" + name);
        LeakCanaryUtils.debug(TAG, "leakSize:" + leakSize);

        LeakTrace leakTrace = result.leakTrace;
        List<Entity> entityList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (null != leakTrace && null != leakTrace.elements) {
            for (int i = 0; i < leakTrace.elements.size(); i++) {
                LeakTraceElement leakTraceElement = leakTrace.elements.get(i);
                stringBuilder.append(leakTraceElement.className);
                String elementText = elementToHtmlString(leakTraceElement);
                Entity entity = new Entity();
                entity.setHtmlText(elementText);
                entityList.add(entity);
            }
        }

        Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();
        String leakPath = gson.toJson(entityList);
        try {
            String text = URLEncoder.encode(leakPath, "utf-8");
            LeakCanaryUtils.debug(TAG, text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        LeakCanaryUtils.debug(TAG, leakPath);

        int key = stringBuilder.toString().hashCode();

        upload("" + key, name, leakSize, leakPath);
        //检查是否结束，结束leakcanary进程
        if (!ReleaseRefHolder.getInstance().isDebug()) {
            LeakCanaryUtils.debug(TAG, "remove " + heapDump.referenceName);
            ReleaseRefHolder.getInstance().removeReference(heapDump.referenceName);
        }
        if (!ReleaseRefHolder.getInstance().isDebug() && !ReleaseRefHolder.getInstance().hasReference()) {
            //结束leakcanary进程
            LeakCanaryUtils.debug(TAG, "kill process");
            SelfKillService.kill(this);
            ReleaseRefHolder.getInstance().release();
        }
    }

    /**
     * 格式化内存泄漏大小
     * @param size 字节数
     * @return
     */
    private String getLeakSize(long size){
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return size + "K";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return (size / 100) + "." + (size % 100) + "M";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return (size / 100) + "."  + (size % 100) + "G";
        }
    }


    /**
     * 上传埋点
     *
     * @param key 唯一key
     * @param name 泄漏组件名字
     * @param leakSize 泄漏大小
     * @param leakPath 泄漏路径
     */
    private void upload(String key, String name, String leakSize, String leakPath) {
        try {
            //todo 上报数据埋点
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * html化element
     */
    private String elementToHtmlString(LeakTraceElement element) {
        String htmlString = "";

        if (element.referenceName == null) {
            htmlString += "leaks ";
        } else {
            htmlString += "references ";
        }

        if (element.type == com.squareup.leakcanary.LeakTraceElement.Type.STATIC_FIELD) {
            htmlString += "<font color='#c48a47'>static</font> ";
        }

        if (element.holder == com.squareup.leakcanary.LeakTraceElement.Holder.ARRAY
            || element.holder == com.squareup.leakcanary.LeakTraceElement.Holder.THREAD) {
            htmlString += "<font color='#139ff3'>" + element.holder.name().toLowerCase() + "</font> ";
        }

        int separator = element.className.lastIndexOf('.');
        String simpleName;
        if (separator == -1) {
            simpleName = element.className;
        } else {
            simpleName = element.className.substring(separator + 1);
        }

        String styledClassName = "<font color='#000000'>" + simpleName + "</font>";

        htmlString += styledClassName;

        if (element.referenceName != null) {
            htmlString += ".<font color='#198bb5'>" + element.referenceName.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;") + "</font>";
        } else {
            htmlString += " <font color='#f3cf83'>instance</font>";
        }

        if (element.extra != null) {
            htmlString += " <font color='#515151'>" + element.extra + "</font>";
        }

        Exclusion exclusion = element.exclusion;
        if (exclusion != null) {
            htmlString += " (excluded)";
        }

        return htmlString;
    }

    @SuppressLint("MemberName")
    public static class Entity {

        private String htmlText;

        public String getHtmlText() {
            return htmlText;
        }

        public void setHtmlText(String htmlText) {
            this.htmlText = htmlText;
        }
    }

}
