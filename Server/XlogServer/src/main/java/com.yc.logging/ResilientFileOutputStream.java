package com.yc.logging;

import android.support.annotation.RestrictTo;

import java.io.*;
import java.nio.channels.FileChannel;

@RestrictTo(RestrictTo.Scope.LIBRARY)
class ResilientFileOutputStream extends OutputStream {

    private File mFile;

    private FileOutputStream mFileOutputStream;

    private RecoveryCoordinator mRecoveryCoordinator;

    protected OutputStream mOutputStream;

    protected boolean mPresumedClean;

    ResilientFileOutputStream(final File file, boolean append) throws FileNotFoundException {
        this.mFile = file;
        this.mFileOutputStream = new FileOutputStream(file, append);
        this.mOutputStream = new BufferedOutputStream(this.mFileOutputStream);
        this.mPresumedClean = true;
    }

    private boolean isPresumedInError() {
        // existence of mRecoveryCoordinator indicates failed state
        return (this.mRecoveryCoordinator != null && !this.mPresumedClean);
    }

    public void write(final byte b[], final int off, final int len) {
        if (isPresumedInError()) {
            if (!this.mRecoveryCoordinator.isTooSoon()) {
                attemptRecovery();
            }
            return; // return regardless of the success of the recovery attempt
        }

        try {
            this.mOutputStream.write(b, off, len);
            postSuccessfulWrite();
        } catch (IOException e) {
            postIOFailure(e);
        }
    }

    @Override
    public void write(final int b) {
        if (isPresumedInError()) {
            if (!this.mRecoveryCoordinator.isTooSoon()) {
                attemptRecovery();
            }
            return; // return regardless of the success of the recovery attempt
        }
        try {
            this.mOutputStream.write(b);
            postSuccessfulWrite();
        } catch (IOException e) {
            postIOFailure(e);
        }
    }

    @Override
    public void flush() {
        if (this.mOutputStream != null) {
            try {
                this.mOutputStream.flush();
                postSuccessfulWrite();
            } catch (IOException e) {
                postIOFailure(e);
            }
        }
    }

    public FileChannel getChannel() {
        if (null == this.mOutputStream) {
            return null;
        }

        return this.mFileOutputStream.getChannel();
    }

    public File getFile() {
        return this.mFile;
    }


    String getDescription() {
        return "mFile [" + this.mFile + "]";
    }


    OutputStream openNewOutputStream() throws IOException {
        // see LOGBACK-765
        this.mFileOutputStream = new FileOutputStream(this.mFile, true);
        return new BufferedOutputStream(this.mFileOutputStream);
    }

    private void postSuccessfulWrite() {
        if (this.mRecoveryCoordinator != null) {
            this.mRecoveryCoordinator = null;
        }
    }

    void postIOFailure(IOException e) {
        this.mPresumedClean = false;
        if (this.mRecoveryCoordinator == null) {
            this.mRecoveryCoordinator = new RecoveryCoordinator();
        }
    }

    @Override
    public void close() throws IOException {
        if (this.mOutputStream != null) {
            this.mOutputStream.close();
        }
    }

    void attemptRecovery() {
        try {
            close();
        } catch (IOException e) {
        }

        // subsequent writes must always be in append mode
        try {
            this.mOutputStream = openNewOutputStream();
            this.mPresumedClean = true;
        } catch (IOException e) {
        }
    }

}
