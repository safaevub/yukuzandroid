package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver;

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
import android.widget.Toast;

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
    @BindView(R.id.request_unpicked_orders_list)
    RecyclerView unpicked_order_list_recyclerView;
    @BindView(R.id.request_progress_circle)
    ProgressBar progressBar;
    public static UnpickedRequestsAdapter unpickedDriverRequestsAdapter;
    public static List<PostOrder> driver_related_posts = new ArrayList<>();

    @BindView(R.id.request_no_posts_txt_view)
    public TextView no_post_text_alert;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, null, false);
    }

    private int purpose;

    public DriverRelatedOrdersListFragment(int purpose) {
        this.purpose = purpose;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());

        unpickedDriverRequestsAdapter = purpose == 1 ? new UnpickedRequestsAdapter(getContext(), driver_related_posts, 3) : new UnpickedRequestsAdapter(getContext(), driver_related_posts, 2);
        unpicked_order_list_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        unpicked_order_list_recyclerView.setAdapter(unpickedDriverRequestsAdapter);
        if (driver_related_posts.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
            refreshDriverRelatedOrderList(getContext());
        } else {
            no_post_text_alert.setVisibility(View.GONE);
            unpickedDriverRequestsAdapter.notifyDataSetChanged();
        }
    }

    public void refreshDriverRelatedOrderList(Context context) {
        String token = Personal.getToken(context);
        driver_related_posts.clear();
        String url = "";
        if (purpose == 1) {
            url = Statics.URL.REST.get_unpicked_driver_related_orders;
        } else if (purpose == 2) {
            url = Statics.URL.REST.get_picked_driver_related_orders;
        }

        Ion.with(context).load("GET", url)
                .setHeader("Authorization", "Token " + token)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                PostOrder order = PostOrder.getOrderByJsonObject(result.get(i).getAsJsonObject(), 2);
                                driver_related_posts.add(order);
                            }
                            progressBar.setVisibility(View.GONE);
                            unpickedDriverRequestsAdapter.notifyDataSetChanged();
                            if (driver_related_posts.isEmpty())
                                no_post_text_alert.setVisibility(View.VISIBLE);
                            else no_post_text_alert.setVisibility(View.GONE);
                            Toast.makeText(getContext(), purpose == 1 ? "Refresh unpicked list finished " : "Refresh picked list finished" + driver_related_posts.size(), Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Toast.makeText(getContext(), "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String TAG = "Driver_Related";
}
