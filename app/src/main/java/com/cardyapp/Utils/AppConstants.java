package com.cardyapp.Utils;

/**
 * Created by rac on 28/12/17.
 */

public class AppConstants {

    public static String TAG = "CARDY-App";
    public static String DEVICE_AOS = "2";
    public static String FB_LOGIN = "1";
    public static String GOOGLE_LOGIN = "2";
    public static String LINKEDIN_LOGIN = "3";

    public static String SOCIAL__SIGN_UP_PASSWORD = "12345";

    public static String F_GENDER = "1";
    public static String M_GENDER = "0";

    public static String PROFILE_COMPLETE = "1";
    public static String PROFILE_INCOMPLETE = "0";

    public static final long LOCATION_UPDATE_INTERVAL_MILLIS = 20000;
    public static final long LOCATION_UPDATE_DISTANCE_METERS = 0;
    public static final int REQUEST_PERMISSION = 100;

    public static final int LAUNCH_CAMERA_REQUEST_CODE = 10001;
    public static final int LAUNCH_ALBUM_REQUEST_CODE = 10002;

    public static final String PROFILE_IMAGE_FILE_NAME = "ProfileImage";
    public static final String CROPPED_IMAGE_FILE_NAME = "CroppedImage";
    public static final int MIN_PROFILE_SIZE_KB = 200;

    public enum DashboardMenu {
        CONNECTION,
        SEARCH,
        QR_SANNER,
        PENDING_REQUST,
        PROFILE
    }

    public enum PendingRequestAction {
        ACCEPT,
        REJECT,
        ACCEPT_AND_REVERT
    }
}
