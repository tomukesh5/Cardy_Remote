package com.cardyapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cardyapp.Activities.ConnectionListActivity;
import com.cardyapp.Adapters.ConnectionsRecyclerViewAdapter;
import com.cardyapp.Models.ConnectionDTO;
import com.cardyapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rac on 27/12/17.
 */

public class ConnectionListFragment extends Fragment {

    @BindView(R.id.rv_connection)
    public RecyclerView mRvConnections;

    private List<ConnectionDTO> list = new ArrayList<>();

    public ConnectionListFragment() {

    }

    public static ConnectionListFragment newInstance() {
        return new ConnectionListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_connection_list,container,false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("About");
        setAdapter();
        return view;
    }

    private void setAdapter() {

        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvConnections.setAdapter(new ConnectionsRecyclerViewAdapter(getActivity(), list));
        mRvConnections.setLayoutManager(linearLayoutManager);
    }
}
