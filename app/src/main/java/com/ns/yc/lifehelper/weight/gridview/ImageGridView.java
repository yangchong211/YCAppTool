package com.ns.yc.lifehelper.weight.gridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.utils.image.ImageUtils;

import java.util.ArrayList;
import java.util.List;


public class ImageGridView extends GridView {


    public ImageGridView(Context context) {
        this(context, null);
    }

    public ImageGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVerticalSpacing(context.getResources().getDimensionPixelOffset(R.dimen.dp5));
        setHorizontalSpacing(context.getResources().getDimensionPixelOffset(R.dimen.dp5));
    }

    /**
     * 解决GridView与嵌套RecyclerView，ScrollView的冲突
     * 只显示一行：自定义一个gridView重新绘制item高度
     * @param widthMeasureSpec              width
     * @param heightMeasureSpec             height
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void setUri(ArrayList<String> list , OnItemClickListener listener){
        List<String> strings = null;
        if(list!=null && list.size()>0){
            setVisibility(VISIBLE);
            setNumColumns(3);
            if(list.size()>=9){
                //只保留前面9张图片
                strings = list.subList(0, 8);
                LogUtils.e("图片"+strings.size());
            }else {
                strings = list;
                LogUtils.e("图片----"+strings.size());
            }
        }else {
            setVisibility(GONE);
        }
        ImageGridAdapter adapter = new ImageGridAdapter(strings);
        setAdapter(adapter);
        setOnItemClickListener(listener);
    }


    private class ImageGridAdapter extends BaseAdapter{

        private List<String> mList;
        private int width;

        ImageGridAdapter(List<String> list) {
            this.mList = list;
            width = ScreenUtils.getScreenWidth()/3;
        }

        @Override
        public int getCount() {
            return mList==null ? 0 : mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressWarnings("SuspiciousNameCombination")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_grid_image, parent, false);
                viewHolder.mIvImg = (ImageView) convertView.findViewById(R.id.iv_image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String url = getItem(position);
            ViewGroup.LayoutParams layoutParams = viewHolder.mIvImg.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;
            viewHolder.mIvImg.setLayoutParams(layoutParams);
            if(position==8){
                //这个地方可以放张查看更多的图片
                ImageUtils.loadImgByPicasso(viewHolder.mIvImg.getContext(), "",R.drawable.image_default,viewHolder.mIvImg);
            }else {
                ImageUtils.loadImgByPicasso(viewHolder.mIvImg.getContext(), url,R.drawable.image_default,viewHolder.mIvImg);
            }
            return convertView;
        }
    }

    class ViewHolder {
        ImageView mIvImg;
    }

}
