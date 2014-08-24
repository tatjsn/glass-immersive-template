package com.example.testglass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.glass.sample.timer.SetTimerActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.container).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						openOptionsMenu();
					}
				});


		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new MainFragment()).commit();
		}
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

			return true;
		case R.id.action_settings:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
