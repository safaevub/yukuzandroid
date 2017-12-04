package com.example.utkur.yukuzapp.MainDirectory.Pages;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Adapters.DriversRecyclerViewAdapter;
import com.example.utkur.yukuzapp.MainDirectory.AddressSelectorActivity;
import com.example.utkur.yukuzapp.Module.Driver;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.PickedPostOrder;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriversListPickedOrderActivity extends AppCompatActivity {
    @BindView(R.id.driver_list_recycler_view)
    RecyclerView drivers_list_recycler_view;
    DriversRecyclerViewAdapter adapter;
    List<Driver> driverList;
    int[] driver_id;

    @BindView(R.id.single_view_order_title)
    TextView order_title;
    @BindView(R.id.single_view_order_description)
    TextView order_description;
    @BindView(R.id.single_view_order_offer_wight)
    TextView order_weight_offer;
    @BindView(R.id.single_view_order_source_address)
    TextView source_address;
    @BindView(R.id.single_view_order_destination_address)
    TextView destination_address;
    @BindView(R.id.single_view_order_requested_car_type)
    TextView car_type;

    private int order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_list_picked_order);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Picked Drivers");

        driver_id = getIntent().getExtras().getIntArray("drivers");
        order_id = getIntent().getExtras().getInt("id");
        driverList = new ArrayList<>();

        adapter = new DriversRecyclerViewAdapter(driverList, getBaseContext());
        drivers_list_recycler_view.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        drivers_list_recycler_view.setAdapter(adapter);
        initialize();

    }

    private PickedPostOrder order;

    public void initialize() {
        refreshDriversList(getBaseContext(), driver_id);
        Ion.with(getBaseContext())
                .load(Statics.URL.REST.get_order + "&&sid=" + order_id)
                .setHeader("Authorization", "token " + Personal.getToken(getBaseContext()))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        order = new PickedPostOrder(PostOrder.getOrderByJsonObject(result, 1));
                        order_title.setText(order.getTitle());
                        order_description.setText(order.getDescription());
                        String c = result.get("currency").getAsString();
                        order_weight_offer.setText(String.format("%d %s / %d kg", (int) order.getPrice(), c, (int) order.getWeight()));
                        try {
                            source_address.setText(AddressSelectorActivity.getAddress(AddressSelectorActivity.getLatLangByText(order.getSource_address()), getBaseContext()));
                            destination_address.setText(AddressSelectorActivity.getAddress(AddressSelectorActivity.getLatLangByText(order.getDestination_address()), getBaseContext()));
                            car_type.setText(result.get("vehicle").getAsString());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                    }
                });
    }

    private String TAG = "DriversList";

    public void refreshDriversList(Context context, int[] ids) {
        StringBuilder url;
        url = new StringBuilder(Statics.URL.REST.driver_create + "?id=1");
        for (int i = 0; i < ids.length; i++) {
            url.append(("&&d_id=")).append(ids[i]);
        }
        Log.d(TAG, "refreshDriversList: " + url.toString() + " token " + Personal.getToken(getBaseContext()));
        Ion.with(context)
                .load("GET", url.toString())
                .setHeader("Authorization", "Token " + Personal.getToken(getBaseContext()))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
//                            Log.d(TAG, "onCompleted: " + result.getAsString());
                            for (int i = 0; i < result.size(); i++) {
                                Driver d = Driver.getDriverByJsonObject(result.get(i).getAsJsonObject());
                                d.order_id = order_id;
                                driverList.add(d);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception ex) {
//                            Log.d(TAG, "onCompleted: " + e.getMessage());
                            Log.e(TAG, "error on compilation", ex);
                        }
                    }
                });
    }
}
