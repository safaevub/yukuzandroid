package com.example.utkur.yukuzapp.MainDirectory.CreateOrder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.utkur.yukuzapp.Adapters.PickedOrdersAdapter;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.PickedPostOrder;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 11/26/2017.
 */

public class PickedOrdersFragment extends Fragment {
    @BindView(R.id.unpicked_orders_list)
    RecyclerView unpicked_order_list_recyclerView;
    public PickedOrdersAdapter pickedOrdersAdapter;
    @BindView(R.id.progress_circle)
    ProgressBar progressBar;
    @BindView(R.id.add_order)
    FloatingActionButton add_order;
    @BindView(R.id.no_posts_txt_view)
    TextView alert_no_post;
    public static List<PickedPostOrder> pickedOrders = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        refreshPickedOrdersList(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        add_order.setVisibility(View.GONE);
        pickedOrdersAdapter = new PickedOrdersAdapter(getContext(), pickedOrders);
        unpicked_order_list_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        unpicked_order_list_recyclerView.setAdapter(pickedOrdersAdapter);
        refreshPickedOrdersList(getContext());
    }

    public void refreshPickedOrdersList(Context context) {
        Ion.with(context)
                .load("GET", Statics.URL.REST.picked_orders_for_person)
                .setHeader("Authorization", "Token " + Personal.getToken(getContext()))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        pickedOrders.clear();
                        PickedPostOrder.getPickedPostOrderByJsonArray(result, pickedOrders, pickedOrdersAdapter);
                        try {
                            if (alert_no_post != null && pickedOrders != null)
                                if (pickedOrders.size() == 0)
                                    alert_no_post.setText(View.VISIBLE);
                                else alert_no_post.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            Log.e(TAG, "onCompleted: ", ex);
                        }
                        pickedOrdersAdapter.notifyDataSetChanged();

                    }
                });

    }

    private String TAG = "PickedOrdersFragment";
}
