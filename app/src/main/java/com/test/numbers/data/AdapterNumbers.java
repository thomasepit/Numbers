package com.test.numbers.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.test.numbers.R;
import com.test.numbers.data.DataClass;

import java.util.List;

public class AdapterNumbers extends BaseAdapter {
    private Context context;
    private List<DataClass> primary_list;

    public AdapterNumbers(Context context, List<DataClass> list) {
        this.context = context;
        this.primary_list = list;
    }

    @Override
    public int getCount() {
        return primary_list.size();
    }

    @Override
    public Object getItem(int position) {
        return primary_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return primary_list.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView txt_number;
        ImageView image;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater _inflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        DataClass movie = primary_list.get(position);

        if (convertView == null) {
            convertView = _inflator.inflate(R.layout.list_layout_numbers, null);

            holder = new ViewHolder();
            holder.txt_number = (TextView) convertView.findViewById(R.id.txt_number);
            holder.image = (ImageView) convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_number.setText(movie.getName());
        Picasso.get().load(primary_list.get(position).getImage()).into(holder.image);

        return convertView;
    }
}