package com.yc.logging;

import android.os.Process;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;

import com.yc.logging.util.LoggerUtils;

import java.util.Date;
import java.util.Locale;


@RestrictTo(RestrictTo.Scope.LIBRARY)
class LongLog extends AbstractLog {
    private String mMsg;
    private Object[] mArgs;
    private Date mDate;
    private String mTag;
    private int mTid;
    private String mTnm;
    private boolean mFormat;

    @Override
    public String getContent() {
        String fmtMsg = mMsg;
        if (mFormat && mArgs != null && mArgs.length > 0) {
            try {
                fmtMsg = String.format(Locale.getDefault(), mMsg, mArgs);//"say % hello to %s"
            } catch (Exception ignore) {
            }
        }
        if (mFormat && !TextUtils.isEmpty(fmtMsg)) {
            return LoggerUtils.formatForLogHead(mDate)
                    + " "
                    + Process.myPid() + "-" + mTid
                    + " "
                    + mTnm
                    + " "
                    + logLevel.name
                    + "/"
                    + mTag
                    + ": "
                    + fmtMsg;
        }
        return fmtMsg;
    }

    @Override
    public String getTag() {
        return mTag;
    }

    @Override
    public String getMsg() {
        if (mArgs != null && mArgs.length > 0) {
            try {
                return String.format(Locale.getDefault(), mMsg, mArgs);
            } catch (Exception ignore) {
            }
        }
        return mMsg;
    }

    @Override
    public byte[] getData() {
        throw new UnsupportedOperationException();
    }

    public static final class Builder {
        private Level logLevel;
        private String mMsg;
        private Object[] mArgs;
        private Date mDate;
        private String mTag;
        private int mTid;
        private String mTnm;
        private boolean mFormat = true;

        public Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder setMsg(String mMsg) {
            this.mMsg = mMsg;
            return this;
        }

        public Builder setLogLevel(Level logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder setArgs(Object[] mArgs) {
            this.mArgs = mArgs;
            return this;
        }

        public Builder setDate(Date mDate) {
            this.mDate = mDate;
            return this;
        }

        public Builder setTag(String mTag) {
            this.mTag = mTag;
            return this;
        }

        public Builder setTid(int mTid) {
            this.mTid = mTid;
            return this;
        }

        public Builder setTnm(String mTnm) {
            this.mTnm = mTnm;
            return this;
        }

        public Builder setFormat(boolean format) {
            mFormat = format;
            return this;
        }

        public LongLog build() {
            LongLog longLog = new LongLog();
            longLog.mDate = this.mDate;
            longLog.logLevel = this.logLevel;
            longLog.mTid = this.mTid;
            longLog.mMsg = this.mMsg;
            longLog.mTag = this.mTag;
            longLog.mTnm = this.mTnm;
            longLog.mArgs = this.mArgs;
            longLog.mFormat = this.mFormat;
            return longLog;
        }
    }
}
