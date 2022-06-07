package com.yc.time.loader.gradle;


import com.yc.time.loader.gradle.task.AppBuildListener;
import com.yc.time.loader.gradle.task.AppTaskExecutionListener;
import com.yc.time.loader.gradle.task.BuildTimeCostExtension;
import com.yc.time.loader.gradle.task.TaskExecTimeInfo;
import org.gradle.BuildResult;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BuildTimeCostPlugin implements Plugin<Project> {

    //用来记录 task 的执行时长等信息
    Map<String, TaskExecTimeInfo> timeCostMap = new HashMap<>();
    //用来按顺序记录执行的 task 名称
    List<String> taskPathList = new ArrayList<>();

    @Override
    @SuppressWarnings("deprecation")
    public void apply(final Project project) {
        // 监听每个task的执行
        // 在每个 task 执行前先搜集其相关信息，记录该 task 执行的开始时间等，
        // 在 task 执行完成后，记录其执行结束时间，这样就能统计出该 task 的执行时长。
        project.getGradle().addListener(new AppTaskExecutionListener() {
            @Override
            public void beforeExecute(Task task) {
                //task开始执行之前搜集task的信息
                TaskExecTimeInfo timeInfo = new TaskExecTimeInfo();
                //记录开始时间
                timeInfo.setStart(System.currentTimeMillis());
                timeInfo.setPath(task.getPath());
                timeCostMap.put(task.getPath(), timeInfo);
                taskPathList.add(task.getPath());
            }

            @Override
            public void afterExecute(Task task,TaskState taskState) {
                //task执行完之后，记录结束时的时间
                TaskExecTimeInfo timeInfo = timeCostMap.get(task.getPath());
                //记录结束时间
                timeInfo.setEnd(System.currentTimeMillis());
                //计算该 task 的执行时长
                timeInfo.setTotal(timeInfo.getEnd() - timeInfo.getStart());
            }
        });

        //创建一个 Extension，配置输出结果
        final BuildTimeCostExtension timeCostExt = project.getExtensions().create(
                "taskExecTime", BuildTimeCostExtension.class);
        //编译结束之后：
        project.getGradle().addBuildListener(new AppBuildListener() {
            @Override
            public void buildFinished( BuildResult buildResult) {
                log("build finished, now println all task execution time:");
                //按 task 执行顺序打印出执行时长信息
                if (timeCostExt.isSorted()){
                    //进行排序
                    List<TaskExecTimeInfo> list = new ArrayList<>();
                    Set<String> keySet = timeCostMap.keySet();
                    Iterator<String> iterator = keySet.iterator();
                    while (iterator.hasNext()){
                        String key = iterator.next();
                        TaskExecTimeInfo taskExecTimeInfo = timeCostMap.get(key);
                        list.add(taskExecTimeInfo);
                    }
                    Collections.sort(list, new Comparator<TaskExecTimeInfo>() {
                        @Override
                        public int compare(TaskExecTimeInfo t1, TaskExecTimeInfo t2) {
                            return (int) (t2.getTotal() - t1.getTotal());
                        }
                    });
                    for (TaskExecTimeInfo timeInfo : list) {
                        long t = timeInfo.getTotal();
                        if (t >= timeCostExt.getThreshold()) {
                            log(timeInfo.getPath() + " " + t + "ms");
                        }
                    }
                } else {
                    for (String path : taskPathList) {
                        long t = timeCostMap.get(path).getTotal();
                        if (t >= timeCostExt.getThreshold()) {
                            log(path + " " + t + "ms");
                        }
                    }
                }
                log("build finished, time all print , yc");
            }
        });
    }

    public static void log(String string){
        //打印日志
        System.out.println("task time log : " + string);
    }

}
