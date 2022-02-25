package com.yc.catonhelperlib.fps;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.yc.catonhelperlib.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceActivity extends AppCompatActivity {

    private RecyclerView dataShow;
    private RecyclerView detail;
    private PolyLineAdapter adapter;
    private PerformanceDataAdapter performanceDataAdapter;
    private TextView parameter;
    private TextView time;
    private TextView date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fps_info);
        PerformanceManager.getInstance().init(this);

        dataShow = findViewById(R.id.data_show);
        detail = findViewById(R.id.data_detail);
        parameter = findViewById(R.id.parameter);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        
        PolyLineAdapter.Builder builder = new PolyLineAdapter.Builder(
                this, 10);
        dataShow.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL, false));
        builder.setMaxValue(100).setMinValue(0);
        //fps
        (new LoadDataTask()).execute(new String[]{PerformanceManager.getInstance().getFpsFilePath()});
        adapter = builder.build();
        dataShow.setAdapter(adapter);
        adapter.setOnViewClickListener(new PolyLineAdapter.OnViewClickListener() {
            public void onViewClick(int position, PerformanceData data) {
                updateTips(data);
            }
        });


        performanceDataAdapter = new PerformanceDataAdapter(this);
        detail.setLayoutManager(new LinearLayoutManager(this));
        detail.setAdapter(performanceDataAdapter);
        performanceDataAdapter.setOnViewClickListener(new PerformanceDataAdapter.OnViewClickListener() {
            @Override
            public void onViewClick(View var1, PerformanceData data) {
                updateTips(data);
            }
        });
        
        PerformanceManager.getInstance().startMonitorFrameInfo();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        PerformanceManager.getInstance().destroy();
    }


    private void updateTips(PerformanceData data) {
        parameter.setText(String.valueOf(data.parameter));
        time.setText(data.time);
        date.setText(data.date);
    }

    private class LoadDataTask extends AsyncTask<String, Integer, List<PerformanceData>> {

        private LoadDataTask() {

        }

        protected void onPostExecute(List<PerformanceData> result) {
            performanceDataAdapter.append(result);
            adapter.setData(result);
            if (result.size() > 1) {
                updateTips((PerformanceData)result.get(1));
            }
        }

        protected List doInBackground(String... strings) {
            File file = new File(strings[0]);
            ArrayList<PerformanceData> datas = new ArrayList();
            if (file.exists()) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String tempString = null;

                    while((tempString = reader.readLine()) != null) {
                        String[] split = tempString.split(" ");
                        datas.add(new PerformanceData(split[1], split[2], Float.valueOf(split[0])));
                    }

                    reader.close();
                } catch (Exception var15) {
                    var15.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException var14) {
                            var14.printStackTrace();
                        }
                    }

                }
            }

            return datas;
        }
    }

}
