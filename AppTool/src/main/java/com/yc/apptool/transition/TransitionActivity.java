package com.yc.apptool.transition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.apptool.R;
import com.yc.transition.TransitionParam;
import com.yc.transition.TransitionUtils;


public class TransitionActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_recycler_view);
        setupView();
    }

    private void setupView() {
        mRecycleView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecycleView.setAdapter(new VideoPlayAdapter());
    }

    public static class VideoPlayAdapter extends RecyclerView.Adapter<VideoPlayViewHolder> {

        @Override
        public VideoPlayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base_easy_image, null);
            return new VideoPlayViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(VideoPlayViewHolder holder, int position) {
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TransitionParam animBean = TransitionUtils.getSourceViewParam(view);
                    VideoPlayActivity.intentStart(view.getContext(), animBean);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 17;
        }
    }

    public static class VideoPlayViewHolder extends RecyclerView.ViewHolder {

        public View rootView;

        public VideoPlayViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
        }

    }
}
