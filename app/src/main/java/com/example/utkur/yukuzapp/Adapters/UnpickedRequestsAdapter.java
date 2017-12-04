package com.example.utkur.yukuzapp.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utkur.yukuzapp.External.DoAction;
import com.example.utkur.yukuzapp.MainDirectory.AddressSelectorActivity;
import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver.DriverRelatedOrdersListFragment;
import com.example.utkur.yukuzapp.MainDirectory.ViewEditOrderActivity;
import com.example.utkur.yukuzapp.Module.Driver;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.PickedPostOrder;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Muhammadjon on 11/2/2017.
 */

public class UnpickedRequestsAdapter extends RecyclerView.Adapter<UnpickedRequestsAdapter.ItemHolder> {
    private List<PostOrder> post_order_list;
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
        this.post_order_list = items;
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (purpose == 1) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ItemHolder(inflater.inflate(R.layout.unpicked_order_list_item, parent, false));
        } else if (purpose == 3) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ItemHolder(inflater.inflate(R.layout.request_order_list_item, parent, false));
        } else if (purpose == 2) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ItemHolder(inflater.inflate(R.layout.request_order_list_item, parent, false));
        }
        return null;
    }


    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ItemHolder holder, @SuppressLint("RecyclerView") int position) {
        PostOrder order = post_order_list.get(position);
        holder.id = order.getId();
        holder.pos = position;
        if (purpose == 1) {
            holder.order_title.setText(order.getTitle());
            holder.order_descr.setText(order.getDescription());
            try {
                holder.order_time.setText(DoAction.convertDateTimeToFormat("", "", order.getTime()));
            } catch (Exception e) {
                holder.order_time.setText(order.getTime());
            }
            Log.d(TAG, "onBindViewHolder: " + order.isIs_cancelled());
            if (!order.isIs_cancelled()) {
                holder.is_alive.setText("alive");
                holder.is_alive.setTextColor(ContextCompat.getColor(context, R.color.dark_green));
            } else {
                holder.is_alive.setText("cancelled");
                holder.is_alive.setTextColor(ContextCompat.getColor(context, R.color.dark_red));
            }
        } else if (purpose == 3 || purpose == 2) {
            holder.order_title.setText(order.getTitle());
            holder.person.setText(order.getRequester_name());
            holder.order_descr.setText(order.getDescription());
            holder.person_id = order.getPosted_by();
            holder.weight.setText(order.getWeight() + " kg");
            holder.deadline.setText(order.getDeadline());
            try {
                holder.from_add.setText(AddressSelectorActivity.getAddress(AddressSelectorActivity.getLatLangByText(order.getSource_address()), context));
                holder.to_add.setText(AddressSelectorActivity.getAddress(AddressSelectorActivity.getLatLangByText(order.getDestination_address()), context));
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.id = order.getId();
            holder.pos = position;
        }
    }

    @Override
    public int getItemCount() {
        switch (purpose) {
            case 1:
                return post_order_list.size();
            case 3:
                return post_order_list.size();
            case 2:
                return post_order_list.size();
            default:
                return 0;
        }
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView order_title;
        private TextView order_descr;
        private TextView order_time;
        private CardView parent_layout;
        private TextView is_alive;
        int id = 0;
        int pos = 0;

        ItemHolder(final View itemView) {
            super(itemView);
            if (purpose == 1) {
                order_title = itemView.findViewById(R.id.order_title);
                order_descr = itemView.findViewById(R.id.order_descr);
                order_time = itemView.findViewById(R.id.order_time);
                parent_layout = itemView.findViewById(R.id.order_list_item_parent);
                is_alive = itemView.findViewById(R.id.order_status);
                parent_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, ViewEditOrderActivity.class);
                        i.putExtra("id", id);
                        i.putExtra("pos", pos);
                        i.putExtra("purpose", 1);
                        context.startActivity(i);
                    }
                });
            } else if (purpose == 3 || purpose == 2) {
                order_title = itemView.findViewById(R.id.request_order_list_item_title_id);
                order_descr = itemView.findViewById(R.id.request_order_list_item_descr);

                person = itemView.findViewById(R.id.request_order_list_item_picked_by_id);
                weight = itemView.findViewById(R.id.request_order_list_item_weight);
                deadline = itemView.findViewById(R.id.request_order_list_item_deadline);
                from_add = itemView.findViewById(R.id.request_order_list_item_add1);
                to_add = itemView.findViewById(R.id.request_order_list_item_add2);
                btn_pick_order = itemView.findViewById(R.id.request_order_list_item_btn_pick_order);
                parent_layout = itemView.findViewById(R.id.order_list_item_parent);
                if (purpose == 3)
                    btn_pick_order.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                            builder.setTitle("picking order " + order_title.getText().toString())
                                    .setMessage("If you take this order " + person.getText().toString() + " will be informed !")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Toast.makeText(context, order_title.getText().toString() + " > ", Toast.LENGTH_SHORT).show();
                                            JsonObject obj = new JsonObject();
                                            obj.addProperty("order", id);
                                            Ion.with(context)
                                                    .load("POST", Statics.URL.REST.pick_order)
                                                    .setHeader("Authorization", "Token " + Personal.getToken(context))
                                                    .setJsonObjectBody(obj)
                                                    .asJsonObject()
                                                    .setCallback(new FutureCallback<JsonObject>() {
                                                        @Override
                                                        public void onCompleted(Exception e, JsonObject result) {
                                                            if (result != null) {
                                                                Toast.makeText(context, "" + result.get("success").getAsString(), Toast.LENGTH_SHORT).show();
                                                                DriverRelatedOrdersListFragment.driver_related_posts.remove(pos);
                                                                DriverRelatedOrdersListFragment.unpickedDriverRequestsAdapter.notifyDataSetChanged();
                                                            }
                                                        }
                                                    });
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                else btn_pick_order.setVisibility(View.GONE);
            }
        }

        int person_id;
        TextView person;
        TextView weight;
        TextView deadline;
        TextView from_add;
        TextView to_add;
        Button btn_pick_order;
    }
}
