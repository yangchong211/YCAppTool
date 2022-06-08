package com.yc.kotlinbusiness

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.coroutines.resume

class KotlinScopeActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_activity_scope)

        findViewById<TextView>(R.id.tv_1).setOnClickListener {
            dispatcherScope1()
        }
    }

    /**
     * 协程经典做饭案例
     * 把做饭的过程看成是一个线程要完成的事情，把菜、饭、汤相关的任务放进不同的协程中。
     * 1.那么把洗好的米放进电饭煲，就意味着与饭相关的协程可以被挂起，
     * 2.把准备好的煲汤材料放进锅里煮后，那么与汤相关的协程就可以被挂起，
     * 3.这时线程就可以执行与菜相关的任务。
     */
    private fun dispatcherScope1() {
        val newSingleThreadExecutor = Executors.newSingleThreadExecutor()
        val dispatcher = newSingleThreadExecutor.asCoroutineDispatcher()

        val latch = CountDownLatch(3)
        dispatcher.launchCoroutine("饭——淘米","饭——煮饭","饭——盛饭",5000,latch)
        dispatcher.launchCoroutine("汤——准备材料","汤——煲汤","汤——盛汤",3000,latch)
        dispatcher.launchCoroutine("菜——洗菜","菜——炒菜","菜——盛菜",0,latch)
        latch.await()
    }

    /**
     * 拓展函数
     */
    private fun CoroutineDispatcher.launchCoroutine(
        s1: String,
        s2: String,
        s3: String,
        time: Int,
        latch: CountDownLatch
    ) {
        GlobalScope.launch(this) {
            // 任务1
            val result1 = suspendCancellableCoroutine<String> {
                it.resume(s1)
            }
            println(result1)
            // 任务2
            val result2 = suspendCancellableCoroutine<String> {
                println(s2 + " 开始，挂起 " + (time/1000) + "秒")
                Thread{
                    Thread.sleep(time.toLong())
                    it.resume(s2 + "结束")
                }.start()
            }
            println(result2)
            // 任务3
            val result3 = suspendCancellableCoroutine<String> {
                it.resume(s3)
            }
            println(result3)
            latch.countDown()
        }
    }

}