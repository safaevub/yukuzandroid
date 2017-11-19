package com.example.utkur.yukuzapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Module.CurrencyType;
import com.example.utkur.yukuzapp.Module.VehicleType;
import com.example.utkur.yukuzapp.R;

import java.util.List;

/**
 * Created by Muhammadjon on 11/5/2017.
 */

public class CustomAdapter extends ArrayAdapter<Object> {
    private List<CurrencyType> currencyTypeList;
    private List<VehicleType> vehicleTypeList;
    private int for_type = -1;

    public CustomAdapter(@NonNull Context context, @NonNull List objects, int type) {
        super(context, R.layout.support_simple_spinner_dropdown_item, objects);
        switch (type) {
            case 1:
                currencyTypeList = (List<CurrencyType>) objects;
                break;
            case 2:
                vehicleTypeList = (List<VehicleType>) objects;
                break;

        }
        for_type = type;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        switch (for_type) {
            case 1:
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
                }
                ViewHolder holder = new ViewHolder(convertView);
                String title = ((CurrencyType) getItem(position)).getTitle();
                holder.id = ((CurrencyType) getItem(position)).getId();
                holder.setTitle(title);
                return convertView;
            case 2:
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
                }
                holder = new ViewHolder(convertView);
                title = ((VehicleType) getItem(position)).getTitle();
                holder.setTitle(title);
                holder.id = ((VehicleType) getItem(position)).getId();
                return convertView;
        }
        return null;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        switch (for_type) {
            case 1:
                return (Object) currencyTypeList.get(position);
            case 2:
                return (Object) vehicleTypeList.get(position);
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        switch (for_type) {
            case 1:
                return currencyTypeList.size();
            case 2:
                return vehicleTypeList.size();
            default:
                return 0;
        }
    }

    class ViewHolder {
        TextView text;
        int id;

        public ViewHolder(View view) {
            text = view.findViewById(android.R.id.text1);
        }

        public void setTitle(String title) {
            text.setText(title);
        }
    }
}
