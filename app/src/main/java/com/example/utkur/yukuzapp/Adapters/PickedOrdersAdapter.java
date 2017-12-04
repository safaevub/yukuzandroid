package com.example.utkur.yukuzapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.utkur.yukuzapp.MainDirectory.Pages.DriversListPickedOrderActivity;
import com.example.utkur.yukuzapp.Module.PickedPostOrder;
import com.example.utkur.yukuzapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 11/26/2017.
 */

public class PickedOrdersAdapter extends RecyclerView.Adapter<PickedOrdersAdapter.ItemHolder> {
    private List<PickedPostOrder> picked_orders;
    private Context context;

    public PickedOrdersAdapter(Context context, List<PickedPostOrder> orders) {
        picked_orders = orders;
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PickedOrdersAdapter.ItemHolder(inflater.inflate(R.layout.picked_order_list_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        PickedPostOrder order = picked_orders.get(position);
        holder.picked_order_list_item_title.setText(order.getTitle() + " -> " + order.number_of_driver_who_took_order);
        holder.picked_order_list_item_picked_by.setText(order.getDescription());
        holder.picked_order_list_item_time.setText(order.getTime());
        holder.order = order;
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
        CardView order_list_item_parent;
        PickedPostOrder order;

        public ItemHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            order_list_item_parent = itemView.findViewById(R.id.order_list_item_parent);
            order_list_item_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //take drivers
                    Intent intent = new Intent(itemView.getContext(), DriversListPickedOrderActivity.class);
                    intent.putExtra("drivers", order.driver_array_list);
                    intent.putExtra("id", order.getOrder_id());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
