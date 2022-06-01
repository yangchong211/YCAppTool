package com.yc.logupload.config

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.yc.appprocesslib.AppStateMonitor

/**
 * @author yangchong
 * email  : yangchong211@163.com
 * time   : 2022/5/11
 * desc   : 初始化操作
 * revise :
 */
class UploadInitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        try {
            val context = context
            context?.let { AppStateMonitor.getInstance().init(it) }
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to auto initialize InitProvider", ex)
        }
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    companion object {
        private const val TAG = "UploadInitProvider"
    }
}