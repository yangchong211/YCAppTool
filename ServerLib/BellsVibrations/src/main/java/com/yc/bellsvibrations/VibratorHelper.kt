package com.yc.bellsvibrations

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCServerLib
 *     time  : 2018/11/9
 *     desc  : 手机震动的设置步骤
 *     doc  :
 * </pre>
 */
class VibratorHelper (private val context: Context){

    /**
     * 在使用震动服务时需要如下权限：android.permission.VIBRATE
     */
    private val vibrator: Vibrator? by lazy {
        //通过系统服务获得手机震动服务
        context.applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    /**
     * 得到震动服务后检测vibrator是否存在
     */
    private fun isVibrator() : Boolean{
        return vibrator?.hasVibrator() == true
    }

    /**
     * 以pattern方式重复repeat次启动vibrator
     * pattern的形式为new long[]{arg1,arg2,arg3,arg4......},其中以两个一组的如arg1和arg2为一组、arg3和arg4为一组，每一组的前一个代表等待多少毫秒启动vibrator，后一个代表vibrator持续多少毫秒停止，之后往复即可。
     * Repeat表示重复次数，当其为-1时，表示不重复只以pattern的方 式运行一次)。
     */
    fun vibrate(pattern: LongArray, repeat: Int) {
        vibrator?.run {
            if (hasVibrator()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    this.vibrate(VibrationEffect.createWaveform(pattern,repeat))
                } else{
                    this.vibrate(pattern, repeat)
                }
            }
        }
    }

    /**
     * 开始启动vibrator持续milliseconds毫秒
     */
    fun vibrate(milliseconds : Long){
        vibrator?.run {
            if (hasVibrator()){
                this.vibrate(milliseconds)
            }
        }
    }

    /**
     * Vibrator停止
     */
    fun cancel() {
        vibrator?.cancel()
    }
}