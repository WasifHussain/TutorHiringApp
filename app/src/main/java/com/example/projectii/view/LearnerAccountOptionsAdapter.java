package com.example.projectii.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.projectii.R;

public class LearnerAccountOptionsAdapter extends BaseAdapter {
    Context context;
    String option[];
    int icon[];
    LayoutInflater inflater;

    public LearnerAccountOptionsAdapter (Context applicationContext, String[] options, int[] icons) {
        context = applicationContext;
        option = options;
        icon = icons;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return option.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fragment_learner_account_option, null);
        TextView tv = (TextView)  view.findViewById(R.id.option);
        ImageView img = (ImageView) view.findViewById(R.id.icon);
        tv.setText(option[i]);
        img.setImageResource(icon[i]);
        return view;
    }
}
