package com.example.utkur.yukuzapp.TestCases.RecyclerViewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Module.Request;

import java.util.List;

/**
 * Created by Muhammadjon on 10/24/2017.
 */

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.RequestViewHolder> {
    private List<Request> items;
    private Context context;
    private int layout;

    public ItemsRecyclerViewAdapter(List<Request> items, Context context, int layout) {
        this.items = items;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public void onBindViewHolder(RequestViewHolder holder, int position) {

    }

    @Override
    public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Request> getItems() {
        return items;
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {
        String title;
        String description;
        String time;

        public RequestViewHolder(View itemView) {
            super(itemView);
        }

    }
}
