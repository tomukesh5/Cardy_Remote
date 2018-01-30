package com.cardyapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cardyapp.Models.BaseResponse;
import com.cardyapp.Models.SignUpModel;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Utils.IntentExtras;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Priyanka on 1/23/2018.
 */

public class OTPActivity extends BaseActivity {

    @BindView(R.id.et_otp1)
    public EditText mEtOTP1;

    @BindView(R.id.et_otp2)
    public EditText mEtOTP2;

    @BindView(R.id.et_otp3)
    public EditText mEtOTP3;

    @BindView(R.id.et_otp4)
    public EditText mEtOTP4;

    private Userdata userdata;
    private String userid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(IntentExtras.USER_DTO))
            userdata = (Userdata) getIntent().getExtras().get(IntentExtras.USER_DTO);
        else if (getIntent().hasExtra(IntentExtras.USER_ID)) {
            userid = (String) getIntent().getExtras().get(IntentExtras.USER_ID);
        }

        mEtOTP2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mEtOTP2.getText().toString().length() == 0) {
                        mEtOTP1.setText("");
                        mEtOTP1.requestFocus();
                        mEtOTP2.setEnabled(false);
                        return true;
                    }
                }
                return false;
            }
        });

        mEtOTP3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mEtOTP3.getText().toString().length() == 0) {
                        mEtOTP2.setText("");
                        mEtOTP2.requestFocus();
                        mEtOTP3.setEnabled(false);
                        return true;
                    }
                }
                return false;
            }
        });

        mEtOTP4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (mEtOTP4.getText().toString().length() == 0) {
                        mEtOTP3.setText("");
                        mEtOTP3.requestFocus();
                        mEtOTP4.setEnabled(false);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_otp;
    }

    @OnTextChanged(value = R.id.et_otp1,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_OTP1(Editable editable) {
        if (editable.length() > 0) {
            mEtOTP2.setEnabled(true);
            mEtOTP2.requestFocus();
        }
    }

    @OnTextChanged(value = R.id.et_otp2,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_OTP2(Editable editable) {
        if (editable.length() > 0) {
            mEtOTP3.setEnabled(true);
            mEtOTP3.requestFocus();
        }
    }

    @OnTextChanged(value = R.id.et_otp3,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_OTP3(Editable editable) {
        if (editable.length() > 0) {
            mEtOTP4.setEnabled(true);
            mEtOTP4.requestFocus();
        }
    }

    @OnClick(R.id.btn_submit)
    public void onClickSubmitBtn() {
        String str = mEtOTP1.getText() + "" + mEtOTP2.getText() + mEtOTP3.getText() + mEtOTP4.getText();
        if (str.length() == 4) {
            if (getIntent().hasExtra(IntentExtras.USER_DTO))
                verifyOTP(str);
            else if (getIntent().hasExtra(IntentExtras.USER_ID))
                resetPassword(str);

        } else {
            DialogUtils.show(OTPActivity.this, getResources().getString(R.string.InvalidOTP), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                @Override
                public void onPositiveAction() {
                }

                @Override
                public void onNegativeAction() {
                }
            });
        }
    }

    public void resetPassword(String otp) {
        showProgress("");

        CardySingleton.getInstance().callToResetPasswordAPI(userid, otp, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                hideProgress();
                Log.d(AppConstants.TAG, response.toString());

                final BaseResponse baseResponse = response.body();

                if (baseResponse != null && baseResponse.getIsStatus()) {
                    Log.e(AppConstants.TAG, "Response :" + baseResponse.toString());

                    //finish();
                } else {
                    DialogUtils.show(OTPActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
                hideProgress();
                Log.d(AppConstants.TAG, "onFailure");
                DialogUtils.show(OTPActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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

    public void verifyOTP(String otp) {
        showProgress("");

        CardySingleton.getInstance().callToVerifyOTPAPI(userdata.getUserid(), otp, Callback_verifyOTP);
    }

    Callback<BaseResponse> Callback_verifyOTP = new Callback<BaseResponse>() {
        @Override
        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

            hideProgress();
            Log.d(AppConstants.TAG, response.toString());

            final BaseResponse baseResponse = response.body();

            if (baseResponse != null && baseResponse.getIsStatus()) {
                Log.e(AppConstants.TAG, "Response :" + baseResponse.toString());
                getApp().getPreferences().setLoggedInUser(userdata, getApp());

                if (userdata.getIsProfileComplete()) {
                    Intent intent = new Intent(OTPActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else startActivity(new Intent(OTPActivity.this, FirstTimeProfileActivity.class));
                finish();
            } else {
                DialogUtils.show(OTPActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
            hideProgress();
            Log.d(AppConstants.TAG, "onFailure");
            DialogUtils.show(OTPActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                @Override
                public void onPositiveAction() {
                }

                @Override
                public void onNegativeAction() {
                }
            });
        }
    };

    @OnClick(R.id.tv_resendOTP)
    public void onClickResendOTP() {

        showProgress("");

        CardySingleton.getInstance().callToSendVerificationAPI(userdata.getUserid(), new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                hideProgress();
                Log.d(AppConstants.TAG, response.toString());

                final BaseResponse baseResponse = (BaseResponse) response.body();

                if (baseResponse != null && baseResponse.getIsStatus()) {
                    Log.e(AppConstants.TAG, "Response :" + baseResponse.toString());
                    DialogUtils.show(OTPActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                        @Override
                        public void onPositiveAction() {
                        }

                        @Override
                        public void onNegativeAction() {
                        }
                    });
                } else {
                    DialogUtils.show(OTPActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
            public void onFailure(Call call, Throwable t) {
                hideProgress();
                Log.d(AppConstants.TAG, "onFailure");
                DialogUtils.show(OTPActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
