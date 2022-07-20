package com.yc.kotlinbusiness.scope

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yc.kotlinbusiness.R
import com.yc.toastutils.ToastUtils
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
        findViewById<TextView>(R.id.tv_2).setOnClickListener {
            dispatcherScope2()
        }
        findViewById<TextView>(R.id.tv_3).setOnClickListener {
            dispatcherScope3()
        }
        findViewById<TextView>(R.id.tv_4).setOnClickListener {
            dispatcherScope4()
        }
        findViewById<TextView>(R.id.tv_5).setOnClickListener {
            dispatcherScope5()
        }
        findViewById<TextView>(R.id.tv_6).setOnClickListener {
            dispatcherScope6()
        }
        findViewById<TextView>(R.id.tv_7).setOnClickListener {
            dispatcherScope7()
        }
        findViewById<TextView>(R.id.tv_8).setOnClickListener {
            dispatcherScope8()
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
        s1: String, s2: String, s3: String, time: Int, latch: CountDownLatch
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

    private fun coroutineSend() {
        val uiScope = CoroutineScope(Dispatchers.Main)
        uiScope.launch {
            println("coroutineSend get 1 ")
            val deffer = async(Dispatchers.Default) {
                val coroutineResult = getCoroutineResult()
                println("coroutineSend get 2 $coroutineResult")
            }

            val coroutineResult = deffer.await()
            println("coroutineSend get 3 $coroutineResult")
        }

    }

    private suspend fun getCoroutineResult(): String {
        delay(9000L)
        return "coroutine result"
    }


    private fun dispatcherScope2() {
        //这段代码就是启动一个协程，并启动，延迟1秒后打印world，就从这个launch方法进行切入
        GlobalScope.launch { 
            delay(1000L) //  延迟（挂起）1000毫秒，注意这不会阻塞线程
            println("dispatcher scope2 World!")
        }
    }


    private fun dispatcherScope3() {
        //设置启动模式
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            delay(1000L)
            println("dispatcher scope3 设置启动模式!")
        }
        println("dispatcher scope3 hello world!")
        job.start()
    }

    private fun dispatcherScope4() {
        //协程间是如何切换的
        //这段代码先打印出next，然后延迟1秒钟后打印出now，像是android里handler的post和postDelay方法。
        GlobalScope.launch(Dispatchers.Default){
            println("dispatcher scope4 "+"协程间是如何切换的")
            println("dispatcher scope4 ---${Thread.currentThread().name}")
            launch {
                delay(1000)
                println("dispatcher scope4 "+"now")
            }
            println("dispatcher scope4 "+"next")
        }
    }

    var launch5 : Job ?= null

    private fun dispatcherScope5() {
        val uiScope = CoroutineScope(Dispatchers.Main)
        launch5 = uiScope.launch {
            println("dispatcher scope5 " + "模拟发送请求")
            val deffer = async(Dispatchers.Default) {
                getResult()
            }
            val result = deffer.await()
            println("dispatcher scope5 ---获取 $result")
        }
    }

    private suspend fun getResult(): String {
        delay(5000L)
        println("dispatcher scope5 "+"模拟请求时间")
        return "result"
    }

    private fun dispatcherScope6() {
        if (launch5 == null){
            ToastUtils.showRoundRectToast("请先点击按钮5开启协程")
            return
        }
        launch5?.cancel()
        println("dispatcher scope5 "+"协程取消")
    }

    private fun dispatcherScope7() {
        val job = GlobalScope.launch {
            delay(1000L)
            println("dispatcher scope7 "+"World")
            delay(1000L)
        }
        println("dispatcher scope7 "+"Hello")
        runBlocking {
            job.join()
        }
        println("dispatcher scope7 "+"Good")
    }


    private fun dispatcherScope8() {
        runBlocking {
            val user = getUserInfo()
            println("dispatcher scope8 user $user")
            val friendList = getFriendList(user)
            println("dispatcher scope8 friendList $friendList")
            val feedList = getFeedList(friendList)
            println("dispatcher scope8 feedList $feedList")
        }
    }

    private suspend fun getUserInfo(): String {
        withContext(Dispatchers.IO) {
            delay(1000L)
        }

        return "BoyCoder"
    }

    private suspend fun getFriendList(user: String): String {
        withContext(Dispatchers.IO) {
            delay(1000L)
        }
        return "Tom, Jack"
    }

    private suspend fun getFeedList(list: String): String {
        withContext(Dispatchers.IO) {
            delay(1000L)
        }
        return "{FeedList..}"
    }

}