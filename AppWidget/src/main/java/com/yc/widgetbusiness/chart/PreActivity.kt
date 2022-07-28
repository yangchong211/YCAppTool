package com.yc.widgetbusiness.chart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yc.appchartview.PieChartView
import com.yc.appchartview.PieEasyLayout
import com.yc.appchartview.PieFlagData
import com.yc.appchartview.PieFlagView
import com.yc.widgetbusiness.R


class PreActivity : AppCompatActivity() {

    private val mFlagsPieView by lazy{
        findViewById<PieFlagView>(R.id.pie_chart_now)
    }
    private val mPieChartView by lazy{
        findViewById<PieEasyLayout>(R.id.pie_chart_layout)
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
                0.70.toFloat(),
                "#9187FE"
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
                0.toFloat(),
                "#FC6E1E"
            )
        )
        mFlagsPieView.setData(pieDataList)

        mPieChartView.setData(pieDataList)
    }
}