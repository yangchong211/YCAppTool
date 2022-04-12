package com.yc.jetpack.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yc.jetpack.R
import java.util.*

class LoginActivity : AppCompatActivity() {

    // 获取权限
    private val REQUEST_CODE_PERMISSIONS = 101
    private val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
    private val MAX_NUMBER_REQUEST_PERMISSIONS = 2

    private val permissions = Arrays.asList(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private var permissionRequestCount: Int = 0

    /*@JvmStatic
    fun startActivity(context: Context) {
        try {
            val target = Intent()
            target.setClass(context, LoginActivity::class.java)
            //target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    companion object {
        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack_login)
        savedInstanceState?.let {
            permissionRequestCount = it.getInt(KEY_PERMISSIONS_REQUEST_COUNT, 0)
        }
        // 获取权限
        requestPermissionsIfNecessary()
    }

    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            if (permissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                permissionRequestCount += 1
                ActivityCompat.requestPermissions(
                    this,
                    permissions.toTypedArray(),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }
    }

    /** Permission Checking  */
    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in permissions) {
            hasPermissions = hasPermissions and isPermissionGranted(permission)
        }
        return hasPermissions
    }

    private fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            requestPermissionsIfNecessary() // no-op if permissions are granted already.
        }
    }


}
