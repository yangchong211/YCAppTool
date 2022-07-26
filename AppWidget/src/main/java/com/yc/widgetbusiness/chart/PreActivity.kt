package com.yc.widgetbusiness.chart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yc.appchartview.PieChartLayout
import com.yc.appchartview.PieChartView
import com.yc.appchartview.PieFlagData
import com.yc.appchartview.PieFlagView
import com.yc.widgetbusiness.R


class PreActivity : AppCompatActivity() {

    private val mFlagsPieView by lazy{
        findViewById<PieFlagView>(R.id.pie_chart_now)
    }
    private val mPieChartView by lazy{
        findViewById<PieChartLayout>(R.id.pie_chart_layout)
    }


    companion object{
        fun startActivity(context: Context) {
            try {
                val target = Intent()
                target.setClass(context, PreActivity::class.java)
                context.startActivity(target)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_view)
        initData()
    }


    private fun initData() {
        val pieDataList: ArrayList<PieFlagData> = ArrayList()
        pieDataList.add(
            PieFlagData(
                "JetPack",
                0.25.toFloat(),
                "#F83130"
            )
        )
        pieDataList.add(
            PieFlagData(
                "Apm性能",
                0.15.toFloat(),
                "#62D54E"
            )
        )
        pieDataList.add(
            PieFlagData(
                "Gradle构建",
                0.10.toFloat(),
                "#FEC01D"
            )
        )
        pieDataList.add(
            PieFlagData(
                "Kotlin 协程",
                0.30.toFloat(),
                "#FC6E1E"
            )
        )
        pieDataList.add(
            PieFlagData(
                "HotFix动态化",
                0.10.toFloat(),
                "#34DFC4"
            )
        )
        pieDataList.add(
            PieFlagData(
                "FrameWork流程",
                0.10.toFloat(),
                "#999999"
            )
        )
        mFlagsPieView.setData(pieDataList)

        mPieChartView.setData(pieDataList)
    }
}