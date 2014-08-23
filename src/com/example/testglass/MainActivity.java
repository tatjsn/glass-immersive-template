package com.example.testglass;

import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		Log.d("tatdbg", "onCreate");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("tatdbg", "onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("tatdbg", "onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("tatdbg", "onResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("tatdbg", "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("tatdbg", "onStop");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private static final String WAKELOCK_TAG = "tag";
		private Camera mCamera;
		private final Handler mHandler = new Handler();
		private float mSavedScreenBrightness;

		private void dimScreen() {
			WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
			mSavedScreenBrightness = lp.screenBrightness;
			lp.screenBrightness = 0;
			getActivity().getWindow().setAttributes(lp);
		}

		private void undimScreen() {
			WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
			lp.screenBrightness = mSavedScreenBrightness;
			getActivity().getWindow().setAttributes(lp);
		}

		private class ShutterCallback implements Camera.ShutterCallback {
			@Override
			public void onShutter() {
				Log.d("tatdbg", "onShutter");
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Log.d("tatdbg", "goToSleep held=" + mWakeLock.isHeld());
						dimScreen();

						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								undimScreen();
								takePicture();
							}
						}, 5 * 1000);

					}
				}, 1 * 1000);
			}

		}
		private void takePicture() {
			Log.d("tatdbg", "take picture");
			if (mCamera == null) {
				mCamera = Camera.open();
				SurfaceView surface =
						(SurfaceView) getView().findViewById(R.id.main_surface);
				try {
					mCamera.setPreviewDisplay(surface.getHolder());
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}
			mCamera.startPreview();
			mCamera.takePicture(new ShutterCallback(), null, null,
					new Camera.PictureCallback() {
						@Override
						public void onPictureTaken(byte[] data, Camera camera) {
							Log.d("tatdbg", "onPictureTaken [jpeg]");
						}
					});
		}

		private PowerManager mPowerManager;
		private PowerManager.WakeLock mWakeLock;

		public PlaceholderFragment() {
			setRetainInstance(true);
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			mPowerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
			mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKELOCK_TAG);
			mWakeLock.setReferenceCounted(false);
			// mWakeLock.acquire(); // No meaning in glass app
			WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
			mSavedScreenBrightness = lp.screenBrightness;
		}

		@Override
		public void onDetach() {
			super.onDetach();
			mWakeLock.release();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_main, container, false);
			// Camera wont work without delay
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					takePicture();
				}
			}, 1 * 1000);

			return rootView;
		}

		@Override
		public void onPause() {
			Log.d("tatdbg", "pause lock=" + mWakeLock.isHeld());
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
			super.onPause();
		}
	}

}
