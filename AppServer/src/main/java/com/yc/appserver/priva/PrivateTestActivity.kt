package com.yc.appserver.priva

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yc.appserver.R
import com.yc.logclient.LogUtils
import com.yc.privateserver.PrivateService
import com.yc.privateserver.UserPrivacyHolder
import com.yc.roundcorner.view.RoundTextView


class PrivateTestActivity : AppCompatActivity() {

    private lateinit var tvTest1: RoundTextView
    private lateinit var tvTest2: RoundTextView

    companion object{
        fun startActivity(context: Context) {
            try {
                val target = Intent()
                target.setClass(context, PrivateTestActivity::class.java)
                context.startActivity(target)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_main)
        initView()
        initListener()
        UserPrivacyHolder.installApp(this)
        UserPrivacyHolder.setIsInitUserPrivacy(true,true)
        LogUtils.i("Log test info : LogTestActivity is create")
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_DENIED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE),100)
            }
        }
    }

    private fun initView() {
        tvTest1 = findViewById(R.id.tv_private_1)
        tvTest2 = findViewById(R.id.tv_private_2)
    }

    private fun initListener() {
        tvTest1.setOnClickListener {
            val sb = StringBuilder()
            sb.append("AndroidId:  ").append(PrivateService.getAndroidId())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sb.append("\nMEID:  ").append(PrivateService.getMEID())
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sb.append("\nIMEI1:  ").append(PrivateService.getImei1())
                sb.append("\nIMEI2:  ").append(PrivateService.getImei2())
                sb.append("\nIMEI:  ").append(PrivateService.getImei())
            }
            sb.append("\nSN:  ").append(PrivateService.getSN())
            sb.append("\nDeviceSN:  ").append(PrivateService.getSN())
            sb.append("\n手机运营商:  ").append(PrivateService.getProviderName())
            sb.append("\nSim卡的运营商Id:  ").append(PrivateService.getOperatorId())
            sb.append("\n卡的运营商名称:  ").append(PrivateService.getOperatorName())
            sb.append("\n设备DeviceId:  ").append(PrivateService.getDeviceId())
            tvTest2.setText(sb.toString())
        }
    }


}