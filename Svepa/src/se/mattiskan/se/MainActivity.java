package se.mattiskan.se;

import android.os.*;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.EditText;

public class MainActivity extends Activity{
	StatisticsGenerator statistics;
	private boolean hasSetWord = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.enableDefaults();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		statistics = new StatisticsGenerator();
		
		EditText searchTo = (EditText)findViewById(R.id.input_word);
		addTextListener(searchTo);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event){
		if(!hasSetWord)
			return false;
		try {
			if(event.getAction() == MotionEvent.ACTION_MOVE)
				statistics.record(event);
			else if(event.getAction() == MotionEvent.ACTION_UP){
				statistics.close();
			}
			
		} catch (Exception e) {
			Log.d("Mattis", "Awww fuck");
			for(StackTraceElement err : e.getStackTrace()){
				//Log.d("Mattis", err.toString());
			}
			
			System.exit(0);
		}
		return false;
	}
	
	private void addTextListener(EditText searchTo){
	    searchTo.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void afterTextChanged(Editable s) {
	        	String word = s.toString();
	        	if(!word.isEmpty()){
	        		statistics.reset(word);
	        		hasSetWord = true;
	        	}	            
	        }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}
	    });
	}
	
}
