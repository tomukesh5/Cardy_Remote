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

import com.cardyapp.Adapters.PendingRequestRecyclerViewAdapter;
import com.cardyapp.App.Cardy;
import com.cardyapp.Models.BaseResponse;
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
                if (model.getIsStatus()) {
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

        list.addAll(list);
        PendingRequestRecyclerViewAdapter adapter = new PendingRequestRecyclerViewAdapter(getActivity(), list, new PendingRequestRecyclerViewAdapter.PendingRequestBtnClickListener() {
            @Override
            public void onClickedPendingBtn(String id, String action) {
                if (AppConstants.PendingRequestAction.ACCEPT.name().equalsIgnoreCase(action)) {
                    acceptRequest(id);
                } else if (AppConstants.PendingRequestAction.REJECT.name().equalsIgnoreCase(action)) {
                    rejectRequest(id);
                } else if (AppConstants.PendingRequestAction.ACCEPT_AND_REVERT.name().equalsIgnoreCase(action)) {
                    acceptAndRevertRequest(id);
                }
            }
        });
        mRvPendingRequests.setAdapter(adapter);
        mRvPendingRequests.setLayoutManager(linearLayoutManager);
    }

    private void acceptRequest(String id) {
        Userdata userdata = app.getPreferences().getLoggedInUser(app);

        CardySingleton.getInstance().callToAcceptRequestAPI(userdata.getUserid(), id, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(AppConstants.TAG, response.toString());
                BaseResponse model = response.body();
                if (model.getIsStatus()) {
                    DialogUtils.show(getActivity(), model.getMessage(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                        @Override
                        public void onPositiveAction() {
                        }

                        @Override
                        public void onNegativeAction() {
                        }
                    });
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

    private void rejectRequest(String id) {
        Userdata userdata = app.getPreferences().getLoggedInUser(app);

        CardySingleton.getInstance().callToRejectRequestAPI(userdata.getUserid(), id, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(AppConstants.TAG, response.toString());
                BaseResponse model = response.body();
                if (model.getIsStatus()) {
                    DialogUtils.show(getActivity(), model.getMessage(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                        @Override
                        public void onPositiveAction() {
                        }

                        @Override
                        public void onNegativeAction() {
                        }
                    });
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

    private void acceptAndRevertRequest(String id) {
        Userdata userdata = app.getPreferences().getLoggedInUser(app);

        CardySingleton.getInstance().callToAcceptAndRevertRequestAPI(userdata.getUserid(), id, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(AppConstants.TAG, response.toString());
                BaseResponse model = response.body();
                if (model.getIsStatus()) {
                    DialogUtils.show(getActivity(), model.getMessage(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                        @Override
                        public void onPositiveAction() {
                        }

                        @Override
                        public void onNegativeAction() {
                        }
                    });
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
