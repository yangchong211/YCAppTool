package com.yc.cpu;


import android.text.TextUtils;

import com.yc.toolutils.AppLogUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : cpu采集
 *     revise:
 * </pre>
 */
public final class ApmCpuHelper {

    private static volatile ApmCpuHelper singleton = null;

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static ApmCpuHelper getInstance() {
        if (singleton == null) {
            synchronized (ApmCpuHelper.class) {
                if (singleton == null) {
                    singleton = new ApmCpuHelper();
                }
            }
        }
        return singleton;
    }

    public CpuRateBean get(){
        long startTime = System.currentTimeMillis();
        //CPU的使用率读取耗时在70ms左右
        CpuRateBean cpuRate = getCpuRateData();
        //有一定概率存在小于等于0的情况，重试两次
        if (cpuRate.getProcess() <= 0 || cpuRate.getTotal() <= 0) {
            cpuRate = getCpuRateData();
            cpuRate.setRetryTimes(1);
        }
        if (cpuRate.getProcess() <= 0 || cpuRate.getTotal() <= 0) {
            cpuRate = getCpuRateData();
            cpuRate.setRetryTimes(2);
        }
        if (cpuRate.getProcess() <= 0 || cpuRate.getTotal() <= 0) {
            cpuRate = getCpuRateData();
            cpuRate.setRetryTimes(3);
        }
        long endTime = System.currentTimeMillis();
        AppLogUtils.d("ApmCpuHelper get , total time : " + (endTime - startTime));
        return cpuRate;
    }


