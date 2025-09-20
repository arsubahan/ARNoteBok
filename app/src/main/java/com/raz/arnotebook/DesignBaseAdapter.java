package com.raz.arnotebook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class DesignBaseAdapter extends BaseAdapter {

    private final List<NoteBkModel> list;
    private final Context context;

    public DesignBaseAdapter(List<NoteBkModel> list, Context context) {
        this.list = list;
        this.context = context;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);

            TextView viewtitle = convertView.findViewById(R.id.item_lstittle);
            TextView viewsecind = convertView.findViewById(R.id.item_lstsecind);

            viewtitle.setText(list.get(position).getTitle());
            viewsecind.setText(list.get(position).getSecind());

            if (Objects.equals(list.get(position).getSecind(), "1")) {
                viewtitle.setBackgroundColor(Color.parseColor("#800000"));
                viewtitle.setTextColor(Color.YELLOW);
            }
            if (Objects.equals(list.get(position).getSecind(), "0")) {
                viewtitle.setBackgroundColor(Color.LTGRAY);
                viewtitle.setTextColor(Color.BLACK);
            }


        }
        return convertView;
    }
}