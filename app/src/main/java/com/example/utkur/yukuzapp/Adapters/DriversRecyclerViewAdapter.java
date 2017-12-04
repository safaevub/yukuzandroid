package com.example.utkur.yukuzapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Module.Driver;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 11/29/2017.
 */

public class DriversRecyclerViewAdapter extends RecyclerView.Adapter<DriversRecyclerViewAdapter.ItemHolder> {
    List<Driver> driverList;
    Context context;

    public DriversRecyclerViewAdapter(List<Driver> drivers, Context context) {
        this.context = context;
        this.driverList = drivers;
    }

    @Override
    public DriversRecyclerViewAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemHolder(inflater.inflate(R.layout.drivers_list_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(DriversRecyclerViewAdapter.ItemHolder holder, int position) {
        Driver d = driverList.get(position);
        holder.user_name.setText(d.getFirst_name() + " " + d.getLast_name());
        holder.email.setText(d.getEmail());
        holder.ssn.setText("ssn: " + d.getSsn());
        holder.phone_number.setText(d.getPhone_number());
        holder.driver_rate.setRating(d.getDriver_rate());
        holder.description.setText(d.getDriver_description());
        Picasso.with(context).load(Statics.URL.base_url + d.getImage()).into(holder.image);
        holder.driver_rate.setEnabled(false);
        holder.oder_id = d.order_id;
        holder.driver_id = d.getEmail();
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        TextView email;
        Button accept_btn;
        AppCompatRatingBar driver_rate;
        Button decline_btn;
        CircularImageView image;
        TextView phone_number;
        TextView ssn;
        public TextView description;
        int oder_id;
        String driver_id;

        ItemHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            user_name = itemView.findViewById(R.id.drivers_list_item_user_name);
            email = itemView.findViewById(R.id.drivers_list_item_user_email);
            accept_btn = itemView.findViewById(R.id.drivers_list_item_accept_btn);
            decline_btn = itemView.findViewById(R.id.drivers_list_item_decline_btn);
            decline_btn.setVisibility(View.GONE);
            driver_rate = itemView.findViewById(R.id.drivers_list_item_user_rate);
            image = itemView.findViewById(R.id.drivers_list_item_user_image);
            phone_number = itemView.findViewById(R.id.drivers_list_item_user_number);
            ssn = itemView.findViewById(R.id.drivers_list_item_user_ssn);
            description = itemView.findViewById(R.id.drivers_list_item_user_descr);
            accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: btn accept");
                    JsonObject obj = new JsonObject();
                    obj.addProperty("order_id", oder_id);
                    obj.addProperty("assigned_to", driver_id);
                    Ion.with(itemView.getContext())
                            .load(Statics.URL.REST.accept_request_of_a_driver)
                            .setHeader("Authorization", "Token " + Personal.getToken(itemView.getContext()))
                            .setJsonObjectBody(obj)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    boolean success = result.get("success").getAsBoolean();
                                    if(success){
                                        ((AppCompatActivity)itemView.getContext()).finish();
                                    }
                                    Log.d(TAG, "onCompleted: " + result);
                                }
                            });
                }
            });
        }

        String TAG = "DriverRecyclerViewAdapter";
    }

}
