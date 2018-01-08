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
import android.widget.Toast;

import com.cardyapp.Activities.SignUpActivity;
import com.cardyapp.Adapters.ConnectionsRecyclerViewAdapter;
import com.cardyapp.Adapters.PendingRequestRecyclerViewAdapter;
import com.cardyapp.App.Cardy;
import com.cardyapp.Models.ConnectionDTO;
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
 * Created by Priyanka on 12/31/2017.
 */

public class PendingRequestFragment extends Fragment {

    @BindView(R.id.rv_pendingRequest)
    public RecyclerView mRvPendingRequests;

    private List<Userdata> list = new ArrayList<>();

    private Cardy app;

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
        app = (Cardy) getActivity().getApplication();

        getPendingRequest();

        return view;
    }

    private void getPendingRequest() {
        Userdata userdata = app.getPreferences().getLoggedInUser(app);

        CardySingleton.getInstance().callToGetPendingRequestsAPI(userdata.getUserid(), new Callback<PendingResuestModel>() {
            @Override
            public void onResponse(Call<PendingResuestModel> call, Response<PendingResuestModel> response) {
                Log.d(AppConstants.TAG, response.toString());
                PendingResuestModel model = response.body();
                if (model.getIsStatus()){
                    list = model.getData();
                    if (list != null)
                        setAdapter();
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
            public void onFailure(Call<PendingResuestModel> call, Throwable t) {

            }
        });
    }

    private void setAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        list.addAll(list);
        PendingRequestRecyclerViewAdapter adapter = new PendingRequestRecyclerViewAdapter(getActivity(), list, new PendingRequestRecyclerViewAdapter.PendingRequestBtnClickListener() {
            @Override
            public void onClickedPendingBtn(int pos, String action) {
                Toast.makeText(getActivity(), action, Toast.LENGTH_LONG).show();
            }
        });
        mRvPendingRequests.setAdapter(adapter);
        mRvPendingRequests.setLayoutManager(linearLayoutManager);
    }
}
