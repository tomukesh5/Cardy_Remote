package com.cardyapp.Utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageBase64Convertion {


    private ImageBase64Convertion() {
        super();
    }

    public static byte[] ImagePathToByteArr(String imgPath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();

    }

    public static String encode(String imagePath) {
        return encode(ImagePathToByteArr(imagePath));
    }

    /**
     * Encode some data and return a String.
     */
    public static String encode(byte[] d) {
        return Base64.encodeToString(d, 0);
    }

    /**
     * Decode data and return bytes.  Assumes that the data passed
     * in is ASCII text.
     */
    public static byte[] decode(byte[] data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    public static byte[] decode(String stringBase64) {
        return Base64.decode(stringBase64, Base64.DEFAULT);
    }

}
