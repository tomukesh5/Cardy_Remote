package com.cardyapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

	public static String BASE_FOLDER_PATH = File.separatorChar + "CARDY" + File.separatorChar;
	public static String PICTURE_FOLDER_PATH = BASE_FOLDER_PATH + "pics" + File.separatorChar;

	public static String getPath(Activity activity, Uri uri) {
		if (null != uri) {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
			if (null != cursor && cursor.moveToFirst()) {
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				return cursor.getString(columnIndex);
			}
		}
		return null;
	}

	public static boolean isEmpty(String string) {
		return TextUtils.isEmpty(string) || string.contentEquals("null");
	}

	public static boolean isExtStorageAvailable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	public static void createDataDirIfNotExists() {
		if (isExtStorageAvailable()) {
			File root = Environment.getExternalStorageDirectory();
			String path = root.getAbsolutePath() + PICTURE_FOLDER_PATH;
			File picsDirectory = new File(path);
			if (!(picsDirectory.exists() && picsDirectory.isDirectory())) {
				picsDirectory.mkdirs();
			}
		}
	}

	public static String getPictureDirectory() {
		File root = Environment.getExternalStorageDirectory();
		String path = root.getAbsolutePath() + PICTURE_FOLDER_PATH;
		return path;
	}

	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}
}