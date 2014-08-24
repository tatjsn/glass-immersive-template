package com.example.testglass;

import java.io.IOException;

import android.app.Fragment;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment implements Camera.PictureCallback {
	private final Handler mHandler = new Handler();
	private Camera mCamera;

	public MainFragment() {
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_main, container, false);
		CameraRetriever.get().request(
				new CameraRetriever.OnResultListener() {
					@Override
					public void onResult(Camera result) {
						mCamera = result;
						startIntervalShutter();
					}
				});

		return rootView;
	}

	@Override
	public void onDestroyView() {
		stopIntervalShutter();
		mCamera.release();
		super.onDestroyView();
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.d("tatdbg", "onPictureTaken [jpeg]");
		mCamera.stopPreview();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mCamera.startPreview();
				mCamera.takePicture(null, null, null, MainFragment.this);
			}
		}, 5 * 1000);
		new AsyncTask<byte[], Void, Void>() {
			@Override
			protected Void doInBackground(byte[]... params) {
				PictureUtils.saveToFile(params[0]);
				return null;
			}
		}.execute(data);
	}

	private void startIntervalShutter() {
		SurfaceView surface = (SurfaceView) getView()
				.findViewById(R.id.main_surface);
		try {
			mCamera.setPreviewDisplay(surface.getHolder());
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		mCamera.startPreview();
		mCamera.takePicture(null, null, null, this);
	}

	private void stopIntervalShutter() {
		CameraRetriever.get().cancel();
		mHandler.removeCallbacksAndMessages(null);
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}
}