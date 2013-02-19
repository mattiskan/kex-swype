package se.mattiskan.se;

import java.io.*;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.*;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event){
		try {
		    FileOutputStream out = openFileOutput("samplefile.txt", MODE_WORLD_READABLE);
		    OutputStreamWriter osw = new OutputStreamWriter(out); 
		    
			final int historySize = event.getHistorySize();
			final int pointerCount = event.getPointerCount();
			
			for (int h = 0; h < historySize; h++) {
			    Log.d("Mattis", String.format("At time %d:", event.getHistoricalEventTime(h)));
				for (int p = 0; p < pointerCount; p++) {
				    Log.d("Mattis", String.format("  pointer %d: (%f,%f)",
				            event.getPointerId(p), event.getHistoricalX(p, h), event.getHistoricalY(p, h)));
				}
			}
			
			Log.d("Mattis2", String.format("At time %d:", event.getEventTime()));
			for (int p = 0; p < pointerCount; p++) {
			    Log.d("Mattis2", String.format("  pointer %d: (%f,%f)",
			        event.getPointerId(p), event.getX(p), event.getY(p)));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
