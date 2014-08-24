package com.example.testglass;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.glass.sample.timer.SetTimerActivity;

public class MainActivity extends Activity implements Camera.PictureCallback {
	private final Handler mHandler = new Handler();
	private Camera mCamera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.main_container).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						openOptionsMenu();
					}
				});
	}

	@Override
	protected void onDestroy() {
		stopIntervalShutter();
		mCamera.release();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mCamera != null) {
			startIntervalShutter();
			return;
		}

		CameraRetriever.get().request(
				new CameraRetriever.OnResultListener() {
					@Override
					public void onResult(Camera result) {
						mCamera = result;
						startIntervalShutter();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_interval:
			final Intent setTimerIntent = new Intent(this, SetTimerActivity.class);
			setTimerIntent.putExtra(
					SetTimerActivity.EXTRA_DURATION_MILLIS, 0);
			//startActivityForResult(setTimerIntent, SET_TIMER);
			startActivity(setTimerIntent);
			stopIntervalShutter();
			return true;
		case R.id.action_settings:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		Log.d("tatdbg", "onPictureTaken [jpeg]");
		mCamera.stopPreview();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mCamera.startPreview();
				mCamera.takePicture(null, null, null, MainActivity.this);
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
		SurfaceView surface = (SurfaceView) findViewById(R.id.main_surface);
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
