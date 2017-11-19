package com.example.utkur.yukuzapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Module.Car;
import com.example.utkur.yukuzapp.R;

import java.util.List;

/**
 * Created by Muhammadjon on 11/19/2017.
 */

public class CarsArrayAdapter extends ArrayAdapter<Car> {
    public CarsArrayAdapter(@NonNull Context context, int resource, @NonNull List<Car> objects) {
        super(context, resource, objects);
    }

    private ViewHolder viewHolder;

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.cars_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.car_list_item_title);
            viewHolder.descr = (TextView) convertView.findViewById(R.id.car_list_item_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Car item = getItem(position);
        if (item != null) {
            viewHolder.title.setText(String.format("%s - %s", item.getTitle(), item.getNumber()));
            viewHolder.descr.setText(String.format("the car can carry baggage between %d kg and %d kg", item.getMin_kg(), item.getMax_kg()));
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView title;
        private TextView descr;
    }
}
