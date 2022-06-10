

package com.yc.threaddebug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ThreadCategory implements Cloneable {
    private String mStartWidthKey;
    private String mAlias;
    private List<ThreadInfo> mInfoList;
    private final static Map<String, String> THREAD_NAME_LOW_CACHE = new HashMap<>();

    boolean is(String threadName) {
        if (threadName == null) {
            return false;
        }

        final int diff = threadName.length() - mStartWidthKey.length();
        if (diff < 0) return false;

        String lowCaseThreadName = THREAD_NAME_LOW_CACHE.get(threadName);
        if (lowCaseThreadName == null) {
            lowCaseThreadName = threadName.toLowerCase();
            THREAD_NAME_LOW_CACHE.put(threadName, lowCaseThreadName);
        }

        return lowCaseThreadName.startsWith(mStartWidthKey);
    }

    int size() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mAlias);
        if (mInfoList == null || mInfoList.isEmpty()) {
            builder.append("[]");
        } else {
            builder.append('[');
            Iterator<ThreadInfo> it = mInfoList.iterator();
            while (it.hasNext()) {
                ThreadInfo next = it.next();
                builder.append(next.threadName);
                if (it.hasNext()) {
                    builder.append(", ");
                }
            }
            builder.append(']');
        }
        return builder.toString();
    }

    public StringBuilder appendAlias(StringBuilder builder) {
        return builder.append(mAlias).append(": ");
    }

    private final static List<String> EMPTY = new ArrayList<>();
    private final static List<ThreadInfo> EMPTY_INFO = new ArrayList<>();

    public List<String> diff(ThreadCategory category) {
        if (mInfoList == null && (category == null || category.mInfoList == null)) {
            return EMPTY;
        }


        final List<String> diffNameList = new ArrayList<>();

        final List<ThreadInfo> curNameList = (List<ThreadInfo>) ((ArrayList) mInfoList).clone();
        final List<ThreadInfo> preNameList = (category == null || category.mInfoList == null) ?
                EMPTY_INFO : (List<ThreadInfo>) ((ArrayList) category.mInfoList).clone();
        for (ThreadInfo info : curNameList) {
            if (!preNameList.contains(info)) {
                diffNameList.add("(+)" + info.threadName);
            }
        }

        for (ThreadInfo info : preNameList) {
            if (!curNameList.contains(info)) {
                diffNameList.add("(-)" + info.threadName);
            }
        }

        return diffNameList;
    }

    public boolean isDiff(ThreadCategory category) {
        if (category == null) {
            return true;
        }

        final int diffSize = size() - category.size();
        return diffSize != 0 || diff(category).size() > 0;

    }


    public static class Builder {
        private final ThreadCategory mCategory = new ThreadCategory();

        public Builder(String key) {
            this(key, key);
        }

        public Builder(String startWithKey, String alias) {
            mCategory.mAlias = alias;
            mCategory.mStartWidthKey = startWithKey.toLowerCase();
        }

        /**
         * @return Whether digested.
         */
        public boolean process(final int hashCode, final String threadName) {
            if (mCategory.is(threadName)) {
                add(hashCode, threadName);
                return true;
            }

            return false;
        }

        public void add(int hashCode, String threadName) {
            if (mCategory.mInfoList == null) {
                mCategory.mInfoList = new ArrayList<>();
            }

            mCategory.mInfoList.add(new ThreadInfo(hashCode, threadName));
        }

        public void reset() {
            if (mCategory.mInfoList != null) {
                mCategory.mInfoList.clear();
            }
        }

        public ThreadCategory build() {
            return mCategory;
        }
    }

    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    @Override
    protected ThreadCategory clone() {
        ThreadCategory clone;
        try {
            clone = (ThreadCategory) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            clone = new ThreadCategory();
        }

        //noinspection unchecked
        clone.mInfoList = mInfoList == null ? null :
                (List<ThreadInfo>) ((ArrayList) mInfoList).clone();

        return clone;
    }

    private static class ThreadInfo {
        private final int hashCode;
        private final String threadName;

        ThreadInfo(int hashCode, String threadName) {
            this.hashCode = hashCode;
            this.threadName = threadName;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ThreadInfo)) {
                return false;
            }

            final ThreadInfo another = (ThreadInfo) o;
            return another.hashCode == hashCode && threadName.equals(another.threadName);
        }
    }
}
