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
	private boolean autoChangingWord = false;
	
	
	EditText inputTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.enableDefaults();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		statistics = new StatisticsGenerator();
		changeWord(statistics.word);
		
		inputTextView = (EditText)findViewById(R.id.input_word);
		addTextListener();
		hasSetWord = true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void changeWord(String newWord){
    	autoChangingWord = true;
    	((EditText)findViewById(R.id.input_word)).setText(newWord);
    	autoChangingWord = false;
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
				        	changeWord(statistics.submitData());
				        }
	
			        })
			    .show();				
			}
			
		} catch (Exception e) {
			Log.d("Mattis", "Awww fuck");
			//Log.d("Mattis", e.getMessage());
			for(StackTraceElement err : e.getStackTrace()){
				Log.d("Mattis", err.toString());
			}
			
			System.exit(0);
		}
		return false;
	}
	
	private void addTextListener(){
	    inputTextView.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void afterTextChanged(Editable s) {
	        	String word = s.toString();
	        	if(!word.isEmpty() && !autoChangingWord ){
	        		statistics.newWord(word);
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
