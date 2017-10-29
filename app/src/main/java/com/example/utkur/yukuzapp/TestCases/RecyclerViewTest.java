package com.example.utkur.yukuzapp.TestCases;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.utkur.yukuzapp.Module.Request;
import com.example.utkur.yukuzapp.R;
import com.example.utkur.yukuzapp.TestCases.RecyclerViewAdapter.ItemsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewTest extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);
        ButterKnife.bind(this);
        List<Request> items = new ArrayList<>();
//        ItemsRecyclerViewAdapter adapter = new ItemsRecyclerViewAdapter(items, getBaseContext(), R.layout.);

    }

}
