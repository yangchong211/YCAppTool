package com.yc.blesample.chat.base;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yc.blesample.R;

import java.util.List;

public class BleAdapter extends BaseAdapter {

    private Context context;

    private List<BluetoothDevice> list;

    public BleAdapter(Context context, List<BluetoothDevice> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_ble, null, false);
            holder.tvName = convertView.findViewById(R.id.tvBle);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        BluetoothDevice device = list.get(position);

        if (device.getName() == null) {
            holder.tvName.setText(device.getAddress());
        } else {
            holder.tvName.setText(device.getName() + ":" + device.getAddress());

        }


        return convertView;
    }

    class Holder {
        TextView tvName;
    }
}
