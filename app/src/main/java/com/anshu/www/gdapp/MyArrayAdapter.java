package com.anshu.www.gdapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anshu.www.gdapp.model.MyDataModel;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<MyDataModel> {
    private List<MyDataModel> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    MyArrayAdapter(Context context, List<MyDataModel> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public MyDataModel getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.topic_layout, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        MyDataModel item = getItem(position);

        vh.textViewName.setText(item.getDailyTopic());
        //vh.textViewCountry.setText(item.getCountry());

        return vh.rootView;
    }

    private static class ViewHolder {
        final RelativeLayout rootView;

        final TextView textViewName;


        private ViewHolder(RelativeLayout rootView, TextView textViewName) {
            this.rootView = rootView;
            this.textViewName = textViewName;

        }

        static ViewHolder create(RelativeLayout rootView) {
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewTopic);
            return new ViewHolder(rootView, textViewName);
        }
    }
}
