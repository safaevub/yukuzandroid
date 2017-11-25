package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToDriver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.utkur.yukuzapp.R;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }
}
