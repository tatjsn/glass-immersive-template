package com.example.testglass;

import android.hardware.Camera;
import android.os.AsyncTask;

public class CameraRetriever {
	private static CameraRetriever sInstance;
	private OnResultListener mListener;

	public interface OnResultListener {
		public void onResult(Camera result);
	}

	public static CameraRetriever get() {
		if (sInstance == null) {
			sInstance = new CameraRetriever();
		}
		return sInstance;
	}

	public CameraRetriever() {
	}

	public void request(OnResultListener listener) {
		mListener = listener;
		new AsyncTask<Void, Void, Camera>() {

			@Override
			protected Camera doInBackground(Void... arg0) {
				return Camera.open();
			}

			@Override
			protected void onPostExecute(Camera result) {
				if (mListener != null) {
					mListener.onResult(result);
				}
				mListener = null;
			}
		}.execute();
	}

	public void cancel() {
		mListener = null;
	}
}
