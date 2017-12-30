package com.cardyapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cardyapp.R;

import butterknife.ButterKnife;

/**
 * Created by rac on 27/12/17.
 */

public class HomeFragment extends Fragment {

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("About");
        return view;
    }
}
