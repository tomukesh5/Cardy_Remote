package com.cardyapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cardyapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Order(13)
    @NotEmpty(sequence = 1, message = "Please enter qualification")
    @BindView(R.id.et_qualification)
    public EditText mEtQualification;

    @Order(14)
    @NotEmpty(sequence = 1, message = "Please enter Biography")
    @BindView(R.id.et_biography)
    public EditText mEtBiography;

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

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

        return view;
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
