package com.example.localevents;

import generics.HTTPAdapter;
import generics.NetworkConnection;

import java.util.ArrayList;
import java.util.List;

import models.UserModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import parsers.UserParser;
import Adapters.UsersAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class Attendings extends Activity{
	NetworkConnection checknetwork;
	public String url_insert ;
	HTTPAdapter httpadapter ;
	String eventid ;
	public UserParser userparser;
	public UserModel user;
	ListView userlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_listview);
		setTitle("Attending");
		
        getActionBar().setIcon(R.drawable.logo);
        
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#66CDAA"));
        getActionBar().setBackgroundDrawable(colorDrawable);
        url_insert = "http://7girls.byethost7.com/public/event/goingpeople";
        userlist = (ListView) findViewById(R.id.user_listview);
        Intent i = getIntent();
        eventid = i.getStringExtra("event_id");
//        Toast.makeText(getApplicationContext(), "evt id = "+eventid, Toast.LENGTH_LONG).show();
        httpadapter = new HTTPAdapter();
        userparser = new UserParser();
        user = new UserModel();
        
        checknetwork = new NetworkConnection(getApplicationContext());
		
	     boolean connection = checknetwork.isNetworkOnline();
	     if(connection==true)
	     { 
	    		new GetUsers().execute(); 

	    		
	     }				
	     else
			{
				Toast.makeText(getApplicationContext(),"please check your internet connection", Toast.LENGTH_LONG).show();
			}
	}
	public class GetUsers extends AsyncTask<String, String, JSONObject>
	{
		
		public  ProgressDialog pDialog;
		ArrayList<UserModel> res ;
		
//			
				protected void onPreExecute() {
					super.onPreExecute();

					 pDialog = new ProgressDialog(Attendings.this);
			            pDialog.setMessage("please wait . . . ");
			            pDialog.setIndeterminate(false);
			            pDialog.setCancelable(true);
			            pDialog.show();		
				}

		@Override
		protected JSONObject doInBackground(String... arg0)
		{
			
			JSONObject json = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>(); //preparing a list of key/value for mail/password
			params.add(new BasicNameValuePair("event_id",eventid));

//			Log.d("params",params.toString());
			boolean serverconnect = checknetwork.isServerAvaliable(url_insert);
			if(serverconnect==true)
			{	
			 json = httpadapter.makeHttpRequestGet(url_insert, params);
			 
			 //send the list to the web service.
//			 Log.d("iiiiiiddd",eventid);
//			 Log.d("json returned",json.toString());
			 
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			int error = 0 ;
			pDialog.dismiss();
			
			if (result !=null)
			{
//				Toast.makeText(getApplicationContext(), "result "+result, Toast.LENGTH_LONG).show();
				JSONArray jArray = null;
				try {
					jArray = result.getJSONArray("contents");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					 res = new ArrayList<UserModel>();
					
					 for(int i = 0 ; i < jArray.length();i++){
							JSONObject jObj = jArray.getJSONObject(i);
							if(i == 0)
							{
								error= Integer.parseInt(jObj.getString("error"));
								if(error==0)
							 	 {
							 		 continue;
							 	 }
							 	else
							 	 {							 		
							 		 break;
							 	 }
							}
							else
							{
									user.setUser_name(jObj.getString("user_name"));
									user.setUser_image(jObj.getString("user_image"));
									res.add(user);
								
							}
							//Log.d("++++++  ",jObj.getString("id"));
							
							
						
							
							
							
						}

					//send the list view to Adapter 
					userlist.setAdapter(new UsersAdapter(getApplicationContext(), res));
					
					//Listener on click the list item to show event details
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//pDialog.dismiss();
		}
		
		
		
		
		
	}

}