    /**
     * 获取总的CPU使用率(可能部分厂商隐藏了CPU状态)
     * CPU在占用率计算上会用到2个值：1.某段时间内Android系统CPU总的使用时间totalCPUTime；2.被测试app某段时间自身使用CPU的时间appCPUTime；
     * appCPU占用率 =(appCPUTime2-appCPUTime1) /（totalCPUTime2-totalCPUTime1）*100;
     * CPU总使用率为：(totalCpu2 - totalCpu1) / (float)(totalCpu2 - totalCpu1)
     *
     * 1、CPU的原始数据为：CPU  16394633 4701297 9484146 36314562 70851 1797 202347 0 0 0
     * 2、后面的数字分别对应：CPU  user nice system idle iowait irq softirq 0 0 0
     * 3、总的时间为：totalCpuTime = user + nice + system + idle + iowait + irq + softirq
     *
     * @return 使用率
     */
    private CpuRateBean getCpuRateData() {
        try {
            //获取时间1总的CPU时间
            long totalCpu1 = getTotalCpuTime();
            //获取时间1，App总的进程时间
            long totalProcess1 = getAppCpuTime();
            //休眠一段时间
            Thread.sleep(100);
            //获取时间2总的CPU时间
            long totalCpu2 = getTotalCpuTime();
            //获取时间2总的进程时间
            long totalProcess2 = getAppCpuTime();
            AppLogUtils.d("ApmCpuHelper get , totalCpu1：" + totalCpu1
                    + " , totalProcess1：" + totalProcess1
                    + " , totalCpu2：" + totalCpu2
                    + " , totalProcess2：" + totalProcess2);
            //CPU总使用率
            float totalRate = (totalCpu2 - totalCpu1) / (float) (totalCpu2 - totalCpu1);
            //CPU占用率
            float processRate = ((totalProcess1 - totalProcess2)) / (float) (totalCpu2 - totalCpu1);
            return new CpuRateBean(totalRate, processRate);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return new CpuRateBean(0f, 0f);
    }

    /**
     * 获取系统总CPU使用时间【注意在Android8.0后就读取不了/proc/stat内容】
     * /proc/stat 内容首行有8个数值
     * 分别提供了所有CPU在 用户态(user)、用户态-低优先级(nice)、内核态(sys)、空闲态(idle)、io等待(iowait)、
     * 硬中断(irq)、软中断(softirq) 状态 下的时间总和，将这些值累加作为系统总的CPU时间(cpuTime)。
     *
     * 读取 proc/stat
     * @return
     */
    public long getTotalCpuTime() {
        String[] cpuInfos = null;
        BufferedReader reader = null;
        try {
            FileInputStream inputStream = new FileInputStream("/proc/stat");
            reader = new BufferedReader(new InputStreamReader(inputStream), 1000);
            String load = reader.readLine();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
            AppLogUtils.d("ApmCpuHelper getTotalCpuTime , cpuInfos : " + cpuInfos);
        }  finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        AppLogUtils.d("ApmCpuHelper getTotalCpuTime , cpuInfos : " + cpuInfos);
        if (cpuInfos != null){
            long totalCpu = Long.parseLong(cpuInfos[2])
                    + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                    + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                    + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
            return totalCpu;
        }
        return 0;
    }


    /**
     * 获取应用占用的CPU时间
     * 读取 /proc/pid/stat
     * @return
     */
    public long getAppCpuTime() {
        String[] cpuInfos = null;
        BufferedReader reader = null;
        try {
            int pid = android.os.Process.myPid();
            FileInputStream inputStream = new FileInputStream("/proc/" + pid + "/stat");
            reader = new BufferedReader(new InputStreamReader(inputStream), 1000);
            String load = reader.readLine();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (cpuInfos != null){
            long appCpuTime = Long.parseLong(cpuInfos[13])
                    + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                    + Long.parseLong(cpuInfos[16]);
            return appCpuTime;
        }
        return 0;
    }


    /**
     * 通过Shell指令获取CPU的总使用率，这个方法不用计算，比较准确，作为重要参考(可能部分厂商隐藏了CPU状态)
     * 格式：User 13%, System 5%, IOW 0%, IRQ 0%
     *
     * @return CPU使用率
     */
    public float getCpuRateTop() {
        int rate = 0;
        try {
            StringBuilder tv = new StringBuilder();
            String result;
            Process p;
            p = Runtime.getRuntime().exec("top -n 1");
            InputStream inputStream = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            while ((result = br.readLine()) != null) {
                if (TextUtils.isEmpty(result) || !(result.contains("%") && result.contains("User") && result
                        .contains("System"))) {
                    continue;
                } else {
                    String[] CPUusr = result.split("%");
                    tv.append("USER:" + CPUusr[0] + "\n");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n");
                    tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n");
                    rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    break;
                }
            }
            AppLogUtils.d("ApmCpuHelper getCpuRateTop : " + tv + " , " + rate);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rate / 100f;
    }


    /**
     * 获取总的CPU使用率(可能部分厂商隐藏了CPU状态)
     * 1、CPU的原始数据为：CPU  16394633 4701297 9484146 36314562 70851 1797 202347 0 0 0
     * 2、后面的数字分别对应：CPU  user nice system idle iowait irq softirq 0 0 0
     * 3、总的时间为：totalCpuTime = user + nice + system + idle + iowait + irq + softirq
     * 4、CPU总使用率为：((totalCpuTime2-totalCpuTime1) - (idel2-idel1)) / (totalCpuTime2-totalCpuTime1)
     *
     * @return 使用率
     */
    public float getCpuProcRate() {
        try {
            long totalCpu1 = getTotalCpuTime();
            Thread.sleep(50);
            long totalCpu2 = getTotalCpuTime();
            return (totalCpu2 - totalCpu1) / (float) (totalCpu2 - totalCpu1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return 0f;
    }


    /**
     * 获取当前CPU占比，此方法较为耗时，网友提供的，不太建议使用
     * 在实际测试中发现，有的手机会隐藏CPU状态，不会完全显示所有CPU信息，例如MX5，所有建议只做参考
     *
     * @return 百分比
     */
    public float getTempProcCPURate() {
        String path = "/proc/stat";// 系统CPU信息文件
        long[] totalJiffies = new long[2];//CPU总时间
        long[] totalIdle = new long[2];//CPU总空闲时间
        int firstCPUNum = 0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader;
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile(" [0-9]+");
        for (int i = 0; i < 2; i++) {
            try {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null && (i == 0 || currentCPUNum < firstCPUNum)) {
                    if (str.toLowerCase().startsWith("cpu")) {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find()) {
                            try {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {
                                    //空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        if (i == 0) {
                            firstCPUNum = currentCPUNum;
                            try {
                                //暂停50毫秒，等待系统更新信息。
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate = -1;
        if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1]) {
            rate = 1.0 * ((totalJiffies[1] - totalIdle[1]) - (totalJiffies[0] - totalIdle[0])) / (totalJiffies[1]
                    - totalJiffies[0]);
        }
        return (float) rate;
    }

}
