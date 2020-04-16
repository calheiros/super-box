package com.jefferson.superbox.br.adapter;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.bumptech.glide.*;
import com.jefferson.superbox.br.*;
import com.jefferson.superbox.br.model.*;
import java.util.*;

public class FilePickerAdapter extends BaseAdapter {
	
    private Context context;
    private LayoutInflater mLayoutInflater;
    public List<PickerModel> models;
    private int selected_item = -1;

    public FilePickerAdapter(List<PickerModel> list, Context context) {

        this.context = context;
        this.models = list;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public void update(List<PickerModel> list) {

        this.models = list;
        notifyDataSetChanged();
    }

    public int getSelectedItem() {

        return this.selected_item;
    }

    public void setSelectedItem(int i) {

        this.selected_item = i;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return this.models.size();
    }

    @Override
    public Object getItem(int i) {

        return i;
    }

    @Override
    public long getItemId(int i) {

        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.file_picker_item, (ViewGroup) null);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.tumbView);
        TextView textView = (TextView) view.findViewById(R.id.item_name);
        TextView textView2 = (TextView) view.findViewById(R.id.item_size);
        PickerModel pickerModel = models.get(i);
        view.setBackgroundColor(i == selected_item ? Color.parseColor("#1c000000") : Color.parseColor("#00f0f0f0"));
        textView.setText(pickerModel.getName());
        textView2.setText(String.valueOf(pickerModel.getSize()));
        Glide.with(context).load("file://" + pickerModel.getTumbPath()).centerCrop().into(imageView);
        return view;
    }
}
