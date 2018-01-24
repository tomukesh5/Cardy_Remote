package com.cardyapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.cardyapp.R;
import com.cardyapp.Utils.DialogUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}
