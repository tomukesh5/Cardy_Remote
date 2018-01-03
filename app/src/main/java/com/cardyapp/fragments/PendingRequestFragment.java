package com.cardyapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cardyapp.Adapters.ConnectionsRecyclerViewAdapter;
import com.cardyapp.Adapters.PendingRequestRecyclerViewAdapter;
import com.cardyapp.Models.ConnectionDTO;
import com.cardyapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Priyanka on 12/31/2017.
 */

public class PendingRequestFragment extends Fragment {

    @BindView(R.id.rv_pendingRequest)
    public RecyclerView mRvPendingRequests;

    private List<ConnectionDTO> list = new ArrayList<>();

    public PendingRequestFragment() {

    }

    public static PendingRequestFragment newIntence() {
        return new PendingRequestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_request, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("About");

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

        PendingRequestRecyclerViewAdapter adapter = new PendingRequestRecyclerViewAdapter(getActivity(), list, new PendingRequestRecyclerViewAdapter.PendingRequestBtnClickListener() {
            @Override
            public void onClickedPendingBtn(int pos, String action) {
                Toast.makeText(getActivity(), action, Toast.LENGTH_LONG).show();
            }
        });
        mRvPendingRequests.setAdapter(adapter);
        mRvPendingRequests.setLayoutManager(linearLayoutManager);

        return view;
    }
}
