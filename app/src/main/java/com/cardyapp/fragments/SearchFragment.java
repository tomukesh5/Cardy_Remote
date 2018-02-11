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
import com.cardyapp.Models.BaseResponse;
import com.cardyapp.Models.PendingResuestModel;
import com.cardyapp.Models.RequestConnection;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rac on 9/1/18.
 */

public class SearchFragment extends Fragment {

    @BindView(R.id.rv_searchResult)
    public RecyclerView mRvSearchResult;
    @BindView(R.id.tv_emptyView)
    public TextView mTvEmptyView;
    @BindView(R.id.btn_send)
    public TextView mBtnSend;

    private List<Userdata> list = new ArrayList<>();
    private ConnectionsRecyclerViewAdapter adapter;

    private Cardy app;

    public SearchFragment() {

    }

    public static SearchFragment newIntence() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        app = (Cardy) getActivity().getApplication();
        getActivity().setTitle(getResources().getString(R.string.Search_title));
        searchRequest();

        return view;
    }

    private void searchRequest() {
        Userdata userdata = app.getPreferences().getLoggedInUser(app);

        final DrawerActivity activity = (DrawerActivity) getActivity();
        activity.showProgress("");
        CardySingleton.getInstance().callToSearchUserNearMeAPI(userdata.getUserid(), "5", new Callback<PendingResuestModel>() {
            @Override
            public void onResponse(Call<PendingResuestModel> call, Response<PendingResuestModel> response) {
                Log.d(AppConstants.TAG, response.toString());
                activity.hideProgress();
                PendingResuestModel model = response.body();
                if (model != null && model.getIsStatus()) {
                    list = model.getData();
                    if (list != null && list.size() > 0) {
                        mTvEmptyView.setVisibility(View.GONE);
                        mRvSearchResult.setVisibility(View.VISIBLE);
                        mBtnSend.setVisibility(View.VISIBLE);
                        setAdapter();
                    } else {
                        mTvEmptyView.setVisibility(View.VISIBLE);
                        mRvSearchResult.setVisibility(View.GONE);
                        mBtnSend.setVisibility(View.GONE);
                    }
                } else {
                    mTvEmptyView.setVisibility(View.VISIBLE);
                    mRvSearchResult.setVisibility(View.GONE);
                    mBtnSend.setVisibility(View.GONE);
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
                mRvSearchResult.setVisibility(View.GONE);
                mBtnSend.setVisibility(View.GONE);
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

        adapter = new ConnectionsRecyclerViewAdapter(getActivity(), list, false);
        mRvSearchResult.setAdapter(adapter);
        mRvSearchResult.setLayoutManager(linearLayoutManager);
    }

    @OnClick(R.id.btn_send)
    public void onClickBtnSend() {
        if (adapter != null) {
            if (adapter.getSelectedUser() != null && adapter.getSelectedUser().size() > 0) {
                Userdata userdata = app.getPreferences().getLoggedInUser(app);
                List<RequestConnection> list = new ArrayList<>();
                for (String str : adapter.getSelectedUser()) {
                    RequestConnection connection = new RequestConnection();
                    connection.setUserid(userdata.getUserid());
                    connection.setRequesttouserid(str);
                    list.add(connection);
                }

                sendMultipleRequest(list);

            } else {
                DialogUtils.show(getActivity(), getResources().getString(R.string.No_connection_selected_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                    @Override
                    public void onPositiveAction() {

                    }

                    @Override
                    public void onNegativeAction() {

                    }
                });
            }
        } else {
            DialogUtils.show(getActivity(), getResources().getString(R.string.No_connection_selected_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                @Override
                public void onPositiveAction() {

                }

                @Override
                public void onNegativeAction() {

                }
            });
        }
    }

    private void sendMultipleRequest(List<RequestConnection> list) {
        final DrawerActivity activity = (DrawerActivity) getActivity();
        activity.showProgress("");
        CardySingleton.getInstance().callToSendMultipleRequestAPI(list, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(AppConstants.TAG, response.toString());
                activity.hideProgress();
                BaseResponse model = response.body();
                if (model != null && model.getIsStatus()) {
                    searchRequest();
                } else {
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
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                activity.hideProgress();
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
}
