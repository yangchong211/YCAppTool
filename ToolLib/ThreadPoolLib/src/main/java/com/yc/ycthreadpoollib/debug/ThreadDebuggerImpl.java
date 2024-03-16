
package com.yc.ycthreadpoollib.debug;

import java.util.ArrayList;
import java.util.List;


class ThreadDebuggerImpl implements IThreadDebugger {

    private boolean mAlwaysPrintUnknown = true;

    private int mPreviousSize = 0;
    private int mSize = 0;

    private List<ThreadCategory> mCurThreadCategoryList;
    private List<ThreadCategory> mPreThreadCategoryList;


    private ThreadCategory mCurUnknowCategory;
    private ThreadCategory mPreUnknowCategory;

    private final Builder mBuilder = new Builder();

    @SuppressWarnings("unchecked")
    private static class Builder {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<ThreadCategory.Builder> mTCBuilderList = new ArrayList<>();

        private final ThreadCategory.Builder mUnknowCategoryBuilder =
                new ThreadCategory.Builder("unknown");

        void process(final int hashCode, final String threadName) {
            final List<ThreadCategory.Builder> builderList = (List<ThreadCategory.Builder>)
                    ((ArrayList) mTCBuilderList).clone();
            for (ThreadCategory.Builder builder : builderList) {
                if (builder.process(hashCode, threadName)) {
                    return;
                }
            }

            mUnknowCategoryBuilder.add(hashCode, threadName);
        }

        void add(String key) {
            add(key, key);
        }

        void add(String startWithKey, String alias) {
            mTCBuilderList.add(new ThreadCategory.Builder(startWithKey, alias));
        }

        void reset() {
            final List<ThreadCategory.Builder> builderList = (List<ThreadCategory.Builder>)
                    ((ArrayList) mTCBuilderList).clone();
            for (ThreadCategory.Builder builder : builderList) {
                builder.reset();
            }

            mUnknowCategoryBuilder.reset();
        }

        List<ThreadCategory> cloneList() {
            final List<ThreadCategory.Builder> builderList = (List<ThreadCategory.Builder>)
                    ((ArrayList) mTCBuilderList).clone();

            final List<ThreadCategory> categoryList = new ArrayList<>(builderList.size());
            for (ThreadCategory.Builder builder : builderList) {
                categoryList.add(builder.build().clone());
            }

            return categoryList;
        }

        ThreadCategory cloneUnknowCategory() {
            return mUnknowCategoryBuilder.build().clone();
        }
    }

    @Override
    public IThreadDebugger add(String key) {
        mBuilder.add(key);
        return this;
    }

    @Override
    public IThreadDebugger add(String startWithKey, String alias) {
        mBuilder.add(startWithKey, alias);
        return this;
    }

    @Override
    public IThreadDebugger ignoreUnknownCategory() {
        mAlwaysPrintUnknown = false;
        return this;
    }

    @Override
    public void refresh() {
        final Thread[] threads = ThreadToolUtils.getAllThreads();

        mBuilder.reset();

        int size = 0;
        for (Thread thread : threads) {
            if (thread != null) {
                size++;
                mBuilder.process(thread.hashCode(), thread.getName());
            }
        }

        mPreThreadCategoryList = mCurThreadCategoryList;
        mCurThreadCategoryList = mBuilder.cloneList();
        mPreUnknowCategory = mCurUnknowCategory;
        mCurUnknowCategory = mBuilder.cloneUnknowCategory();
        mPreviousSize = mSize;
        mSize = size;
    }

