package com.example.utkur.yukuzapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.utkur.yukuzapp.External.DoAction;
import com.example.utkur.yukuzapp.MainDirectory.ViewEditOrderActivity;
import com.example.utkur.yukuzapp.Module.PickedPostOrder;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Muhammadjon on 11/2/2017.
 */

public class UnpickedRequestsAdapter extends RecyclerView.Adapter<UnpickedRequestsAdapter.ItemHolder> {
    private List<PostOrder> objects;
    private List<PickedPostOrder> picked_objects;
    private Context context;
    private int purpose;

    public UnpickedRequestsAdapter(List<PickedPostOrder> items, Context context, int purpose) {
        this.purpose = purpose;
        this.picked_objects = items;
        this.context = context;
    }

    public UnpickedRequestsAdapter(Context context, List<PostOrder> items, int purpose) {
        this.purpose = purpose;
        this.objects = items;
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (purpose) {
            case 1:
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new ItemHolder(inflater.inflate(R.layout.unpicked_order_list_item, parent, false));
            default:
                return null;
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ItemHolder holder, @SuppressLint("RecyclerView") int position) {
        switch (purpose) {
            case 1:
                PostOrder order = (PostOrder) objects.get(position);
                holder.order_title.setText(order.getTitle());
                holder.order_descr.setText(order.getDescription());
                holder.order_time.setText(DoAction.convertDateTimeToFormat("", "", order.getTime()));
                holder.id = order.getId();
                holder.pos = position;
                Log.d(TAG, "onBindViewHolder: " + order.isIs_cancelled());
                if (order.isIs_cancelled()) {
                    holder.is_alive.setText("alive");
                    holder.is_alive.setTextColor(ContextCompat.getColor(context, R.color.dark_green));
                } else {
                    holder.is_alive.setText("cancelled");
                    holder.is_alive.setTextColor(ContextCompat.getColor(context, R.color.dark_red));
                }
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (purpose) {
            case 1:
                return objects.size();
            case 2:
                return picked_objects.size();
            default:
                return 0;
        }
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.order_title)
        TextView order_title;
        @BindView(R.id.order_descr)
        TextView order_descr;
        @BindView(R.id.order_time)
        TextView order_time;
        @BindView(R.id.order_list_item_parent)
        CardView parent_layout;
        @BindView(R.id.order_status)
        TextView is_alive;
        int id = 0;
        int pos = 0;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ViewEditOrderActivity.class);
                    i.putExtra("id", id);
                    i.putExtra("pos", pos);
                    context.startActivity(i);
                }
            });
        }
    }
}
