package com.ypcxpt.fish.main.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ypcxpt.fish.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win7 on 2017-06-07.
 */

public class VersionUpdateAdapter extends BaseAdapter {

    private Context context;
    private String str;
    private List<String> data;
    private int RowCount=0;

    public VersionUpdateAdapter(Context context, String data) {
        this.context = context;
        this.str = data;
        Log.e("VersionUpdateAdapter", "版本更新描述：" + data);
        if (str.length() != 0) {
            String[] strs = str.split("\\#");
            this.data = new ArrayList<>();
            for (int i = 0; i < strs.length; i++) {
                this.data.add(strs[i]);
            }
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_versiontext, null);
            viewHolder.tv_text = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_text.setText(data.get(position));

        return convertView;
    }

    public  int getRowCount(){
        if(data!=null)
            for (int i = 0; i < data.size(); i++) {
                if(data.get(i).length()>20)
                    RowCount++;
                RowCount++;
            }
        return RowCount;
    }

    class ViewHolder {
        public TextView tv_text;
    }
}
