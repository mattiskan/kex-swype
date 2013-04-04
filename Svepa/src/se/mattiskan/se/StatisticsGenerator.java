package se.mattiskan.se;

import java.io.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.*;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;
import android.view.*;

public class StatisticsGenerator {
	String word;
	private FileWriter filewriter;
	private long referenceTime;
	private JSONObject wordStatistics;
	private JSONArray swypeCoordinates;
	
	public StatisticsGenerator() {
		referenceTime = System.nanoTime()/1000000;
	}	
	
	public void record(MotionEvent event){
		try{
			
			final int historySize = event.getHistorySize();			
			for (int h = 0; h < historySize; h++) {
				
				JSONObject hcoordinate = newCoordinate(event.getHistoricalEventTime(h), 
						event.getHistoricalX(0, h), event.getHistoricalY(0, h));
				swypeCoordinates.put(hcoordinate);
			}
			
			JSONObject coordinate = newCoordinate(event.getEventTime(), event.getX(0), event.getY(0));
			swypeCoordinates.put(coordinate);

		} catch (JSONException e){
			e.printStackTrace();
		}
	}
	
	private JSONObject newCoordinate(long t, float x, float y) throws JSONException{
		JSONObject coordinate = new JSONObject();
		coordinate.put("time", t - referenceTime);
		coordinate.put("x", x);
		coordinate.put("y", y);
		return coordinate;
	}
	
	public void reset() {
		reset(word);
	}

	
	public void reset(String word){
		try {
			this.word = word;
			wordStatistics = new JSONObject();
			swypeCoordinates = new JSONArray();
			wordStatistics.put("word", word);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			wordStatistics.put("data", swypeCoordinates);
			
			/*String path = Environment.getExternalStorageDirectory().getPath();
			filewriter = new FileWriter(new File(path+"/svepa",wordStatistics.getString("word")+".json") ,true);
			filewriter.write(wordStatistics.toString(2));
			filewriter.close();*/
			
			submitData();
			reset(word);
			
		} catch (JSONException e) { 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private void submitData() throws IOException, JSONException {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://mattiskan.se/projects/kex/submit.php?word="+word);

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("json", wordStatistics.toString()));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        Log.d("Mattis", "Submitted " + wordStatistics.getString("word"));
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    }
	}
}
