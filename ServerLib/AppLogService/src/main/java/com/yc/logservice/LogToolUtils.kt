package com.yc.logservice

import java.io.*

object LogToolUtils {


    const val COMMAND_SU = "su"
    const val COMMAND_SH = "sh"
    const val COMMAND_EXIT = "exit\n"
    const val COMMAND_LINE_END = "\n"

    fun saveLogWithPath(log: String, path: String) {
        val file = File(path)
        if (!file.parentFile.exists()) file.parentFile.mkdirs()
        if (!file.exists()) {
            file.createNewFile();
        }
        file.appendText("=======================")
        file.appendText(log)
    }


    fun execCommand(command: String, isRoot: Boolean): CommandResult {
        return execCommand(arrayOf(command), isRoot, true)
    }

    private fun execCommand(
        commands: Array<String?>?, isRoot: Boolean,
        isNeedResultMsg: Boolean
    ): CommandResult {
        var result = -1
        if (commands == null || commands.isEmpty()) {
            return CommandResult(result, null, null)
        }
        var process: Process? = null
        var successResult: BufferedReader? = null
        var errorResult: BufferedReader? = null
        var successMsg: StringBuilder? = null
        var errorMsg: StringBuilder? = null
        var os: DataOutputStream? = null
        try {
            val runtime = Runtime.getRuntime()
            process = runtime.exec(if (isRoot) COMMAND_SU else COMMAND_SH)
            os = DataOutputStream(process.outputStream)
            for (command in commands) {
                if (command == null) {
                    continue
                }
                os.write(command.toByteArray())
                os.writeBytes(COMMAND_LINE_END)
                os.flush()
            }
            os.writeBytes(COMMAND_EXIT)
            os.flush()
            result = process.waitFor()
            // get command result
            if (isNeedResultMsg) {
                successMsg = StringBuilder()
                errorMsg = StringBuilder()
                successResult = BufferedReader(InputStreamReader(process.inputStream))
                errorResult = BufferedReader(InputStreamReader(process.errorStream))
                var s: String
                while (successResult.readLine().also { s = it } != null) {
                    successMsg.append(
                        """
                        $s
                        
                        """.trimIndent()
                    )
                }
                while (errorResult.readLine().also { s = it } != null) {
                    errorMsg.append(
                        """
                        $s
                        """.trimIndent()
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
                successResult?.close()
                errorResult?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            process?.destroy()
        }
        return CommandResult(result, successMsg?.toString(), errorMsg?.toString())
    }

    class CommandResult {
        var result: Int
        var successMsg: String? = null
        var errorMsg: String? = null

        constructor(result: Int) {
            this.result = result
        }

        constructor(result: Int, successMsg: String?, errorMsg: String?) {
            this.result = result
            this.successMsg = successMsg
            this.errorMsg = errorMsg
        }
    }
}