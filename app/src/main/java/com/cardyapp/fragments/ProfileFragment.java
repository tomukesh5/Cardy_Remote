package com.cardyapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.cardyapp.Activities.BaseActivity;
import com.cardyapp.Activities.DashboardActivity;
import com.cardyapp.Activities.FirstTimeProfileActivity;
import com.cardyapp.Activities.OTPActivity;
import com.cardyapp.Activities.SignInActivity;
import com.cardyapp.App.Cardy;
import com.cardyapp.Models.BaseResponse;
import com.cardyapp.Models.GetUserProfileModel;
import com.cardyapp.Models.SignInModel;
import com.cardyapp.Models.UploadProfilePicModel;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;
import com.cardyapp.Utils.CommonUtil;
import com.cardyapp.Utils.DialogUtils;
import com.cardyapp.Utils.ImageBase64Convertion;
import com.cardyapp.Utils.IntentExtras;
import com.cardyapp.Utils.PictureSourceChooser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.cardyapp.Utils.AppConstants.F_GENDER;
import static com.cardyapp.Utils.AppConstants.M_GENDER;

/**
 * Created by rac on 27/12/17.
 */

public class ProfileFragment extends Fragment implements Validator.ValidationListener, PictureSourceChooser.OnGallerySource,
        PictureSourceChooser.OnCameraSource {

    public interface onProfileUpdateListener {
        void onProfileUpdated();
    }

    public onProfileUpdateListener listener;


    @BindView(R.id.et_firstName)
    @Order(1)
    @NotEmpty(sequence = 1, message = "Please enter first name")
    public EditText mEtFName;


    @BindView(R.id.et_lastName)
    @Order(2)
    @NotEmpty(sequence = 1, message = "Please enter last name")
    public EditText mEtLName;


    @BindView(R.id.et_dateOfBirth)
    @Order(3)
    @NotEmpty(sequence = 1, message = "Please enter date of birth")
    public EditText mEtDateOfBirth;


    @BindView(R.id.et_availability)
    @Order(4)
    @NotEmpty(sequence = 1, message = "Please enter availability")
    public EditText mEtAvailability;


    @BindView(R.id.et_mobileNo)
    @Order(5)
    @NotEmpty(sequence = 1, message = "Please enter mobile number")
    public EditText mEtMobileNo;


    @BindView(R.id.et_email)
    @Order(6)
    @NotEmpty(sequence = 1, message = "Please enter email address")
    @Email(sequence = 2, message = "Invalid email address")
    public EditText mEtEmail;


    @BindView(R.id.et_currentCompany)
    @Order(7)
    @NotEmpty(sequence = 1, message = "Please enter current company")
    public EditText mEtCurrentCompany;

    @BindView(R.id.et_designation)
    @Order(8)
    @NotEmpty(sequence = 1, message = "Please enter designation")
    public EditText mEtDesignation;

    @BindView(R.id.et_officialEmailAddress)
    @Order(9)
    @NotEmpty(sequence = 1, message = "Please enter official email address")
    @Email(sequence = 2, message = "Invalid email address")
    public EditText mEtOfficialEmail;

    @BindView(R.id.et_previousCompany)
    @Order(10)
    @NotEmpty(sequence = 1, message = "Please enter previous company")
    public EditText mEtPreviousCompany;

    @Order(11)
    @NotEmpty(sequence = 1, message = "Please enter facebook profile link")
    @BindView(R.id.et_fbProfileLink)
    public EditText mEtFBProfileLink;

    @BindView(R.id.et_googleProfileLink)
    @Order(12)
    @NotEmpty(sequence = 1, message = "Please enter google profile link")
    public EditText mEtGoogleProfileLink;

    @BindView(R.id.et_linkedinProfileLink)
    @Order(13)
    @NotEmpty(sequence = 1, message = "Please enter linkedin profile link")
    public EditText mEtLinkedinProfileLink;

    @BindView(R.id.et_qualification)
    @Order(14)
    @NotEmpty(sequence = 1, message = "Please enter qualification")
    public EditText mEtQualification;

    @BindView(R.id.et_biography)
    @Order(15)
    @NotEmpty(sequence = 1, message = "Please enter Biography")
    public EditText mEtBiography;

    @BindView(R.id.rb_Male)
    public RadioButton mRBMale;

    @BindView(R.id.rb_Female)
    public RadioButton mRBFemale;

    @BindView(R.id.civ_profile)
    public CircleImageView mCIVProfilePic;

    @BindView(R.id.ll_basic)
    public LinearLayout llBasic;

    @BindView(R.id.ll_advance)
    public LinearLayout llAdvance;

    @BindView(R.id.view_basic)
    public View viewBasic;

    @BindView(R.id.view_advance)
    public View viewAdvance;

    private Userdata userdata;
    private Cardy app;
    private Validator mValidator;
    private PictureSourceChooser pictureSourceChooser;
    private File profilePic;
    String base64Profile;
    private String mobileNumber = "";

    public ProfileFragment() {

    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public void setListener(onProfileUpdateListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle(getResources().getString(R.string.Profile_title));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        app = (Cardy) getActivity().getApplication();
        userdata = app.getPreferences().getLoggedInUser(app);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

        if (userdata != null)
            mobileNumber = userdata.getUser_mobile();

        getUserDetails();
        return view;
    }

    @OnClick({R.id.tv_basic, R.id.tv_advance})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_basic :
                llBasic.setVisibility(View.VISIBLE);
                llAdvance.setVisibility(View.GONE);
                viewBasic.setBackgroundColor(getResources().getColor(R.color.yellow_app));
                viewAdvance.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.tv_advance :
                llBasic.setVisibility(View.GONE);
                llAdvance.setVisibility(View.VISIBLE);
                viewBasic.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                viewAdvance.setBackgroundColor(getResources().getColor(R.color.yellow_app));
                break;
        }
    }

    private void getUserDetails() {
        ((BaseActivity)getActivity()).showProgress("");
        CardySingleton.getInstance().callToGetProfileAPI(userdata.getUserid(), new Callback<GetUserProfileModel>() {
            @Override
            public void onResponse(Call<GetUserProfileModel> call, Response<GetUserProfileModel> response) {
                ((BaseActivity)getActivity()).hideProgress();
                final GetUserProfileModel signInModel = response.body();
                if (signInModel != null && signInModel.getIsStatus()) {
                    Log.e(AppConstants.TAG, "Response : " + signInModel.toString());
                    if (signInModel.getData() != null) {
                        app.getPreferences().setLoggedInUser(signInModel.getData(), app);
                        userdata = signInModel.getData();
                    }
                    initView();

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
            public void onFailure(Call<GetUserProfileModel> call, Throwable t) {
                ((BaseActivity)getActivity()).hideProgress();
                initView();
            }
        });
    }

    private void initView() {

        mEtMobileNo.setText(mobileNumber);

        if (userdata == null)
            return;

        mEtFName.setText(userdata.getFirstname() == null ? "" : userdata.getFirstname());
        mEtLName.setText(userdata.getLastname() == null ? "" : userdata.getLastname());
        mEtDateOfBirth.setText(userdata.getDob() == null ? "" : userdata.getDob());
        mEtAvailability.setText("");
        //mEtMobileNo.setText(userdata.getMobileno() == null ? "" : userdata.getMobileno());
        mEtEmail.setText(userdata.getPersonalemail() == null ? "" : userdata.getPersonalemail());
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
        loadProfilePic(userdata.getProfilepic());
    }

    private void loadProfilePic(String url) {

        if (!CommonUtil.isEmpty(url)) {
            Glide.with(getActivity()).load(getResources().getString(R.string.BASE_PROFILE_URL) + url).signature(new StringSignature(new Date() + "")).error(setDefaultProfilePic()).into(mCIVProfilePic);
        } else {
            mCIVProfilePic.setImageResource(setDefaultProfilePic());
        }
    }

    private int setDefaultProfilePic() {
        return R.drawable.blank_profile;
    }

    private void getPermissions() {
        String[] permissions = new String[]{
                "android.permission.CAMERA",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        BaseActivity activity = (BaseActivity) getActivity();
        activity.requestForPermissions(permissions, getString(R.string.Camera_permission_dialog), new BaseActivity.PermissionCallback() {
            @Override
            public void onAllPermissionGranted() {
                pictureSourceChooser = new PictureSourceChooser(getActivity(), ProfileFragment.this, ProfileFragment.this);
                pictureSourceChooser.show();
            }

            @Override
            public void onPermissionsDenied(String[] deniedPermissions) {
                getPermissions();
            }
        });
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

    @OnClick(R.id.iv_camera)
    public void onClickProfileCamera() {
        getPermissions();
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

        ((BaseActivity)getActivity()).showProgress("");
        final Userdata user = new Userdata();
        user.setUserid(userdata.getUserid());
        user.setFirstname(mEtFName.getText() + "");
        user.setLastname(mEtLName.getText() + "");
        if (mRBFemale.isChecked())
            user.setGender(F_GENDER);
        else user.setGender(M_GENDER);
        user.setDob(mEtDateOfBirth.getText() + "");
        user.setUser_mobile(mEtMobileNo.getText() + "");
        user.setPersonalemail(mEtEmail.getText() + "");
        user.setCurcompany(mEtCurrentCompany.getText() + "");
        user.setDesignation(mEtDesignation.getText() + "");
        user.setOfficialemail(mEtOfficialEmail.getText() + "");
        user.setPrevcompany(mEtPreviousCompany.getText() + "");
        user.setFbprofilelink(mEtFBProfileLink.getText() + "");
        user.setGoogleprofilelink(mEtGoogleProfileLink.getText() + "");
        user.setLinkedinprofilelink(mEtLinkedinProfileLink.getText() + "");
        user.setQualification(mEtQualification.getText() + "");
        user.setBiography(mEtBiography.getText() + "");

        CardySingleton.getInstance().callToUpdateUserProfileAPI(user, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(AppConstants.TAG, response.toString());
                ((BaseActivity)getActivity()).hideProgress();
                if (response.body().getIsStatus()) {
                    userdata.setFirstname(mEtFName.getText() + "");
                    userdata.setLastname(mEtLName.getText() + "");
                    if (mRBFemale.isChecked())
                        userdata.setGender(F_GENDER);
                    else userdata.setGender(M_GENDER);
                    userdata.setDob(mEtDateOfBirth.getText() + "");
                    userdata.setUser_mobile(mEtMobileNo.getText() + "");
                    userdata.setPersonalemail(mEtEmail.getText() + "");
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

                    if (listener != null) {
                        listener.onProfileUpdated();
                    }

                } else {
                    DialogUtils.show(getActivity(), response.body().getMessage(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
                ((BaseActivity)getActivity()).hideProgress();
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

    @Override
    public void onCameraSelected() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File croppedFile = new File(getCroppedImagePath().getPath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(croppedFile));
        startActivityForResult(intent, AppConstants.LAUNCH_CAMERA_REQUEST_CODE);
    }

    @Override
    public void onGallerySelected() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, AppConstants.LAUNCH_ALBUM_REQUEST_CODE);
    }

    private Uri getProfileImagePath() {
        return Uri.fromFile(new File(CommonUtil.getPictureDirectory() + AppConstants.PROFILE_IMAGE_FILE_NAME + ".jpg"));
    }

    private Uri getBkUpProfileImagePath() {
        return Uri.fromFile(new File(CommonUtil.getPictureDirectory() + AppConstants.PROFILE_IMAGE_FILE_NAME + "_bkup" + ".jpg"));
    }

    private Uri getCroppedImagePath() {
        return Uri.fromFile(new File(CommonUtil.getPictureDirectory() + AppConstants.CROPPED_IMAGE_FILE_NAME + ".jpg"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case AppConstants.LAUNCH_ALBUM_REQUEST_CODE:
                if (RESULT_OK == resultCode) {
                    File croppedFile = new File(CommonUtil.getPath(getActivity(), data.getData()));
                    beginCrop(Uri.fromFile(croppedFile));
                }
                break;

            case AppConstants.LAUNCH_CAMERA_REQUEST_CODE:
                if (RESULT_OK == resultCode) {
                    File croppedFile = new File(getCroppedImagePath().getPath());
                    beginCrop(Uri.fromFile(croppedFile));
                }
                break;

            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    final File croppedPic = new File(Crop.getOutput(data).getPath());
                    if (croppedPic.length() / 1024 > AppConstants.MIN_PROFILE_SIZE_KB) {
                        //Image size > 200KB
                        //Do sampling
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inDither = false;
                        options.inSampleSize = 2;
                        options.inScaled = false;
                        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                        options.inTempStorage = new byte[16384];
                        Bitmap croppedBitmap = BitmapFactory.decodeFile(croppedPic.getAbsolutePath(), options);
                        //profileImageView.setImageBitmap(croppedBitmap);

                        try {
                            if (croppedBitmap == null) {
                                croppedBitmap = BitmapFactory.decodeStream(new FileInputStream(croppedPic), null, options);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        base64Profile = ImageBase64Convertion.encode(b);
                        croppedBitmap = rotateImage(croppedBitmap, croppedPic.getAbsolutePath());
                        profilePic = new File(getProfileImagePath().getPath());
                        try {
                            //Do Compress
                            croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(getProfileImagePath().getPath(), false));
                            Glide.with(this).load(getProfileImagePath()).signature(new StringSignature(new Date() + "")).into(mCIVProfilePic);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            //DialogUtils.show(this, getString(R.string.alert));
                        }
                    } else {
                        profilePic = new File(getProfileImagePath().getPath());
                        Glide.with(ProfileFragment.this).load(profilePic).signature(new StringSignature(new Date() + "")).into(mCIVProfilePic);
                    }
                    updateProfilePic();
                } else if (resultCode == Crop.RESULT_ERROR) {
                    DialogUtils.show(getActivity(), Crop.getError(data).getMessage(), getString(R.string.Dialog_title), getString(R.string.OK), new DialogUtils.ActionListner() {
                        @Override
                        public void onPositiveAction() {

                        }

                        @Override
                        public void onNegativeAction() {

                        }
                    });
                }
                break;
        }
    }

    private void beginCrop(final Uri source) {
        final File currentProfilePic = new File(getProfileImagePath().getPath());
        if (currentProfilePic.exists() && currentProfilePic.isFile()) {
            final File bkupFile = new File(getBkUpProfileImagePath().getPath());
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        CommonUtil.copy(currentProfilePic, bkupFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Uri destination = getProfileImagePath();
                    Crop.of(source, destination).asSquare().start(getActivity(), ProfileFragment.this);
                }
            }.execute();
        } else {
            Uri destination = getProfileImagePath();
            Crop.of(source, destination).asSquare().start(getActivity(), ProfileFragment.this);
        }
    }

    public static Bitmap rotateImage(Bitmap source, String imagePath) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) getImageOrientation(imagePath));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static int getImageOrientation(String imagePath) {
        try {
            switch (new ExifInterface(new File(imagePath).getAbsolutePath()).getAttributeInt("Orientation", 1)) {
                case 3 /*3*/:
                    return 180;
                case R.styleable.Toolbar_contentInsetEnd /*6*/:
                    return 90;
                case 8 /*8*/:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void updateProfilePic() {
        if (profilePic == null)
            return;

//        UploadProfilePicModel uploadProfilePicModel = new UploadProfilePicModel();
//        uploadProfilePicModel.setUserid(userdata.getUserid());
//        uploadProfilePicModel.setProfilepic(profilePic);
        CardySingleton.getInstance().callToUpdateProfilePicAPI(userdata.getUserid(), profilePic, new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.d(AppConstants.TAG, response.toString());
                if (response.body() != null) {
                    DialogUtils.show(getActivity(), response.body().getMessage(), getResources().getString(R.string.Dialog_title), getResources().getString(R.string.OK), false, false, new DialogUtils.ActionListner() {
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
