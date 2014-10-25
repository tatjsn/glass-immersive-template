package com.example.testglass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class PictureUtils {
	private static final String PREFIX = "relax-";
	private static final String DATEFORMAT = "yyyyMMdd-HHmmss";
	private static final String SUFFIX = ".jpg";

	private static String getFileName() {
		final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT, Locale.US);
		return PREFIX + sdf.format(Calendar.getInstance().getTime()) + SUFFIX;
	}

    private static ContentValues getContentValues(File file, long time) {
        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.TITLE, file.getName());
        values.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.DATE_MODIFIED, time / 1000);
        values.put(MediaStore.Images.Media.DATE_ADDED, time / 1000);
        values.put(MediaStore.Images.Media.ORIENTATION, 0);
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.SIZE, file.length());
        // This is a workaround to trigger the MediaProvider to re-generate the
        // thumbnail.
        values.put(MediaStore.Images.Media.MINI_THUMB_MAGIC, 0);
        return values;
    }

	public static void saveToFile(Context context, byte[] data) {
		File path = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File file = new File(path, getFileName());
		path.mkdirs();
		FileOutputStream s = null;
		try {
			s = new FileOutputStream(file);
			s.write(data);
		} catch (FileNotFoundException e) {
			// Do nothing
			throw new IllegalStateException(e);
		} catch (IOException e) {
			// Do nothing
			throw new IllegalStateException(e);
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					// Do nothing
				}
			}
		}
        ContentValues values = getContentValues(file, System.currentTimeMillis());
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // This cant help images being added into timeline  
        // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }
}
