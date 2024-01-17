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
import com.yc.privateserver.SafePrivateHelper
import com.yc.privateserver.UserPrivacyInit
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
        UserPrivacyInit.installApp(this)
        UserPrivacyInit.setIsInitUserPrivacy(true,true)
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
            //获取Android唯一标识符
            val androidId = PrivateService.getAndroidId()
//            //获取IMEI1
//            val imei1 = PrivateService.getImei1()
//            //获取IMEI1
//            val imei2 = PrivateService.getImei2()
//            //获取IMEI
//            val imei = PrivateService.getImei()
//            //获取手机的SN
//            val sn = PrivateService.getSN()
//            //获取手机运营商
//            val providerName = PrivateService.getProviderName()
//            //获取Sim卡的运营商Id
//            val operatorId = PrivateService.getOperatorId()
//            //获取卡的运营商名称
//            val operatorName = PrivateService.getOperatorName()
//            //获取设备id
//            val deviceId = PrivateService.getDeviceId()
            val sb = StringBuilder()
            sb.append("AndroidId:  ").append(androidId)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //获取MEID
                val meid = PrivateService.getMEID()
                sb.append("\nMEID:  ").append(meid)
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
            System.out.print(sb.toString())
            tvTest2.setText(sb.toString())
        }
    }


}