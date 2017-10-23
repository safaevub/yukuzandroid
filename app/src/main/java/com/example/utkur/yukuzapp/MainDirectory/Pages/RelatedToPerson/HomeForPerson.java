package com.example.utkur.yukuzapp.MainDirectory.Pages.RelatedToPerson;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.utkur.yukuzapp.R;


/**
 * Created by Muhammadjon on 10/23/2017.
 */

public class HomeForPerson extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_home, container, false);

        return v;
    }
}
