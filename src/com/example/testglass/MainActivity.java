package com.example.testglass;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

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

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_main, container, false);
			rootView.requestFocus(); // require focusable view
			rootView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("tatdbg", "onclick!!!!");
				}
			});

			return rootView;
		}
	}

}
