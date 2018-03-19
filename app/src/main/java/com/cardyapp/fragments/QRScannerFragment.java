package com.cardyapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cardyapp.Activities.BaseActivity;
import com.cardyapp.App.Cardy;
import com.cardyapp.R;
import com.cardyapp.Utils.PictureSourceChooser;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by rac on 17/1/18.
 */

public class QRScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {

    @BindView(R.id.bar_code_image)
    ImageView bar_code_image;
    @BindView(R.id.ll_scan)
    FrameLayout scanLayout;
    @BindView(R.id.ll_show)
    FrameLayout showLayout;
    @BindView(R.id.show_layout)
    RelativeLayout QRCodeLayout;
    @BindView(R.id.scanner_view)
    FrameLayout scannerViewLayout;

    private Cardy app;
    private ZXingScannerView mScannerView;

    public QRScannerFragment() {

    }

    public static QRScannerFragment newIntence() {
        return new QRScannerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        ButterKnife.bind(this, view);
        app = (Cardy) getActivity().getApplication();
        getActivity().setTitle(getResources().getString(R.string.QRScanner_title));

        mScannerView = new ZXingScannerView(getActivity());
        scannerViewLayout.addView(mScannerView);

        return view;
    }

    @OnClick(R.id.ll_scan)
    public void onClickedScan() {
        getPermissions();
    }

    private void getPermissions() {
        String[] permissions = new String[]{
                "android.permission.CAMERA"
        };
        BaseActivity activity = (BaseActivity) getActivity();
        activity.requestForPermissions(permissions, getString(R.string.Camera_permission_dialog), new BaseActivity.PermissionCallback() {
            @Override
            public void onAllPermissionGranted() {
                showLayout.setVisibility(View.GONE);
                scanLayout.setVisibility(View.GONE);
                scannerViewLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPermissionsDenied(String[] deniedPermissions) {
                getPermissions();
            }
        });
    }

    @OnClick(R.id.ll_show)
    public void onClickedShow() {
        showLayout.setVisibility(View.GONE);
        scanLayout.setVisibility(View.GONE);
        QRCodeLayout.setVisibility(View.VISIBLE);
        gen(new QRCode());
    }

    private void gen(QRCode qrCode) {
        String barcodeDate = new Gson().toJson(qrCode, QRCode.class);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(barcodeDate, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            bar_code_image.setImageBitmap(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(false);
        mScannerView.setAutoFocus(true);
       // mScannerView.setFormats(barcodeFormats);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        if (result != null) {
            //Intents.Scan scan = new Gson().fromJson(result.getText(), Intents.Scan.class);
            /*if (Utilities.isConnectedToInternet(getActivity())) {
                ((AttendanceActivity) getActivity()).showLoading();
                scan.setEventId(eventId);
                scanPresenter.validateCode(scan, false);
            } else {
                showSnackBar(getString(R.string.no_internet_string));
            }*/
            // Note:
            // * Wait 2 seconds to resume the preview.
            // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(QRScannerFragment.this);
                }
            }, 2000);
        }
    }
}