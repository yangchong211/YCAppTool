# CPU性能采集库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念介绍
#### 1.0 背景介绍一下
- 在开发APP的时候，通常会有一些性能优化的内容。
    - 比如需要获取当前APP在运行中，占用CPU的比率多少；获取当前设备中的CPU的内核数、主频等信息。
    - 以用来衡量该机器的性能强弱；获取当前进程在系统中处于一种什么状态，当前进程被杀死或者被退出的状态等等。


#### 1.1 CPU活动信息
- 如何获取CPU使用率
    - 系统的 proc/stat 和 /proc/pid/stat 文件动态记录所有 CPU 活动的信息，分别对应总的CPU 使用率和指定进程的 CPU 使用率。
- 以文件的方式为内核与进程提供通信的接口
    - 其信息只存在于内存当中，而不占用外存空间。 用户和程序可以通过 读取 这些文件下的 目录及子目录下的相关文件得到系统提供的一些资源信息。
- 该文件中的所有值都是从系统启动开始累计到当前时刻。
    - 所以，计算系统当前的 CPU 占用率的方法就是，通过一定的算法，计算在间隔较短(ms级)的时间内，cpu的各活动信息的变化量，作为当前的实时 CPU占用率。
    - 目前这个方案在应用不活动的时候会存在使用率为0的情况，我们可以采用多次重试的方案。


#### 1.2 CPU内存架构
- CPU的内存架构
    - ![image](https://img-blog.csdnimg.cn/574aa76843b14f83b6ec938288840119.png)
    - 现在的计算机，基本都是多个CPU，并且有些CPU还是多核的，因此你的Java程序中，每个CUP执行一个线程，并且俩个或者俩个以上的CPU在同时执行任务，这种情况就是我么所说的：并发。
- CPU寄存器是什么
    - 每个CPU都有自己的寄存器，CPU在寄存器上执行操作的速度，远远大于在主内存中。
    - 每个CPU还有自己的高速缓存层，CPU访问缓存层的速度快于访问主存的速度，但比访问内部寄存器的速度还要慢一点。
    - 一个计算机还包含一个主存。所有的CPU都可以访问主存。主存通常比CPU中的缓存大得多。
- CPU寄存器读和写数据
    - 当CPU需要读取主内存的时候，他会将部分数据读到CPU缓存中，甚至可以将CPU缓存中的部分数据读到寄存器中，然后在寄存器中操作，操作完成后，需要将数据写入主存中的时候，先将数据刷新至CPU缓存中，然后在某个时间点将数据刷新到主存中。
    - 当CPU需要在缓存层存放一些东西的时候，存放在缓存中的内容通常会被刷新回主存。CPU缓存可以在某一时刻将数据局部写到它的内存中，和在某一时刻局部刷新它的内存。它不会再某一时刻读/写整个缓存。
- 举一个案例理解CPU执行
    - For example : int i = i + 1
    - 当线程执行这个语句时，会先从主内存中读取i的值，然后复制一份到CPU的高速缓存中，然后CPU执行指令对i进行加1的操作，然后将数据写入高速缓存，最后将最新的i值刷新到主存当重。


#### 1.3 CPU使用率
- 在Linux系统下CPU分为：
    - CPU利用率分为用户态、系统态、空闲态，分别表示CPU处于用户态执行的时间，系统内核执行的时间，和空闲系统进程执行的时间。
    - 平时所说的CPU利用率是指：CPU执行非系统空闲进程的时间 / CPU总的执行时间。
- 先介绍几个和Linux时间有关的名词：HZ、tick与jiffies。
    - HZ：Linux 核心每隔固定周期会发出timer interrupt (IRQ 0)，HZ是用来定义每一秒有几次timer interrupts。例如HZ为1000，就代表每秒有1000次timer interrupts。
    - Tick ：Tick是HZ的倒数，Tick = 1/HZ 。即timer interrupt每发生一次中断的时间。如HZ为250时，tick为4毫秒(millisecond)。
    - Jiffies ：在Linux的内核中，有一个全局变量：Jiffies。 Jiffies代表时间。它的单位随硬件平台的不同而不同。jiffies的单位就是 1/HZ。Intel平台jiffies的单位是1/100秒，这就是系统所能分辨的最小时间间隔了。每个CPU时间片，Jiffies都要加1。 CPU的利用率就是用执行用户态+系统态的Jiffies除以总的Jifffies来表示。
- CPU利用率计算公式也就是：
    - CPU使用率＝（用户态Jiffies＋系统态Jiffies）／总Jiffies



#### 1.4 CPU占用率获取
- CPU是系统非常重要的资源，在Android中/proc/stat
    - 包含了所有CPU的相关详情信息，查看CPU使用情况，CPU不是一个瞬时态，而是一个过程态的体现，一般可以使用top命令和dump cpuinfo命令进行CPU占用率获取。
- top命令获取方法
    - top是比较经典的CPU计算方法，总的cpu时间 = user + nice + system + idle + iowait + irq + softirq
    - 例如：User 147 + Nice 11 + Sys 79 + Idle 408 + IOW 1 + IRQ 0 + SIRQ 6 = 652
    ```
    user 从系统启动开始累计到当前时刻，处于用户态的运行时间，不包含 nice值为负进程。
    nice 从系统启动开始累计到当前时刻，nice值为负的进程所占用的CPU时间
    system 从系统启动开始累计到当前时刻，处于核心态的运行时间
    idle 从系统启动开始累计到当前时刻，除IO等待时间以外的其它等待时间
    iowait 从系统启动开始累计到当前时刻，IO等待时间(since 2.5.41)
    irq 从系统启动开始累计到当前时刻，硬中断时间(since 2.6.0-test4)
    softirq 从系统启动开始累计到当前时刻，软中断时间(since 2.6.0-test4)
    ```
    - 而proc->delta_time是两次读取/proc/pid/stat相减得到，可见，top是一段时间内，计算process的cpu jiffies与总的cpu jiffies差值得到。
    - 通过adb获取top：adb shell top -m 100 -n 1 -s cpu | grep 包名
- dump cpuinfo命令获取方法
    - Android特有的命令，dump cpuinfo命令的实现在androidm/frameworks/base/core/java/com/android/internal/os/ProcessCpuTracker.java类里面，方法是printCurrentState
    - 进程的总Cpu时间processCpuTime = utime + stime + cutime + cstime，该值包括其所有线程的cpu时间。
    - 通过adb获取dump：adb shell dumpsys cpuinfo |grep 包名





### 02.常见思路和做法
#### 2.1 获取CPU信息
- 进程CPU占用率
    - 当我们需要获取进程对CPU的占用率时，可以通过获取 /proc/pid/stat 文件，解析其中的参数进而进行计算得出。
    - 计算的方法为: 进程CPU占用率 = （进程CPU时间2 - 进程CPU时间1） / （CPU总时间2 - CPU总时间1）
    - https://juejin.cn/post/7061788020134920206



#### 2.2 总的CPU时间
- 在Android低版本设备中，可以通过读取 /proc/stat 文件实现
    - /proc/stat 内容首行有8个数值，分别提供了所有CPU在 用户态(user)、用户态-低优先级(nice)、内核态(sys)、空闲态(idle)、io等待(iowait)、硬中断(irq)、软中断(softirq) 状态 下的时间总和，将这些值累加作为系统总的CPU时间(cpuTime)。
    - 计算 iowait/cpuTime 为系统的CPU空闲率，1-cpu空闲率 及为cpu利用率 。注意这里的时间单位为 jiffies，通常一个jiffies 等于10ms。
    - 总的cpu时间totalCpuTime = user + nice + system + idle + iowait + irq + softirq + stealstolen +guest
- 在Android 8.0以上版本
    - 为了防止旁路攻击，普通应用程序已经无法访问/proc/stat 文件，所以无法通过/proc/stat 的方式计算系统cpu利用率。
    - 部分线下性能监控相关的开源库 如Dokit 会在Android8.0以上的设备 通过执行shell 命令 top -n 1 来直接获取某个进程CPU使用率信息，不过这种方式在高版本设备上也是无法使用的，得到的CPU使用率总是为0。
- 计算cpu使用率
    - 1、采样两个足够短的时间间隔的Cpu快照，分别记作t1,t2，其中t1、t2的结构均为： (user、nice、system、idle、iowait、irq、softirq、stealstolen、guest)的9元组;
    - 2、计算总的Cpu时间片totalCpuTime。a) 把第一次的所有cpu使用情况求和，得到s1；b) 把第二次的所有cpu使用情况求和，得到s2；c) s2 - s1得到这个时间间隔内的所有时间片，即totalCpuTime = s2 - s1 ;
    - 3、计算空闲时间idle。idle对应第四列的数据，用第二次的idle - 第一次的idle即可 idle = idle2 - idle1
    - 4、计算cpu使用率。CPU总使用率（%） = 100*((totalCputime2- totalCputime1)-(idle2-idle1))/(totalCputime2-totalCputime1)



