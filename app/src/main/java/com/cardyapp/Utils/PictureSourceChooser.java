package com.cardyapp.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cardyapp.R;

public class PictureSourceChooser {

    public interface OnCameraSource {
        public void onCameraSelected();
    }

    public interface OnGallerySource {
        public void onGallerySelected();
    }

    private Context context;
    private OnCameraSource onCameraSource;
    private OnGallerySource onGallerySource;
    private BottomSheetDialog bottomSheetDialog;

    public PictureSourceChooser(Context context, OnCameraSource onCameraSource, OnGallerySource onGallerySource) {
        this.context = context;
        this.setOnCameraSource(onCameraSource);
        this.setOnGallerySource(onGallerySource);
    }

    public void show() {
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        View v = LayoutInflater.from(context).inflate(R.layout.profile_pic_chooser, null);
        TextView camera = (TextView) v.findViewById(R.id.cameraTextButton);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                handleCameraSource();
            }
        });
        TextView gallery = (TextView) v.findViewById(R.id.galleryTextButton);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                handleGallerySource();
            }
        });
        bottomSheetDialog.contentView(v).show();
    }

    public void handleCameraSource() {
        if (null != onCameraSource)
            onCameraSource.onCameraSelected();
    }

    public void handleGallerySource() {
        if (null != onGallerySource)
            onGallerySource.onGallerySelected();
    }

    /**
     * Interface Setter Methods
     */

    public void setOnCameraSource(OnCameraSource onCameraSource) {
        this.onCameraSource = onCameraSource;
    }

    public void setOnGallerySource(OnGallerySource onGallerySource) {
        this.onGallerySource = onGallerySource;
    }
}