package com.example.utkur.yukuzapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Module.PickedPostOrder;
import com.example.utkur.yukuzapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 11/26/2017.
 */

public class PickedRequestsAdapter extends RecyclerView.Adapter<PickedRequestsAdapter.ItemHolder> {
    private List<PickedPostOrder> picked_orders;
    private Context context;

    public PickedRequestsAdapter(Context context, List<PickedPostOrder> orders) {
        picked_orders = orders;
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PickedRequestsAdapter.ItemHolder(inflater.inflate(R.layout.picked_order_list_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        PickedPostOrder order = picked_orders.get(position);
        holder.picked_order_list_item_title.setText(order.getTitle());
        holder.picked_order_list_item_picked_by.setText("picked by: " + order.getPicked_by().getFirst_name() + " " + order.getPicked_by().getLast_name());
        holder.picked_order_list_item_time.setText(order.getTime());
    }

    @Override
    public int getItemCount() {
        return picked_orders.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.picked_order_list_item_title_id)
        TextView picked_order_list_item_title;
        @BindView(R.id.picked_order_list_item_picked_by_id)
        TextView picked_order_list_item_picked_by;
        @BindView(R.id.picked_order_list_item_time_id)
        TextView picked_order_list_item_time;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
