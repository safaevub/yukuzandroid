package com.example.utkur.yukuzapp.MainDirectory.CreateOrder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.utkur.yukuzapp.Adapters.UnpickedRequestsAdapter;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.Module.Statics;
import com.example.utkur.yukuzapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 10/29/2017.
 */

public class UnpickedOrdersFragment extends Fragment {

    @BindView(R.id.unpicked_orders_list)
    RecyclerView unpicked_order_list_recyclerView;
    @BindView(R.id.progress_circle)
    ProgressBar progressBar;
    @BindView(R.id.add_order)
    FloatingActionButton add_order;
    public UnpickedRequestsAdapter unpickedRequestsAdapter;
    public static List<PostOrder> postOrders = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        unpickedRequestsAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, null, false);
    }

    @BindView(R.id.no_posts_txt_view)
    public TextView no_post_text_alert;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FillBasics.class);
                getActivity().startActivity(intent);
            }
        });
        unpickedRequestsAdapter = new UnpickedRequestsAdapter(getContext(), postOrders, 1);
        unpicked_order_list_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        unpicked_order_list_recyclerView.setAdapter(unpickedRequestsAdapter);
        if (postOrders.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            refreshList(getContext());
        } else {
            no_post_text_alert.setVisibility(View.GONE);
            unpickedRequestsAdapter.notifyDataSetChanged();
        }
    }

    public void refreshList(Context c) {
        postOrders.clear();
        Ion.with(getContext())
                .load(Statics.URL.REST.get_unpicked_orders)
                .setHeader("Authorization", "Token " + Personal.getToken(c))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result != null) {
                                Log.d("result", "onCompleted: " + result);
                                for (int i = 0; i < result.size(); i++) {
                                    PostOrder order = PostOrder.getOrderByJsonObject(result.get(i).getAsJsonObject(), 1);
                                    postOrders.add(order);
                                    unpickedRequestsAdapter.notifyDataSetChanged();
                                }
                                if (postOrders.isEmpty())
                                    no_post_text_alert.setVisibility(View.VISIBLE);
                                else no_post_text_alert.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            } else
                                Toast.makeText(getContext(), "Something went wrong or check your internet connection", Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(getContext(), e.getMessage() + "-" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
