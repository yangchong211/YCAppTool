package com.ycbjie.music.executor.inter;


public interface IExecutor<T> {

    void execute();
    void onPrepare();
    void onExecuteSuccess(T t);
    void onExecuteFail(Exception e);

}