#### 2.3 App应用CPU时间
- 单个应用CPU监控
    - 将选中应用的PID传入，读取/proc/PID/stat文件信息及可获取该PID对应程序的CPU信息。
- 计算方法
    - 1、首先获取应用的进程id： adb shell ps | grep com.package | awk '{print $2}' > tmp
    - 2、根据进程id，通过proc获取CPU信息 while read line; do adb shell cat /proc/line/stat | awk '{print 14,15,16,$17}' >> appcpu0; done < tmp
- 以下只解释对我们计算Cpu使用率有用相关参数（14-17列） 参数解释
    - utime 该任务在用户态运行的时间，单位为jiffies
    - stime 该任务在核心态运行的时间，单位为jiffies
    - cutime 所有已死线程在用户态运行的时间，单位为jiffies
    - cstime 所有已死在核心态运行的时间，单位为jiffies
- 进程的总Cpu时间
    - processCpuTime = utime + stime + cutime + cstime，该值包括其所有线程的cpu时间。
- App的CPU占用率
    - 之后可以每1s获取一次CPU信息，分析获得app的CPU占用率等信息。
    - 单个程序的CPU使用率（%） = 100*(processCpuTime2-processCpuTime1)/(totalCpuTime2-totalCpuTime1)




### 03.Api调用说明



### 04.遇到的坑分析
#### 4.1 高版本权限问题




### 参考
- Android平台下的cpu利用率优化实现
  - https://juejin.cn/post/7061788020134920206
- Android 性能监控之CPU监控
  - https://www.chenwenguan.com/android-performance-monitor-cpu/
- Android 8.0以后CPU使用率的方案研究
  - https://cloud.tencent.com/developer/article/1427843

