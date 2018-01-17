package com.cardyapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cardyapp.App.Cardy;
import com.cardyapp.R;

import butterknife.ButterKnife;

/**
 * Created by rac on 17/1/18.
 */

public class ShareFragment extends Fragment {

    private Cardy app;

    public ShareFragment() {

    }

    public static ShareFragment newIntence() {
        return new ShareFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        ButterKnife.bind(this, view);
        app = (Cardy) getActivity().getApplication();
        getActivity().setTitle(getResources().getString(R.string.Share_title));

        return view;
    }
}
