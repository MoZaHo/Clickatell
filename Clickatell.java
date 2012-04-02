package com.clickatell;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

/*
 * Clickatell Library to send message using a Clickatell HTTP Api
 * and Androids SharedPreferences Library
 * 
 * @author : Morne Oosthuizen 
 */
public class Clickatell extends Activity {
	
	public SharedPreferences sp;
	public SharedPreferences user_settings;
	
	public String sUsername;
	public String sPassword;
	public String sApiId;
	public String baseUrl = "http://api.clickatell.com/http/";
	
	/*
	 * Load Preferences and save it to variables
	 * 
	 * @author Morne Oosthuizen
	 * 
	 */
	
	public void onCreate(Bundle savedInstanceState) {
            
		sp = PreferenceManager.getDefaultSharedPreferences(this);
        	sUsername = sp.getString("settings_username", "notset");
        	sPassword = sp.getString("settings_password","notset");
        	sApiId = sp.getString("settings_api_id","notset");
        
	}
	
	/*
	 * Create new thread to fetch balance. Using thread else the app will halt
	 * while it tries to open url
	 */
	public void GetBalance()
	{
		
		new Thread(new Runnable() {
   
			public void run() {
				
				String sBalance = "";
				
				// TODO Auto-generated method stub
				try {
					URL url = new URL(baseUrl + "getbalance?user=" + sUsername + "&password=" + sPassword + "&api_id=" + sApiId);
	            	
	            			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream())); 
	
	            			String line;
	            			while ((line = br.readLine()) != null)
	            			{
	            				sBalance = line;
	            			}
	            	
	            		} catch (Exception e)
	            		{
	            			//Handle Exception
	            		}
			}
            
    		}).start();
		
	}
	
	/*
	 * Send a message. 
	 * Using a new thread so app doesn't stall while trying to send.
	 */
	public void sendMessage() {
		
		Thread action = new Thread()
		{
		 
			public void run()
			{
				try
				{
		    	  		EditText telNumberTmp = (EditText)findViewById(R.id.contacts);
		    	  		String telNumber = telNumberTmp.getText().toString();
					
					EditText msgTextTmp = (EditText)findViewById(R.id.sSmsBody);
					String msgText = msgTextTmp.getText().toString();
					
					msgText = msgText.replace(" ", "+");
					
					try {
						String tStatus = "";
						URL url = new URL(baseUrl + "sendmsg?api_id=" + sApiId + "&user=" + sUsername + "&password=" + sPassword + "&to=" + telNumber + "&text=" + msgText);

						BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
												
		            			String line;
		            			while ((line = br.readLine()) != null)
		            			{
		            				tStatus = line;
		            			}
		            	
		            			if (tStatus.startsWith("ERR:"))
		            			{
		            				//Display Error Message
		            			} else {
		            				//Display Confirm Message
		            			}
						
					} catch (Exception e)
					{
						//Handle Exception
					}
		 
		      		} catch (Exception e) {  }
		 
		  	}
		 
		};
		 
		this.runOnUiThread(action);

	}
	
}
