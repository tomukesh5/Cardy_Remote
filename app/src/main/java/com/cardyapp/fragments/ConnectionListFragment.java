package com.cardyapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cardyapp.Activities.DrawerActivity;
import com.cardyapp.Adapters.ConnectionsRecyclerViewAdapter;
import com.cardyapp.App.Cardy;
import com.cardyapp.Models.PendingResuestModel;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rac on 27/12/17.
 */

public class ConnectionListFragment extends Fragment {

    @BindView(R.id.rv_connection)
    public RecyclerView mRvConnections;
    @BindView(R.id.tv_emptyView)
    public TextView mTvEmptyView;

    private List<Userdata> list = new ArrayList<>();

    private Cardy app;

    public ConnectionListFragment() {

    }

    public static ConnectionListFragment newInstance() {
        return new ConnectionListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection_list, container, false);
        ButterKnife.bind(this, view);
        app = (Cardy) getActivity().getApplication();
        getActivity().setTitle(getResources().getString(R.string.Connection_title));
        getConnections();
        return view;
    }

    private void getConnections() {
        Userdata userdata = app.getPreferences().getLoggedInUser(app);

        final DrawerActivity activity = (DrawerActivity) getActivity();
        activity.showProgress("");
        CardySingleton.getInstance().callToGetConnectionsAPI(userdata.getUserid(), new Callback<PendingResuestModel>() {
            @Override
            public void onResponse(Call<PendingResuestModel> call, Response<PendingResuestModel> response) {
                Log.d(AppConstants.TAG, response.toString());
                activity.hideProgress();
                PendingResuestModel model = response.body();
                if (model != null && model.getIsStatus()) {
                    list = model.getData();
                    if (list != null && list.size() > 0) {
                        mTvEmptyView.setVisibility(View.GONE);
                        mRvConnections.setVisibility(View.VISIBLE);
                        setAdapter();
                    } else {
                        mTvEmptyView.setVisibility(View.VISIBLE);
                        mRvConnections.setVisibility(View.GONE);
                    }
                } else {
                    mTvEmptyView.setVisibility(View.VISIBLE);
                    mRvConnections.setVisibility(View.GONE);
                    DialogUtils.show(getActivity(), response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                        @Override
                        public void onPositiveAction() {

                        }

                        @Override
                        public void onNegativeAction() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PendingResuestModel> call, Throwable t) {
                activity.hideProgress();
                mTvEmptyView.setText(getResources().getString(R.string.Network_error));
                mTvEmptyView.setVisibility(View.VISIBLE);
                mRvConnections.setVisibility(View.GONE);
                DialogUtils.show(getActivity(), getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                    @Override
                    public void onPositiveAction() {

                    }

                    @Override
                    public void onNegativeAction() {

                    }
                });
            }
        });
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvConnections.setAdapter(new ConnectionsRecyclerViewAdapter(getActivity(), list, true));
        mRvConnections.setLayoutManager(linearLayoutManager);
    }
}
