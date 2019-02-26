package com.ycbjie.android.view.adapter


import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.Utils
import com.ycbjie.android.R
import com.ycbjie.android.model.bean.HomeData
import com.ycbjie.android.util.AndroidUtils
import com.ycbjie.library.utils.spannable.RoundBackgroundSpan
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder


class AndroidCollectAdapter : RecyclerArrayAdapter<HomeData>{


    constructor(activity: Activity?) : super(activity)

    override fun OnCreateViewHolder(parent: ViewGroup, viewType: Int):
            BaseViewHolder<HomeData> {
        return ViewHolder(parent)
    }


    private inner class ViewHolder internal constructor(parent: ViewGroup) :
            BaseViewHolder<HomeData>(parent, R.layout.item_android_home_news) {

        internal var ttIvHead: ImageView = getView(R.id.ttIvHead)
        internal var ttTvName: TextView = getView(R.id.ttTvName)
        internal var ivMore: ImageView = getView(R.id.ivMore)
        internal var flLike: FrameLayout = getView(R.id.flLike)
        internal var ivLike: ImageView = getView(R.id.ivLike)
        internal var tvContent: TextView = getView(R.id.tvContent)
        internal var tvTagTitle: TextView = getView(R.id.tvTagTitle)
        internal var tvSuperChapterName: TextView = getView(R.id.tvSuperChapterName)
        internal var tvLip: TextView = getView(R.id.tvLip)
        internal var tvChildChapterName: TextView = getView(R.id.tvChildChapterName)
        internal var tvTime: TextView = getView(R.id.tvTime)

        init {
            addOnClickListener(R.id.ivMore)
            addOnClickListener(R.id.flLike)
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun setData(data: HomeData?) {
            super.setData(data)
            val homeData: HomeData = data as HomeData
            ttTvName.text = homeData.author
            tvTime.text = homeData.niceDate
            tvSuperChapterName.text = homeData.superChapterName
            tvChildChapterName.text = homeData.chapterName
            tvContent.text = homeData.title
            val collect = homeData.collect
            //是否收藏
            if (collect) {
                ivLike.setBackgroundColor(Color.RED)
            } else {
                ivLike.setBackgroundColor(Color.GRAY)
            }


            if (adapterPosition==0 || adapterPosition==1){
                val text1 = "杨充"
                val text2 = "潇湘剑雨"
                val titleContent = text1 + text2 + homeData.title
                val titleSpannable = SpannableString(titleContent)

                // 杨充
                val exclusiveSpannable = RoundBackgroundSpan(
                        Color.parseColor("#f25057"),
                        Color.parseColor("#f25057"),
                        Color.parseColor("#ffffff"),
                        AndroidUtils.SingleObject.getApp().
                                resources.getDimensionPixelOffset(R.dimen.dp1),
                        Utils.getApp().resources.getDimensionPixelOffset(R.dimen.dp4))
                spannable(titleSpannable, exclusiveSpannable, 0, text1.length)

                // text2 不等于空 在绘制text
                if (!TextUtils.isEmpty(text2)) {
                    val bgColor = Color.parseColor("#ffffff")
                    val borderColor = Color.parseColor("#ff3d51")
                    val textColor = Color.parseColor("#f25057")
                    val spannable = RoundBackgroundSpan(bgColor, borderColor, textColor,
                            Utils.getApp().resources.getDimensionPixelOffset(R.dimen.dp1),
                            Utils.getApp().resources.getDimensionPixelOffset(R.dimen.dp4))
                    spannable(titleSpannable, spannable, 2, 2 + text2.length)
                }

                tvContent.text = titleSpannable
            }else{
                tvContent.text = homeData.title
            }
        }
    }

    private fun spannable(titleSpannable: SpannableString, spannable: RoundBackgroundSpan,
                          start: Int, end: Int) {
        titleSpannable.setSpan(spannable, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        titleSpannable.setSpan(AbsoluteSizeSpan(Utils.getApp().resources.getDimensionPixelSize(
                R.dimen.textSize9)), start,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

}

