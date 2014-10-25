package com.example.testglass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Environment;

public class PictureUtils {
	private static final String PREFIX = "relax-";
	private static final String DATEFORMAT = "yyyyMMdd-HHmmss";
	private static final String SUFFIX = ".jpg";

	private static String getFileName() {
		final SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT, Locale.US);
		return PREFIX + sdf.format(Calendar.getInstance().getTime()) + SUFFIX;
	}

	public static void saveToFile(byte[] data) {
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
	}
}
