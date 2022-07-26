package com.yc.widgetbusiness.chart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yc.appchartview.PieChartView
import com.yc.appchartview.PieChartData
import com.yc.widgetbusiness.R


class PreActivity : AppCompatActivity() {

    private val mFlagsPieView by lazy{
        findViewById<PieChartView>(R.id.pie_chart_now)
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
        val pieDataList: ArrayList<PieChartData> = ArrayList()
        pieDataList.add(
            PieChartData(
                "JetPack",
                0.25.toFloat(),
                "#F83130"
            )
        )
        pieDataList.add(
            PieChartData(
                "Apm性能",
                0.15.toFloat(),
                "#62D54E"
            )
        )
        pieDataList.add(
            PieChartData(
                "Gradle构建",
                0.10.toFloat(),
                "#FEC01D"
            )
        )
        pieDataList.add(
            PieChartData(
                "Kotlin 协程",
                0.30.toFloat(),
                "#FC6E1E"
            )
        )
        pieDataList.add(
            PieChartData(
                "HotFix动态化",
                0.10.toFloat(),
                "#34DFC4"
            )
        )
        pieDataList.add(
            PieChartData(
                "FrameWork流程",
                0.10.toFloat(),
                "#999999"
            )
        )
        mFlagsPieView.setData(pieDataList)
    }
}