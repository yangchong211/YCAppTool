package me.pqpo.librarylog4a.appender;

import java.util.ArrayList;
import java.util.List;

import me.pqpo.librarylog4a.Level;
import me.pqpo.librarylog4a.interceptor.Interceptor;


public class AndroidAppender extends AbsAppender {

    protected AndroidAppender(Builder builder) {
        setLevel(builder.level);
        addInterceptor(builder.interceptors);
    }

    @Override
    protected void doAppend(int logLevel, String tag, String msg) {
        android.util.Log.println(logLevel, tag, msg);
    }

    public static class Builder {

        private int level = Level.VERBOSE;
        private List<Interceptor> interceptors;

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            interceptors.add(interceptor);
            return this;
        }

        public AndroidAppender create() {
            return new AndroidAppender(this);
        }

    }

}
