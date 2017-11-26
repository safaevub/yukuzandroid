package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.utkur.yukuzapp.Adapters.UnpickedRequestsAdapter;
import com.example.utkur.yukuzapp.Module.Personal;
import com.example.utkur.yukuzapp.Module.PostOrder;
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

public class DriverRelatedOrdersListFragment extends Fragment {
    @BindView(R.id.unpicked_orders_list)
    RecyclerView unpicked_order_list_recyclerView;
    @BindView(R.id.progress_circle)
    ProgressBar progressBar;
    @BindView(R.id.add_order)
    FloatingActionButton add_order;
    public UnpickedRequestsAdapter unpickedDriverRequestsAdapter;
    public static List<PostOrder> driver_related_posts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        add_order.setVisibility(View.GONE);
        unpickedDriverRequestsAdapter = new UnpickedRequestsAdapter(getContext(), driver_related_posts, 3);
        unpicked_order_list_recyclerView.setAdapter(unpickedDriverRequestsAdapter);
        refreshDriverRelatedOrderList(getContext());
    }

    public void refreshDriverRelatedOrderList(Context context) {
        String token = Personal.getToken(context);
        Ion.with(context).load("GET", Statics.URL.REST.get_unpicked_driver_related_orders)
                .setHeader("Authorization", "Token " + token)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        for (int i = 0; i < result.size(); i++) {
                            PostOrder order = PostOrder.getOrderByJsonObject(result.get(i).getAsJsonObject(), 2);
                            driver_related_posts.add(order);
                            unpickedDriverRequestsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
