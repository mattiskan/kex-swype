package se.mattiskan.se;

import android.os.*;
import android.app.*;
import android.content.DialogInterface;
import android.text.*;
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
			if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN)
				statistics.record(event);
			else if(event.getAction() == MotionEvent.ACTION_UP){
				new AlertDialog.Builder(this)
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("Submit data?")
			        .setMessage("Would you like to submit your last swype?")
    			    .setNegativeButton("No",new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				        	statistics.reset();
				        }
	
			        })
			        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				        	statistics.close();
				        }
	
			        })
			    .show();
				
				
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
