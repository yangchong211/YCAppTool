package com.yc.netlib.ui;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.yc.netlib.R;
import com.yc.netlib.data.NetworkTraceBean;
import com.yc.netlib.utils.NetWorkUtils;

import java.util.LinkedHashMap;
import java.util.Map;


public class NetworkTraceAdapter extends BaseRecycleAdapter<NetworkTraceBean> {

    public NetworkTraceAdapter(Context context) {
        super(context, R.layout.item_network_trace);
    }

    @Override
    protected void bindData(BaseViewHolder holder, NetworkTraceBean networkTraceBean) {
        final TextView urlTextView = holder.getView(R.id.tv_trace_url);
        NetTraceView traceDetailView = holder.getView(R.id.ntv_trace_detail_view);
        String url = networkTraceBean.getUrl();
        Map<String, Long> networkEventsMap = networkTraceBean.getNetworkEventsMap();
        LinkedHashMap<String, Long> stringLongLinkedHashMap = NetWorkUtils.transformToTraceDetail(networkEventsMap);
        urlTextView.setText(url);
        urlTextView.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = urlTextView.getLineCount();
                if (lineCount>2){
                    urlTextView.setLines(2);
                    urlTextView.setMaxLines(2);
                    urlTextView.setEllipsize(TextUtils.TruncateAt.END);
                }
            }
        });
        traceDetailView.addTraceDetail(stringLongLinkedHashMap);
    }

}