    @Override
    public String drawUpEachThreadSize() {
        if (mCurThreadCategoryList == null) {
            return NO_DATA;
        }

        final StringBuilder builder = createBasicInfoStringBuilder("drawUpEachThreadSize");

        appendThreadCount(mSize, builder);

        final List<ThreadCategory> threadCategoryList = (List<ThreadCategory>)
                ((ArrayList) mCurThreadCategoryList).clone();
        final ThreadCategory unknowCategory = mCurUnknowCategory.clone();

        boolean hasSplit = false;
        for (ThreadCategory threadCategory : threadCategoryList) {
            if (threadCategory.size() > 0) {
                threadCategory.appendAlias(builder).append(threadCategory.size());
                hasSplit = true;
                appendSplit(builder);
            }
        }

        if (mAlwaysPrintUnknown && unknowCategory.size() > 0) {
            unknowCategory.appendAlias(builder).append(unknowCategory.size());
        } else if (hasSplit) {
            deleteLastSplit(builder);
        }

        return builder.toString();
    }

    @Override
    public String drawUpEachThreadInfo() {
        if (mCurThreadCategoryList == null) {
            return NO_DATA;
        }

        final StringBuilder builder = createBasicInfoStringBuilder("drawUpEachThreadInfo");

        appendThreadCount(mSize, builder);

        final List<ThreadCategory> threadCategoryList = (List<ThreadCategory>)
                ((ArrayList) mCurThreadCategoryList).clone();
        final ThreadCategory unknowCategory = mCurUnknowCategory.clone();

        boolean hasSplit = false;
        for (ThreadCategory threadCategory : threadCategoryList) {
            if (threadCategory.size() > 0) {
                hasSplit = true;
                builder.append(threadCategory);
                appendSplit(builder);
            }
        }


        if (mAlwaysPrintUnknown && unknowCategory.size() > 0) {
            builder.append(unknowCategory);
        } else if (hasSplit) {
            deleteLastSplit(builder);
        }

        return builder.toString();
    }

    @Override
    public String drawUpEachThreadSizeDiff() {
        return drawUpEachThreadDiff(false);

    }

    @Override
    public String drawUpEachThreadInfoDiff() {
        return drawUpEachThreadDiff(true);
    }

    @Override
    public String drawUpUnknownInfo() {
        if (mCurUnknowCategory == null) {
            return NO_DATA;
        }

        final StringBuilder builder = createBasicInfoStringBuilder("drawUpUnknownInfo");

        builder.append("Unknow thread count = ")
                .append(mCurUnknowCategory.size()).append(". ");
        final int diff = mPreUnknowCategory == null ?
                mCurUnknowCategory.size() : mCurUnknowCategory.size() - mPreUnknowCategory.size();
        builder.append("Unknow thread differ = ");
        if (diff > 0) {
            builder.append("+");
        }

        builder.append(diff).append(". ");

        return builder.append(mCurUnknowCategory.toString()).toString();
    }

    @Override
    public boolean isSizeChanged() {
        if (mAlwaysPrintUnknown) {
            return mSize != mPreviousSize;
        } else {
            return mCurThreadCategoryList != null && mPreThreadCategoryList != null
                    && mCurThreadCategoryList.size() != mPreThreadCategoryList.size();
        }
    }

    @Override
    public boolean isChanged() {
        if (mCurThreadCategoryList == null) {
            return false;
        }
        if (isSizeChanged()) {
            return true;
        }

        if (mAlwaysPrintUnknown && mCurUnknowCategory != null) {
            if (mCurUnknowCategory.isDiff(mPreUnknowCategory)) {
                return true;
            }
        }
        final List<ThreadCategory> preThreadCategoryList = mPreThreadCategoryList == null ?
                null : (List<ThreadCategory>) ((ArrayList) mPreThreadCategoryList).clone();
        final List<ThreadCategory> curThreadCategoryList = (List<ThreadCategory>)
                ((ArrayList) mCurThreadCategoryList).clone();


        final int length = curThreadCategoryList.size();
        for (int i = 0; i < length; i++) {
            final ThreadCategory curThreadCategory = curThreadCategoryList.get(i);
            final ThreadCategory preThreadCategory = preThreadCategoryList == null ?
                    null : preThreadCategoryList.get(i);
            if (curThreadCategory.isDiff(preThreadCategory)) {
                return true;
            }
        }

        return false;
    }

