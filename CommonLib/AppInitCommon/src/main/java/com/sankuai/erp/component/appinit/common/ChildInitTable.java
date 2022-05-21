package com.sankuai.erp.component.appinit.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 作者:王浩
 * 创建时间:2018/11/12
 * 描述:
 */
public class ChildInitTable extends ArrayList<AppInitItem> implements Comparable<ChildInitTable> {
    public int priority;
    public String coordinate;
    public Set<String> dependencies;
    public boolean calculated = false; // 是否已经计算过优先级
    public long time;

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public void setDependencies(String dependencies) {
        if (AppInitCommonUtils.isEmpty(dependencies)) {
            this.dependencies = new HashSet<>();
        } else {
            this.dependencies = new HashSet<>(Arrays.asList(dependencies.split(",")));
        }
    }

    void setDependenciesSet(Set<String> dependencies) {
        if (dependencies == null || dependencies.isEmpty()) {
            this.dependencies = new HashSet<>();
        } else {
            this.dependencies = dependencies;
        }
    }

    public String getModuleInfo() {
        String dep = getDep();
        if (AppInitCommonUtils.isEmpty(dep)) {
            return String.format("    《%s》[priority=%s]", coordinate, priority);
        } else {
            return String.format("    《%s》[priority=%s][dependencies=%s]", coordinate, priority, dep);
        }
    }

    private String getDep() {
        if (dependencies != null && !dependencies.isEmpty()) {
            return dependencies.toString().replace("[", "").replace("]", "");
        }
        return null;
    }

    public String getTimeInfo() {
        if (time == 0) {
            for (AppInitItem appInitItem : this) {
                time += appInitItem.time;
            }
        }

        String dep = getDep();
        if (AppInitCommonUtils.isEmpty(dep)) {
            return String.format("    《%s》[priority=%s][耗时=%sms]", coordinate, priority, time);
        } else {
            return String.format("    《%s》[priority=%s][dependencies=%s][耗时=%sms]", coordinate, priority, dep, time);
        }
    }

    @Override
    public int compareTo(ChildInitTable other) {
        return Integer.compare(this.priority, other.priority);
    }
}
