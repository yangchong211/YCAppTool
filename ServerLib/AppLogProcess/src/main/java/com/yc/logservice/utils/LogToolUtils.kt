package com.yc.logservice.utils

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
//            Log.d("feifei","---Runtime.getRuntime()");
            val runtime = Runtime.getRuntime()
            //            Log.d("feifei","---Runtime.getRuntime() sucess");

//            Log.d("feifei","---runtime.exe:"+(isRoot ? COMMAND_SU : COMMAND_SH));
            process = runtime.exec(if (isRoot) COMMAND_SU else COMMAND_SH)
            //            Log.d("feifei","---runtime.exe finish");
            os = DataOutputStream(process.outputStream)
            for (command in commands) {
                if (command == null) {
                    continue
                }


// donnot use os.writeBytes(commmand), avoid chinese charset error
                os.write(command.toByteArray())
                os.writeBytes(COMMAND_LINE_END)
                os.flush()
            }
            os.writeBytes(COMMAND_EXIT)
            os.flush()

//            Log.d("feifei","process.waitFor");
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