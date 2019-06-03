package com.anshu.www.gdapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anshu.www.gdapp.model.Mydatamodel;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<Mydatamodel> {
    List<Mydatamodel> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public MyArrayAdapter(Context context, List<Mydatamodel> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public Mydatamodel getItem(int position) {
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

        Mydatamodel item = getItem(position);

        vh.textViewName.setText(item.getName());
        //vh.textViewCountry.setText(item.getCountry());

        return vh.rootView;
    }

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2016-01-05 00:50:26 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final RelativeLayout rootView;

        public final TextView textViewName;
        //public final TextView textViewCountry;

        private ViewHolder(RelativeLayout rootView, TextView textViewName) {
            this.rootView = rootView;
            this.textViewName = textViewName;
            //this.textViewCountry = textViewCountry;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView textViewName = (TextView) rootView.findViewById(R.id.textViewTopic);
//            TextView textViewCountry = (TextView) rootView.findViewById(R.id.textViewCountry);
            return new ViewHolder(rootView, textViewName);
        }
    }
}
