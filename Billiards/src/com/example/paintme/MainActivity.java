package com.example.paintme;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	static public boolean clear = false;
	static public int mK = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
//		menu.add(R.string.clear);
//		menu.add(R.string.more);
//		menu.add(R.string.less);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.clear){
//			Log.i("MAIN", "clicked");
			clear = true;
		}
//		else if(item.getItemId() == R.id.speed_down){
//			mK = -1;
//		}
//		else if(item.getItemId() == R.id.speed_up){
//			mK = 1;
//		}
	  return false;
	}


}
