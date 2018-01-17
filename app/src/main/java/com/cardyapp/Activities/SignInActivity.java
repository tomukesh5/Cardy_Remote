package com.cardyapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cardyapp.Models.SignInModel;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Views.CardyProgressBar;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.linkedin.platform.LISessionManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseSocialSignInActivity implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(sequence = 1, message = "Please enter email address")
    @Email(sequence = 2, message = "Invalid email address")
    @BindView(R.id.et_email)
    public EditText mEtEmail;

    @Order(2)
    @NotEmpty(sequence = 1, message = "Please enter Password")
    @Password(min = 5, message = "Please enter a min 5 digit Password")
    @BindView(R.id.et_password)
    public EditText mEtPassword;

    @BindView(R.id.progressBar)
    public CardyProgressBar mProgress;

    private Validator mValidator;

    private String TAG = getClass().getSimpleName();

    @Override
    protected void getSocialData(String email, String password, String socialType, String socialUserData) {
        signInWithSocial(email, password, socialType, socialUserData);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
        //generateHashkey();
    }

    public void generateHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.cardyapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String str = Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP);
                Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    @OnTextChanged(value = R.id.et_password,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_Password(Editable editable) {
        if (editable.length() > 0) {
            mEtPassword.setError(null);
        }
    }

    @OnTextChanged(value = R.id.et_email,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged_Email(Editable editable) {
        if (editable.length() > 0) {
            mEtEmail.setError(null);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_in;
    }

    @OnClick(R.id.btn_signIn)
    public void onClickBtn_signUp() {
        validate();
    }

    @OnClick(R.id.ll_signUp)
    public void onClickSignUp() {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    @OnClick(R.id.tv_forgotPassword)
    public void onClickForgotPassword() {
        startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
    }

    // handle the respone by calling LISessionManager and start new activity
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        requestCode, resultCode, data);

        /*Intent intent = new Intent(MainActivity.this, HomePage.class);
        startActivity(intent);*/
        getCallbackManager().onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            getDataFromGPlus(result);
        }
    }

    @OnClick(R.id.ll_linkedin)
    public void onClickLL_Linkedin() {
        linkedinLogin();
    }

    @OnClick(R.id.ll_fb)
    public void onFacebookLogin() {
        onFbLogin();
    }

    @OnClick(R.id.ll_google)
    public void onGooglePluseLogin() {
        googleLogin();
    }

    public void validate() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        signInWithSocial(mEtEmail.getText() + "", mEtPassword.getText() + "", "", "");
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String[] listMessage = error.getCollatedErrorMessage(SignInActivity.this).split("\n");
            String message = listMessage[0];
            //String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText) {
                EditText text = ((EditText) view);
                text.setError(message);
            }
        }
    }

    private void signInWithSocial(String email, String password, String socialusertype, String socialUserData) {
        showProgress("");
        CardySingleton.getInstance().callToSignInAPI(email, password, socialusertype, socialUserData, Callback_SignIn);
    }


    Callback<SignInModel> Callback_SignIn = new Callback<SignInModel>() {
        @Override
        public void onResponse(Call<SignInModel> call, Response<SignInModel> response) {

            Log.d(AppConstants.TAG, response.toString());

            final SignInModel signInModel = response.body();
            hideProgress();
            if (signInModel != null && signInModel.getIsStatus()) {
                Log.e(TAG, "Response : " + signInModel.toString());
                getApp().getPreferences().setLoggedInUser(signInModel.getUserdata(), getApp());
                startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                finish();
            } else {
                DialogUtils.show(SignInActivity.this, response.message(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
        public void onFailure(Call<SignInModel> call, Throwable t) {
            hideProgress();
            Log.d(AppConstants.TAG, "onFailure");
            DialogUtils.show(SignInActivity.this, getResources().getString(R.string.Network_error), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                @Override
                public void onPositiveAction() {

                }

                @Override
                public void onNegativeAction() {

                }
            });
        }
    };
}
