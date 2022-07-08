package com.yc.architecturelib.livedata

import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class MutableLiveDataStatus<T> : MutableLiveData<T> {

    /**
     * 数据状态
     * 记录数据上一次的状态
     */
    private val mLiveDataStatus = MutableLiveData<LiveDataStatus>()

    /**
     * 重写构造方法，设置数据状态
     */
    constructor() : super() {
        //数据默认是需要重设的
        setReset()
    }
    /**
     * @param value      默认数据，如果需要直接获取数据的话
     * @param initStatus 默认数据的初始状态
     */
    /**
     * @param value 默认数据，如果需要直接获取数据的话
     */
    @JvmOverloads
    constructor(value: T, initStatus: LiveDataStatus = LiveDataStatus.RESET) : super(value) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            changeStatus(initStatus)
        } else {
            postChangeStatus(initStatus)
        }
    }

    /**
     * 设置有效数据
     *
     * @param value 数据 不可null
     */
    override fun setValue(value: T?) {
        setValue(value, LiveDataStatus.SUCCESS.message)
    }

    /**
     * 设置数据，同时设置Success消息信息
     *
     * @param value 数据
     * @param msg   消息
     */
    fun setValue(value: T?, msg: String?) {
        super.setValue(value)
        changeStatus(LiveDataStatus.SUCCESS.setMessage(msg))
        //状态恢复到完成状态
        changeStatus(LiveDataStatus.COMPLETE)
    }

    /**
     * 数据改变前
     */
    fun setStart() {
        changeStatus(LiveDataStatus.START)
    }

    /**
     * 数据改变出现错误
     * @param msg 一些错误描述信息
     */
    fun setError(msg: String?) {
        changeStatus(LiveDataStatus.ERROR.setMessage(msg))
    }

    /**
     * 数据需要重置
     */
    fun setReset() {
        changeStatus(LiveDataStatus.RESET)
    }

    /**
     * 在fragment中要使用ViewLifecycleOwner
     */
    fun observe(
        fragment: Fragment,
        observer: Observer<in T>,
        observerStatus: Observer<LiveDataStatus>
    ) {
        this.observe(fragment.viewLifecycleOwner, observer, observerStatus)
    }

    /**
     * 在fragment中要使用ViewLifecycleOwner
     */
    fun observe(fragment: Fragment, observer: Observer<in T>) {
        this.observe(fragment.viewLifecycleOwner, observer)
    }

    /**
     * 在fragment中要使用ViewLifecycleOwner
     */
    fun observeStatus(fragment: Fragment, observer: Observer<LiveDataStatus>) {
        this.observeStatus(fragment.viewLifecycleOwner, observer)
    }

    /**
     * 数据状态观测
     *
     * @param owner          生命周期
     * @param observer       数据改变
     * @param observerStatus 状态改变
     */
    fun observe(
        owner: LifecycleOwner,
        observer: Observer<in T>,
        observerStatus: Observer<LiveDataStatus>
    ) {
        this.observe(owner, observer)
        observeStatus(owner, observerStatus)
    }

    /**
     * 数据观察
     *
     * @param owner    生命周期
     * @param observer 数据改变
     */
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
    }

    /**
     * 数据状态观察
     *
     * @param owner    生命周期
     * @param observer 状态改变
     */
    fun observeStatus(owner: LifecycleOwner, observer: Observer<LiveDataStatus>) {
        mLiveDataStatus.observe(owner, observer)
    }

    /**
     * 通知数据发生改变
     */
    fun notifyDataChanged() {
        val value = value
        setValue(value)
    }

    /**
     * 检查活动数据状态，如果不是完成状态，将改变成[LiveDataStatus.RESET]
     * 恢复到初始状态
     */
    fun checkNotCompleteReset() {
        if (mLiveDataStatus.value !== LiveDataStatus.COMPLETE) {
            mLiveDataStatus.value = LiveDataStatus.RESET
        }
    }

    /**
     * 修改状态 进行判断防止多次观察重复相应
     */
    private fun changeStatus(liveDataStatus: LiveDataStatus) {
        mLiveDataStatus.value = liveDataStatus
    }

    private fun postChangeStatus(liveDataStatus: LiveDataStatus) {
        mLiveDataStatus.postValue(liveDataStatus)
    }
}