    private String drawUpEachThreadDiff(boolean showDetail) {
        if (mCurThreadCategoryList == null) {
            return NO_DATA;
        }

        final StringBuilder builder = createBasicInfoStringBuilder("drawUpEachThread" +
                (showDetail ? "Info" : "Size") + "Diff");

        appendThreadCount(mSize, builder);

        if (!showDetail && mPreviousSize == mSize) {
            return builder.append("Thread size has not changed.").toString();
        }

        final List<ThreadCategory> preThreadCategoryList = mPreThreadCategoryList == null ?
                null : (List<ThreadCategory>) ((ArrayList) mPreThreadCategoryList).clone();
        final List<ThreadCategory> curThreadCategoryList = (List<ThreadCategory>)
                ((ArrayList) mCurThreadCategoryList).clone();

        builder.append("Thread differ : ");
        final int diff = mSize - mPreviousSize;
        if (diff > 0) {
            builder.append("+");
        }
        builder.append(diff).append(". ");

        boolean hasSplit = false;
        final int length = curThreadCategoryList.size();
        for (int i = 0; i < length; i++) {
            final ThreadCategory curThreadCategory = curThreadCategoryList.get(i);
            final ThreadCategory preThreadCategory = preThreadCategoryList == null ?
                    null : preThreadCategoryList.get(i);
            if (appendDiff(builder, preThreadCategory, curThreadCategory, showDetail)) {
                hasSplit = true;
                appendSplit(builder);
            }
        }

        if (mAlwaysPrintUnknown) {
            final ThreadCategory preUnknowCategory = mPreUnknowCategory == null ?
                    null : mPreUnknowCategory.clone();
            final ThreadCategory curUnknowCategory = mCurUnknowCategory == null ?
                    null : mCurUnknowCategory.clone();

            if ((curUnknowCategory == null ||
                    !appendDiff(builder, preUnknowCategory, curUnknowCategory, true)) && hasSplit) {
                deleteLastSplit(builder);
            }
        } else if (hasSplit) {
            deleteLastSplit(builder);
        }


        return builder.toString();
    }

    private boolean appendDiff(StringBuilder builder, ThreadCategory preThreadCategory,
                               ThreadCategory curThreadCategory, boolean showDetail) {
        final int diff = preThreadCategory == null ? curThreadCategory.size() :
                curThreadCategory.size() - preThreadCategory.size();

        if (showDetail) {
            final List<String> diffNameList = curThreadCategory.diff(preThreadCategory);
            if (diffNameList == null || diffNameList.size() == 0) {
                return false;
            }

            curThreadCategory.appendAlias(builder);
            if (diff > 0) {
                builder.append("+");
            }

            if (diff == 0) {
                builder.append("SWAP");
            } else {
                builder.append(diff);
            }

            builder.append(" ").append(diffNameList);

            return true;
        } else if (diff == 0) {
            return false;
        } else {
            curThreadCategory.appendAlias(builder);
            if (diff > 0) {
                builder.append("+");
            }

            builder.append(diff);
            return true;
        }
    }

    private static StringBuilder appendThreadCount(final int count, StringBuilder builder) {
        return builder.append("Thread count = ")
                .append(count)
                .append(". ");
    }

    private static StringBuilder createBasicInfoStringBuilder(String methodName) {
        StringBuilder builder = new StringBuilder();
        builder.append(methodName)
                .append(": ");

        return builder;
    }

    private final static String NO_DATA = "NO data";
    private final static String CATEGORY_SPLIT = " | ";

    private static void deleteLastSplit(StringBuilder builder) {
        builder.delete(builder.length() - CATEGORY_SPLIT.length(), builder.length());
    }

    private static void appendSplit(StringBuilder builder) {
        builder.append(CATEGORY_SPLIT);
    }

}
