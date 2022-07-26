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
    private val mPieChart by lazy{
        findViewById<PieChartView>(R.id.pie_chart_two)
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
                0.40.toFloat(),
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
                0.50.toFloat(),
                "#FC6E1E"
            )
        )
        mFlagsPieView.setData(pieDataList)

        mPieChartView.setData(pieDataList)


        mPieChart.addItemType(PieChartView.ItemType("苹果", 25, -0xdf4d56))
        mPieChart.addItemType(PieChartView.ItemType("华为", 17, -0x97dd75))
        mPieChart.addItemType(PieChartView.ItemType("小米", 13, -0x74a600))
        mPieChart.addItemType(PieChartView.ItemType("三星", 8, -0x32c900))
        mPieChart.addItemType(PieChartView.ItemType("OPPO", 6, -0x769733))
        mPieChart.addItemType(PieChartView.ItemType("VIVO", 5, -0xbc8ebb))
        mPieChart.addItemType(PieChartView.ItemType("其他品牌", 20, -0x666667))
    }
}