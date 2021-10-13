package com.yc.serialtasklib;


import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Iterator;

public class SerialTaskQueue {
    
    private final ArrayDeque<Work> mWorks = new ArrayDeque<>();
    private Work mActive;

    public SerialTaskQueue() {

    }

    @MainThread
    @NonNull
    public Cancelable append(Task task, AppendMode mode) {
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
                return new EmptyCancelable();
            }

            ArrayDeque works;
            synchronized(mWorks) {
                works = new ArrayDeque<>(mWorks);
            }

            Iterator iter = works.iterator();

            label66:
            while(true) {
                while(true) {
                    Work work;
                    do {
                        if (!iter.hasNext()) {
                            break label66;
                        }

                        work = (Work)iter.next();
                    } while(!work.getCategory().equals(task.getCategory()));

                    if (mode != AppendMode.Replace && mode != AppendMode.ReplaceStrict) {
                        if (mode == AppendMode.Discard || mode == AppendMode.DiscardStrict) {
                            logInfo("Cancel " + task + " category: " + task.getCategory() + " mode: " + mode);
                            task.onCancel();
                            return new EmptyCancelable();
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

        final Work work = execute(task);
        logInfo("Execute " + work + " mode: " + mode + "\n\tmActive: " + mActive);
        return new Cancelable() {
            public void cancel() {
                if (!work.cancel && !work.finished) {
                    work.onCancel();
                }

            }
        };
    }

    public void clear() {
        Iterator var1 = mWorks.iterator();

        while(var1.hasNext()) {
            Work work = (Work)var1.next();
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
        } catch (Throwable var3) {
            var3.printStackTrace();
        }
    }

    private synchronized Work execute(final Task task) {
        Work work = new Work() {
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

            public void onCancel() {
                logInfo("onCancel " + this + "\n\tmActive: " + mActive);
                task.onCancel();
                finished = true;
                if (mActive == this) {
                    scheduleNext();
                }

            }

            String getCategory() {
                return task.getCategory();
            }

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
            mActive = (Work)mWorks.poll();
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

