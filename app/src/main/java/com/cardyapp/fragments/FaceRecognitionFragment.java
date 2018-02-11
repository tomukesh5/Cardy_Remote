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
 * Created by Priyanka on 2/11/2018.
 */

public class FaceRecognitionFragment extends Fragment {

    private Cardy app;

    public FaceRecognitionFragment() {

    }

    public static FaceRecognitionFragment newIntence() {
        return new FaceRecognitionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_face_recognition, container, false);
        ButterKnife.bind(this, view);
        app = (Cardy) getActivity().getApplication();
        getActivity().setTitle(getResources().getString(R.string.faceRecognition_text));

        return view;
    }
}
