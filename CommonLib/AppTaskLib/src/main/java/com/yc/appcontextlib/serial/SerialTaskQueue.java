package com.yc.appcontextlib.serial;


import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2017/11/30
 *    desc   : 顺序task任务管理器
 */
public final class SerialTaskQueue {
    
    private final ArrayDeque<AbsWork> mWorks = new ArrayDeque<>();
    private AbsWork mActive;

    public SerialTaskQueue() {

    }

    @MainThread
    @NonNull
    public ICancelable append(AbsTask task, AppendMode mode) {
        if (mode != AppendMode.Normal) {
            if (mode == AppendMode.ReplaceStrict) {
                if (mActive != null && mActive.getCategory().equals(task.getCategory())) {
                    mActive.cancel = true;
                    if (!mActive.finished) {
                        mActive.onCancel();
                    }
                }
            } else if (mode == AppendMode.DiscardStrict && mActive != null && mActive.getCategory().equals(task.getCategory())) {
                logInfo("Cancel " + task + " category: " + task.getCategory() + " mode: " + mode);
                task.onCancel();
                return new EmptyCancelableImpl();
            }

            ArrayDeque<AbsWork> works;
            synchronized(mWorks) {
                works = new ArrayDeque<>(mWorks);
            }

            Iterator<AbsWork> iterator = works.iterator();

            label66:
            while(true) {
                while(true) {
                    AbsWork work;
                    do {
                        if (!iterator.hasNext()) {
                            break label66;
                        }
                        work = iterator.next();
                    } while(!work.getCategory().equals(task.getCategory()));

                    if (mode != AppendMode.Replace && mode != AppendMode.ReplaceStrict) {
                        if (mode == AppendMode.Discard || mode == AppendMode.DiscardStrict) {
                            logInfo("Cancel " + task + " category: " + task.getCategory() + " mode: " + mode);
                            task.onCancel();
                            return new EmptyCancelableImpl();
                        }
                    } else {
                        logInfo("Cancel " + work + " mode: " + mode);
                        work.cancel = true;
                        if (!work.finished) {
                            work.onCancel();
                        }

                        logInfo(work.toString() + " is Replaced.");
                    }
                }
            }
        }

        final AbsWork work = execute(task);
        logInfo("Execute " + work + " mode: " + mode + "\n\tmActive: " + mActive);
        return new ICancelable() {
            @Override
            public void cancel() {
                if (!work.cancel && !work.finished) {
                    work.onCancel();
                }
            }
        };
    }

    public void clear() {
        Iterator<AbsWork> iterator = mWorks.iterator();
        while(iterator.hasNext()) {
            AbsWork work = iterator.next();
            if (!work.finished) {
                work.onCancel();
            }
        }
        synchronized(mWorks) {
            mWorks.clear();
        }
    }

    private void logInfo(String info) {
        try {
            Log.i("SerialTaskQueue : ",info);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private synchronized AbsWork execute(final AbsTask task) {
        AbsWork work = new AbsWork() {
            @Override
            public void onWorkThread() {
                logInfo("onWorkThread " + this);
                if (!finished) {
                    if (cancel) {
                        onCancel();
                    } else {
                        task.onWorkThread();
                    }

                }
            }

            @Override
            public void onMainThread() {
                logInfo("onMainThread " + this + "\n\tmActive: " + mActive);
                if (finished) {
                    if (mActive == this) {
                        scheduleNext();
                    }

                } else {
                    if (cancel) {
                        onCancel();
                    } else {
                        task.onMainThread();
                        finished = true;
                        scheduleNext();
                    }

                }
            }

            @Override
            public void onCancel() {
                logInfo("onCancel " + this + "\n\tmActive: " + mActive);
                task.onCancel();
                finished = true;
                if (mActive == this) {
                    scheduleNext();
                }

            }

            @Override
            String getCategory() {
                return task.getCategory();
            }

            @Override
            public String toString() {
                return task.toString() + " category: " + getCategory() + " cancel: " + cancel + " finished: " + finished;
            }
        };
        synchronized(mWorks) {
            mWorks.offer(work);
        }

        if (mActive == null) {
            scheduleNext();
        }

        return work;
    }

    private void scheduleNext() {
        synchronized(mWorks) {
            mActive = mWorks.poll();
        }
        if (mActive != null) {
            if (mActive.cancel) {
                if (!mActive.finished) {
                    mActive.onCancel();
                }
                scheduleNext();
            } else {
                Dispatcher.async(mActive);
            }
        }
    }


    
}

