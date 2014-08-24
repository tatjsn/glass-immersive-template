package com.example.testglass;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
		private Camera mCamera;
		private final Handler mHandler = new Handler();

		private class ShutterCallback implements Camera.ShutterCallback {
			@Override
			public void onShutter() {
				Log.d("tatdbg", "onShutter");
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if (getActivity() == null) {
							return;
						}
						takePicture();
					}
				}, 5 * 1000);
			}

		}

		private class JpegCallback implements Camera.PictureCallback {
			private String getFileName() {
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
				final String dateTime = sdf.format(Calendar.getInstance().getTime());
				return "relax-" + dateTime + ".jpg";
			}

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				Log.d("tatdbg", "onPictureTaken [jpeg]");
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
			mCamera.takePicture(new ShutterCallback(), null, null, new JpegCallback());
		}

		public PlaceholderFragment() {
			setRetainInstance(true);
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
					if (getActivity() == null) {
						return;
					}
					takePicture();
				}
			}, 1 * 1000);

			return rootView;
		}

		@Override
		public void onPause() {
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
			super.onPause();
		}
	}

}
