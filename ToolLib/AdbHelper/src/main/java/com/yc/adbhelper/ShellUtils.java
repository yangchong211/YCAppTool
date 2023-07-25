package com.yc.adbhelper;

import com.yc.appcontextlib.AppToolUtils;
import com.yc.toolutils.AppLogUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public final class ShellUtils {


    private ShellUtils() {

    }

    // 日志 TAG
    private static final String TAG = ShellUtils.class.getSimpleName();

    // 操作成功状态码
    private static final int SUCCESS = 0;

    /**
     * 执行 shell 命令
     *
     * @param command 待执行命令
     * @param isRoot  是否以 root 权限执行
     * @return {@link CommandResult}
     */
    public static CommandResult execCmd(
            final String command,
            final boolean isRoot
    ) {
        return execCmd(new String[]{command}, isRoot, true);
    }

    /**
     * 执行 shell 命令
     *
     * @param commands 多条待执行命令
     * @param isRoot   是否以 root 权限执行
     * @return {@link CommandResult}
     */
    public static CommandResult execCmd(
            final List<String> commands,
            final boolean isRoot
    ) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot, true);
    }

    /**
     * 执行 shell 命令
     *
     * @param commands 多条待执行命令
     * @param isRoot   是否以 root 权限执行
     * @return {@link CommandResult}
     */
    public static CommandResult execCmd(
            final String[] commands,
            final boolean isRoot
    ) {
        return execCmd(commands, isRoot, true);
    }

    /**
     * 执行 shell 命令
     *
     * @param command         待执行命令
     * @param isRoot          是否以 root 权限执行
     * @param isNeedResultMsg 是否需要返回结果消息 (error、success message)
     * @return {@link CommandResult}
     */
    public static CommandResult execCmd(
            final String command,
            final boolean isRoot,
            final boolean isNeedResultMsg
    ) {
        return execCmd(new String[]{command}, isRoot, isNeedResultMsg);
    }

    /**
     * 执行 shell 命令
     *
     * @param commands        多条待执行命令
     * @param isRoot          是否以 root 权限执行
     * @param isNeedResultMsg 是否需要结果消息 (error、success message)
     * @return {@link CommandResult}
     */
    public static CommandResult execCmd(
            final List<String> commands,
            final boolean isRoot,
            final boolean isNeedResultMsg
    ) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot, isNeedResultMsg);
    }

    /**
     * 执行 shell 命令
     *
     * @param commands        多条待执行命令
     * @param isRoot          是否以 root 权限执行
     * @param isNeedResultMsg 是否需要结果消息 (error、success message)
     * @return {@link CommandResult}
     */
    public static CommandResult execCmd(
            final String[] commands,
            final boolean isRoot,
            final boolean isNeedResultMsg
    ) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }
        Process process = null;
        DataOutputStream dos = null;
        String successMsg = null;
        String errorMsg = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            dos = new DataOutputStream(process.getOutputStream());
            // 循环写入待执行命令
            for (String command : commands) {
                if (command == null) continue;
                dos.write(command.getBytes());
                dos.writeBytes(DevFinal.SYMBOL.NEW_LINE);
                dos.flush();
            }
            dos.writeBytes("exit" + DevFinal.SYMBOL.NEW_LINE);
            dos.flush();
            // 为了避免 Process.waitFor() 导致主线程堵塞问题, 最好读取信息
            if (isNeedResultMsg) { // 如果程序不断在向输出流和错误流写数据, 而 JVM 不读取的话, 当缓冲区满之后将无法继续写入数据, 最终造成阻塞在 waitFor() 这里
                // 读取成功数据
                successMsg = consumeInputStream(
                        new InputStreamReader(process.getInputStream(), DevFinal.ENCODE.UTF_8)
                );
                // 读取异常数据
                errorMsg = consumeInputStream(
                        new InputStreamReader(process.getErrorStream(), DevFinal.ENCODE.UTF_8)
                );
            }
            // 执行结果状态码
            result = process.waitFor();
        } catch (Exception e) {
            AppLogUtils.e(TAG, "execCmd " + e);
        } finally {
            AppToolUtils.closeIO(dos);
            // 进程销毁
            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(result, successMsg, errorMsg);
    }

    /**
     * 消费 InputStream 并且返回字符串
     *
     * @param reader {@link InputStreamReader}
     * @return 读取流数据
     */
    private static String consumeInputStream(final InputStreamReader reader) {
        BufferedReader br = null;
        try {
            StringBuilder builder = new StringBuilder();
            br = new BufferedReader(reader);
            String str;
            if ((str = br.readLine()) != null) {
                builder.append(str);
                while ((str = br.readLine()) != null) {
                    builder.append(DevFinal.SYMBOL.NEW_LINE).append(str);
                }
            }
            return builder.toString();
        } catch (Exception e) {
            AppLogUtils.e(TAG, "consumeInputStream " + e);
        } finally {
            AppToolUtils.closeIO(br);
        }
        return null;
    }

    // ============
    // = 对外实体类 =
    // ============

    /**
     * detail: 命令执行结果实体类
     *
     * @author Ttt
     */
    public static final class CommandResult {

        // 执行结果状态码
        public int result;
        // 成功信息
        public String successMsg;
        // 错误信息
        public String errorMsg;

        /**
         * 构造函数
         *
         * @param result     执行结果状态码
         * @param successMsg 成功信息
         * @param errorMsg   错误信息
         */
        public CommandResult(
                final int result,
                final String successMsg,
                final String errorMsg
        ) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        /**
         * 判断是否执行成功
         *
         * @return {@code true} yes, {@code false} no
         */
        public boolean isSuccess() {
            return result == SUCCESS;
        }

        /**
         * 判断是否执行成功 ( 判断 errorMsg )
         *
         * @return {@code true} yes, {@code false} no
         */
        public boolean isSuccess2() {
            return result == SUCCESS && (errorMsg == null || errorMsg.length() == 0);
        }

        /**
         * 判断是否执行成功 ( 判断 successMsg )
         *
         * @return {@code true} yes, {@code false} no
         */
        public boolean isSuccess3() {
            return result == SUCCESS && successMsg != null && successMsg.length() != 0;
        }

        /**
         * 判断是否执行成功 ( 判断 successMsg ) , 并且 successMsg 是否包含某个字符串
         *
         * @param contains 待校验包含字符串
         * @return {@code true} yes, {@code false} no
         */
        public boolean isSuccess4(final String contains) {
            if (result == SUCCESS && successMsg != null && successMsg.length() != 0) {
                return contains != null && contains.length() != 0 && successMsg.toLowerCase().contains(contains);
            }
            return false;
        }
    }

}
