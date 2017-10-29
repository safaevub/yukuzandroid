package com.example.utkur.yukuzapp.MainDirectory.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson.AddOrderActivity;
import com.example.utkur.yukuzapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammadjon on 10/29/2017.
 */

public class Deliveries extends Fragment {
    @BindView(R.id.add_order)
    FloatingActionButton add_order;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, getActivity());
        add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddOrderActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
