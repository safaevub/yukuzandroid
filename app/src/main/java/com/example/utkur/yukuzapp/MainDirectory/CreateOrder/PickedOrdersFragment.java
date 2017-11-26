package com.example.utkur.yukuzapp.MainDirectory.CreateOrder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.utkur.yukuzapp.Adapters.PickedRequestsAdapter;
import com.example.utkur.yukuzapp.Adapters.UnpickedRequestsAdapter;
import com.example.utkur.yukuzapp.Module.PickedPostOrder;
import com.example.utkur.yukuzapp.Module.PostOrder;
import com.example.utkur.yukuzapp.R;

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
    public PickedRequestsAdapter pickedRequestsAdapter;
    @BindView(R.id.progress_circle)
    ProgressBar progressBar;
    @BindView(R.id.add_order)
    FloatingActionButton add_order;
    public static List<PickedPostOrder> pickedOrders = new ArrayList<>();

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

    }
}
