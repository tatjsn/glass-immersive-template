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

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
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
			CameraRetriever.get().request(
					new CameraRetriever.OnResultListener() {
						@Override
						public void onResult(Camera result) {
							mCamera = result;
							SurfaceView surface = (SurfaceView) getView()
									.findViewById(R.id.main_surface);
							try {
								mCamera.setPreviewDisplay(surface.getHolder());
							} catch (IOException e) {
								throw new IllegalStateException(e);
							}
							takePicture();
						}
					});

			return rootView;
		}

		@Override
		public void onDestroyView() {
			mHandler.removeCallbacksAndMessages(null);
			CameraRetriever.get().cancel();
			if (mCamera != null) {
				mCamera.release();
			}
			super.onDestroyView();
		}
	}

}
