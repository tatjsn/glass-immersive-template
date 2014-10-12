package com.example.testglass;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements Camera.PictureCallback {
	private final Handler mHandler = new Handler();
	private Camera mCamera;
	private SurfaceHolder mSurfaceHolder;
	private boolean mSurfaceReady;
	private TextView mTextInterval;
	private int mConfigId = R.id.action_interval_5s;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSurfaceHolder = ((SurfaceView) findViewById(R.id.main_surface)).getHolder();
		mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				mSurfaceReady = true;
				startIntervalShutterIfReady();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}
		});

		CameraRetriever.get().request(
				new CameraRetriever.OnResultListener() {
					@Override
					public void onResult(Camera result) {
						mCamera = result;
						startIntervalShutterIfReady();
					}
				});

		findViewById(R.id.main_container).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						openOptionsMenu();
					}
				});
		mTextInterval = (TextView) findViewById(R.id.main_interval);
		mTextInterval.setText(Config.get(mConfigId).stringId);
	}

	@Override
	protected void onDestroy() {
		stopIntervalShutter();
		mCamera.release();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// FIXME Menu disappear after onPause,
		// interval display will be inconsistent
		stopIntervalShutter();
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		Config config = Config.get(itemId);
		if (config == null) {
			return super.onOptionsItemSelected(item);
		}
		mConfigId = itemId;
		mTextInterval.setText(Config.get(mConfigId).stringId);
		// TODO Check if both surface and camera are ready
		startIntervalShutterIfReady();
		return true;
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
		}, Config.get(mConfigId).interval);
		new AsyncTask<byte[], Void, Void>() {
			@Override
			protected Void doInBackground(byte[]... params) {
				PictureUtils.saveToFile(params[0]);
				return null;
			}
		}.execute(data);
	}

	private void startIntervalShutterIfReady() {
		if (Config.get(mConfigId).interval <= 0) {
			// O means stop interval
			return;
		}
		if (!mSurfaceReady || mCamera == null) {
			return;
		}
		Log.d("tatdbg", "start interval "
				+ getResources().getString(Config.get(mConfigId).stringId));
		try {
			mCamera.setPreviewDisplay(mSurfaceHolder);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		mCamera.startPreview();
		mCamera.takePicture(null, null, null, this);
	}

	private void stopIntervalShutter() {
		Log.d("tatdbg", "stop interval "
				+ getResources().getString(Config.get(mConfigId).stringId));
		// TODO Cancel surface initialize callback
		CameraRetriever.get().cancel();
		mHandler.removeCallbacksAndMessages(null);
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
		}
	}
}
