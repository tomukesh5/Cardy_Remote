package com.cardyapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cardyapp.Activities.ConnectionDetailsActivity;
import com.cardyapp.Activities.DrawerActivity;
import com.cardyapp.Adapters.ConnectionListRecyclerViewAdapter;
import com.cardyapp.Adapters.SearchRecyclerViewAdapter;
import com.cardyapp.App.Cardy;
import com.cardyapp.Models.PendingResuestModel;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Utils.IntentExtras;

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

public class ConnectionListFragment extends Fragment implements ConnectionListRecyclerViewAdapter.onClickConnectionButton {

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

        mRvConnections.setAdapter(new ConnectionListRecyclerViewAdapter(getActivity(), list, this));
        mRvConnections.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClickedCallButton(int position) {
        String number = "tel:9999999999";
        if (list.get(position) != null)
            number = "tel:" + list.get(position).getMobileno();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(number));
        startActivity(intent);
    }

    @Override
    public void onClickedMailButton(int position) {
        String str = "to@email.com";
        if (list.get(position) != null)
            str = list.get(position).getUser_email();

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{str});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @Override
    public void onClickedWhatsappButton(int position) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        startActivity(whatsappIntent);
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getActivity(), ConnectionDetailsActivity.class);
        intent.putExtra(IntentExtras.CONNECTION_DTO, list.get(position));
        startActivity(intent);
    }
}
