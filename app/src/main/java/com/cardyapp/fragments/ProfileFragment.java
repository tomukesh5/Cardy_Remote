package com.cardyapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.cardyapp.App.Cardy;
import com.cardyapp.Models.BaseResponse;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.DialogUtils;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cardyapp.Utils.AppConstants.F_GENDER;
import static com.cardyapp.Utils.AppConstants.M_GENDER;

/**
 * Created by rac on 27/12/17.
 */

public class ProfileFragment extends Fragment implements Validator.ValidationListener {

    @Order(1)
    @NotEmpty(sequence = 1, message = "Please enter first name")
    @BindView(R.id.et_firstName)
    public EditText mEtFName;

    @Order(2)
    @NotEmpty(sequence = 1, message = "Please enter last name")
    @BindView(R.id.et_lastName)
    public EditText mEtLName;

    @Order(3)
    @NotEmpty(sequence = 1, message = "Please enter date of birth")
    @BindView(R.id.et_dateOfBirth)
    public EditText mEtDateOfBirth;

    @Order(4)
    @NotEmpty(sequence = 1, message = "Please enter availability")
    @BindView(R.id.et_availability)
    public EditText mEtAvailability;

    @Order(5)
    @NotEmpty(sequence = 1, message = "Please enter mobile number")
    @BindView(R.id.et_mobileNo)
    public EditText mEtMobileNo;

    @Order(6)
    @NotEmpty(sequence = 1, message = "Please enter email address")
    @Email(sequence = 2, message = "Invalid email address")
    @BindView(R.id.et_email)
    public EditText mEtEmail;

    @Order(7)
    @NotEmpty(sequence = 1, message = "Please enter current company")
    @BindView(R.id.et_currentCompany)
    public EditText mEtCurrentCompany;

    @Order(8)
    @NotEmpty(sequence = 1, message = "Please enter designation")
    @BindView(R.id.et_designation)
    public EditText mEtDesignation;

    @Order(9)
    @NotEmpty(sequence = 1, message = "Please enter official email address")
    @Email(sequence = 2, message = "Invalid email address")
    @BindView(R.id.et_officialEmailAddress)
    public EditText mEtOfficialEmail;

    @Order(10)
    @NotEmpty(sequence = 1, message = "Please enter previous company")
    @BindView(R.id.et_previousCompany)
    public EditText mEtPreviousCompany;

    @Order(11)
    @NotEmpty(sequence = 1, message = "Please enter facebook profile link")
    @BindView(R.id.et_fbProfileLink)
    public EditText mEtFBProfileLink;

    @Order(12)
    @NotEmpty(sequence = 1, message = "Please enter google profile link")
    @BindView(R.id.et_googleProfileLink)
    public EditText mEtGoogleProfileLink;

    @Order(13)
    @NotEmpty(sequence = 1, message = "Please enter linkedin profile link")
    @BindView(R.id.et_linkedinProfileLink)
    public EditText mEtLinkedinProfileLink;

    @Order(14)
    @NotEmpty(sequence = 1, message = "Please enter qualification")
    @BindView(R.id.et_qualification)
    public EditText mEtQualification;

    @Order(15)
    @NotEmpty(sequence = 1, message = "Please enter Biography")
    @BindView(R.id.et_biography)
    public EditText mEtBiography;

    @BindView(R.id.rb_Male)
    public RadioButton mRBMale;

    @BindView(R.id.rb_Female)
    public RadioButton mRBFemale;

    private Userdata userdata;
    private Cardy app;
    private Validator mValidator;

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("About");
        app = (Cardy) getActivity().getApplication();
        userdata = app.getPreferences().getLoggedInUser(app);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

