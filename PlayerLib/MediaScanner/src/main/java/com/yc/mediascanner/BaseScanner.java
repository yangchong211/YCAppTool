package com.yc.mediascanner;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.yc.toolutils.context.AppToolUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 扫描器基类，该类实现了扫描器的基本功能。
 *
 * @param <T> 媒体文件对应的实体类型。
 */
public abstract class BaseScanner<T> implements Scanner<T> {

    private static final Executor mExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });

    private String[] mProjection;
    private String mSelection;
    private String[] mSelectionArgs;
    private String mSortOrder;

    private final Uri mUri;
    private final ContentResolver mResolver;
    private final MediaStoreHelper.Decoder<T> mDecoder;

    private OnScanCallback<T> mCallback;
    private final Handler mMainHandler;
    private int mThreshold;

    private boolean mRunning;
    private boolean mCancelled;
    private boolean mFinished;

    private long mLastUpdateTime;

    public BaseScanner(Uri uri, ContentResolver resolver, MediaStoreHelper.Decoder<T> decoder) {
        mUri = uri;
        mResolver = resolver;
        mDecoder = decoder;

        mThreshold = MIN_UPDATE_THRESHOLD;
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public Scanner<T> projection(String[] projection) {
        mProjection = projection;
        return this;
    }

    @Override
    public Scanner<T> selection(String selection) {
        mSelection = selection;
        return this;
    }

    @Override
    public Scanner<T> selectionArgs(String[] args) {
        mSelectionArgs = args;
        return this;
    }

    @Override
    public Scanner<T> sortOrder(String sortOrder) {
        mSortOrder = sortOrder;
        return this;
    }

    @Override
    public Scanner<T> updateThreshold(int threshold) {
        mThreshold = threshold;

        if (mThreshold < MIN_UPDATE_THRESHOLD) {
            mThreshold = MIN_UPDATE_THRESHOLD;
        }

        return this;
    }

    protected synchronized final boolean isRunning() {
        return mRunning;
    }

    protected synchronized final void setRunning(boolean running) {
        mRunning = running;
    }

    protected synchronized final boolean isFinished() {
        return mFinished;
    }

    protected synchronized final void setFinished(boolean finished) {
        mFinished = finished;
    }

    protected synchronized final boolean isCancelled() {
        return mCancelled;
    }

    @Override
    public synchronized final void cancel() {
        mCancelled = true;
        setRunning(false);
        setFinished(true);
    }

    @Override
    public void scan(@NonNull final OnScanCallback<T> callback) throws IllegalStateException {
        AppToolUtils.checkNotNull(callback);

        if (isRunning()) {
            throw new IllegalStateException("scanner is running.");
        }

        mCallback = callback;

        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (isCancelled() || isFinished()) {
                    return;
                }

                setRunning(true);
                setFinished(false);

                notifyStartScan();

                Cursor cursor = mResolver.query(mUri, mProjection, mSelection, mSelectionArgs, mSortOrder);
                if (cursor != null && cursor.moveToFirst()) {
                    forEachCursor(cursor);
                    cursor.close();
                    return;
                }

                notifyFinished(new ArrayList<T>());
            }
        });
    }

    private void forEachCursor(Cursor cursor) {
        int progress = 0;
        int max = cursor.getCount();
        List<T> items = new ArrayList<>(max);

        do {
            progress++;
            items.add(decode(cursor, progress, max));
        } while (cursor.moveToNext() && !isCancelled());

        notifyFinished(items);
    }

    private T decode(Cursor cursor, final int progress, final int max) {
        T item = mDecoder.decode(cursor);
        notifyProgressUpdate(progress, max, item);
        return item;
    }

    private void notifyStartScan() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onStartScan();
            }
        });
    }

    private void notifyProgressUpdate(final int progress, final int max, final T item) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastUpdateTime < mThreshold) {
            return;
        }

        if (isCancelled() || isFinished()) {
            return;
        }

        mLastUpdateTime = currentTime;
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onUpdateProgress(progress, max, item);
            }
        });
    }

    private void notifyFinished(final List<T> items) {
        setFinished(true);
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onFinished(items);
            }
        });
    }
}
