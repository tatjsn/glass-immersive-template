package com.example.testglass;

import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {
	Camera mCamera;

	private void takePicture() {
		if (mCamera == null) {
			mCamera = Camera.open();
			SurfaceView surface = (SurfaceView) findViewById(R.id.main_surface);
			try {
				mCamera.setPreviewDisplay(surface.getHolder());
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		mCamera.startPreview();
		mCamera.takePicture(
				new Camera.ShutterCallback() {
					@Override
					public void onShutter() {
						Log.d("tatdbg", "onShutter");
					}
				}, null, null,
				new Camera.PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						Log.d("tatdbg", "onPictureTaken [jpeg]");
					}
				});
	}

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
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
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

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_main, container, false);
			rootView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("tatdbg", "onclick!!!!");
					((MainActivity) getActivity()).takePicture();
				}
			});

			return rootView;
		}
	}

}