        initView();
        return view;
    }

    private void initView() {
        if (userdata == null)
            return;

        mEtFName.setText(userdata.getFirstname() == null ? "" : userdata.getFirstname());
        mEtLName.setText(userdata.getLastname() == null ? "" : userdata.getLastname());
        mEtDateOfBirth.setText(userdata.getDob() == null ? "" : userdata.getDob());
        mEtAvailability.setText("");
        mEtMobileNo.setText(userdata.getMobileno() == null ? "" : userdata.getMobileno());
        mEtEmail.setText(userdata.getUser_email() == null ? "" : userdata.getUser_email());
        mEtCurrentCompany.setText(userdata.getCurcompany() == null ? "" : userdata.getCurcompany());
        mEtDesignation.setText(userdata.getDesignation() == null ? "" : userdata.getDesignation());
        mEtOfficialEmail.setText(userdata.getOfficialemail() == null ? "" : userdata.getOfficialemail());
        mEtPreviousCompany.setText(userdata.getPrevcompany() == null ? "" : userdata.getPrevcompany());
        mEtFBProfileLink.setText(userdata.getFbprofilelink() == null ? "" : userdata.getFbprofilelink());
        mEtGoogleProfileLink.setText(userdata.getGoogleprofilelink() == null ? "" : userdata.getGoogleprofilelink());
        mEtLinkedinProfileLink.setText(userdata.getLinkedinprofilelink() == null ? "" : userdata.getLinkedinprofilelink());
        mEtQualification.setText(userdata.getQualification() == null ? "" : userdata.getQualification());
        mEtBiography.setText(userdata.getBiography() == null ? "" : userdata.getBiography());

        if (userdata.getGender() != null && userdata.getGender().equalsIgnoreCase(F_GENDER)) {
            mRBFemale.setChecked(true);
        } else {
            mRBMale.setChecked(true);
        }
    }

    @OnClick({R.id.rb_Female, R.id.rb_Male})
    public void onClickGenderRadioButton(RadioButton radioButton) {
        if (radioButton.isChecked()) {
            switch (radioButton.getId()) {
                case R.id.rb_Female:
                    mRBMale.setChecked(false);
                    break;
                case R.id.rb_Male:
                    mRBFemale.setChecked(false);
                    break;
            }
        }
    }

    @OnClick(R.id.btn_submit)
    public void onClickBtnSubmit() {
        validate();
    }

    @OnClick(R.id.et_firstName)
    public void onClickFName() {
        mEtFName.setError(null);
    }

    @OnClick(R.id.et_lastName)
    public void onClickLName() {
        mEtLName.setError(null);
    }

    @OnClick(R.id.et_dateOfBirth)
    public void onClickDateOfBirth() {
        mEtDateOfBirth.setError(null);
    }

    @OnClick(R.id.et_availability)
    public void onClickAvailability() {
        mEtAvailability.setError(null);
    }

    @OnClick(R.id.et_mobileNo)
    public void onClickMobileNo() {
        mEtMobileNo.setError(null);
    }

    @OnClick(R.id.et_email)
    public void onClickEmail() {
        mEtEmail.setError(null);
    }

    @OnClick(R.id.et_currentCompany)
    public void onClickCurrentCompany() {
        mEtCurrentCompany.setError(null);
    }

    @OnClick(R.id.et_designation)
    public void onClickDesignation() {
        mEtDesignation.setError(null);
    }

    @OnClick(R.id.et_officialEmailAddress)
    public void onClickOfficialEmailAddress() {
        mEtOfficialEmail.setError(null);
    }

    @OnClick(R.id.et_previousCompany)
    public void onClickPreviousCompany() {
        mEtPreviousCompany.setError(null);
    }

    @OnClick(R.id.et_fbProfileLink)
    public void onClickFbProfileLink() {
        mEtFBProfileLink.setError(null);
    }

    @OnClick(R.id.et_googleProfileLink)
    public void onClickGoogleProfileLink() {
        mEtGoogleProfileLink.setError(null);
    }

    @OnClick(R.id.et_linkedinProfileLink)
    public void onClickLinkedinProfileLink() {
        mEtLinkedinProfileLink.setError(null);
    }

    @OnClick(R.id.et_qualification)
    public void onClickQualification() {
        mEtQualification.setError(null);
    }

    @OnClick(R.id.et_biography)
    public void onClickBiography() {
        mEtBiography.setError(null);
    }

    public void validate() {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        // TODO: 12/30/2017 Integrated update profile api
        final Userdata user = new Userdata();
        user.setUserid(userdata.getUserid());
        user.setFirstname(mEtFName.getText() + "");
        user.setLastname(mEtLName.getText() + "");
        if (mRBFemale.isChecked())
            user.setGender(F_GENDER);
        else user.setGender(M_GENDER);
        user.setDob(mEtDateOfBirth.getText() + "");
        user.setMobileno(mEtMobileNo.getText() + "");
        user.setUser_email(mEtEmail.getText() + "");
        user.setCurcompany(mEtCurrentCompany.getText() + "");
        user.setDesignation(mEtDesignation.getText() + "");
        user.setOfficialemail(mEtOfficialEmail.getText() + "");
        user.setPrevcompany(mEtPreviousCompany.getText() + "");
        user.setFbprofilelink(mEtFBProfileLink.getText() + "");
        user.setGoogleprofilelink(mEtGoogleProfileLink.getText() + "");
        user.setLinkedinprofilelink(mEtLinkedinProfileLink.getText() + "");
        user.setQualification(mEtQualification.getText() + "");
        user.setBiography(mEtBiography.getText() + "");

        CardySingleton.getInstance().callToUpdateUserProfileAPI(userdata, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(AppConstants.TAG, response.toString());
                if (response.body().getIsStatus()) {
                    userdata.setFirstname(mEtFName.getText() + "");
                    userdata.setLastname(mEtLName.getText() + "");
                    if (mRBFemale.isChecked())
                        userdata.setGender(F_GENDER);
                    else userdata.setGender(M_GENDER);
                    userdata.setDob(mEtDateOfBirth.getText() + "");
                    userdata.setMobileno(mEtMobileNo.getText() + "");
                    userdata.setUser_email(mEtEmail.getText() + "");
                    userdata.setCurcompany(mEtCurrentCompany.getText() + "");
                    userdata.setDesignation(mEtDesignation.getText() + "");
                    userdata.setOfficialemail(mEtOfficialEmail.getText() + "");
                    userdata.setPrevcompany(mEtPreviousCompany.getText() + "");
                    userdata.setFbprofilelink(mEtFBProfileLink.getText() + "");
                    userdata.setGoogleprofilelink(mEtGoogleProfileLink.getText() + "");
                    userdata.setLinkedinprofilelink(mEtLinkedinProfileLink.getText() + "");
                    userdata.setQualification(mEtQualification.getText() + "");
                    userdata.setBiography(mEtBiography.getText() + "");

                    app.getPreferences().setLoggedInUser(userdata, app);
                }
                DialogUtils.show(getActivity(), response.body().getMessage(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
                    @Override
                    public void onPositiveAction() {

                    }

                    @Override
                    public void onNegativeAction() {

                    }
                });
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

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String[] listMessage = error.getCollatedErrorMessage(getActivity()).split("\n");
            String message = listMessage[0];
            //String message = error.getCollatedErrorMessage(getActivity());
            // Display error messages ;)
            if (view instanceof EditText) {
                EditText text = ((EditText) view);
                text.setError(message);
            }
        }
    }
}
