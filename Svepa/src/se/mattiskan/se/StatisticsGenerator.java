package se.mattiskan.se;

import java.io.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.*;
import org.apache.http.protocol.HTTP;
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
		setNewWord();
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
		newWord(word);
	}

	
	public void newWord(String word){
		try {
			Log.d("Mattis", "UTF-8: "+word);
			this.word = word;
			wordStatistics = new JSONObject();
			swypeCoordinates = new JSONArray();
			wordStatistics.put("word", word);
//			wordStatistics.put("word", new String(word.getBytes("ISO-8859-1"), "UTF-8"));
		}// catch(UnsupportedEncodingException e){ e.printStackTrace();} 
		catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void setNewWord(){
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://mattiskan.se/projects/kex/submit.php");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        newWord(EntityUtils.toString(response.getEntity(), "UTF-8"));
	        
	    } catch (Exception e) {
	    	Log.d("Mattis", "WordGet failed :(");
	    	System.exit(1);
	    }
	}

	
	public String submitData() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://mattiskan.se/projects/kex/submit.php?word="+word);

	    try {
			wordStatistics.put("data", swypeCoordinates);
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("json", wordStatistics.toString()));
	        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs);
	        entity.setContentEncoding(HTTP.ISO_8859_1);
	        httppost.setEntity(entity);

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        newWord(EntityUtils.toString(response.getEntity()));
	        
	        //Log.d("Mattis", "New word: "+ word);
	    } catch (Exception e) {
	    	Log.d("Mattis", "Submition failed :(");
	    	System.exit(1);
	    }
	    return word;
	}
}
