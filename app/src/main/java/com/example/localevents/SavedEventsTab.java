package com.example.localevents;

import generics.HTTPAdapter;
import generics.NetworkConnection;
import generics.sharedprefs;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import models.EventModel;
import parsers.EventParser;
import Adapters.EventsAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SavedEventsTab   extends Fragment 
{
	ListView displayEvents;

	public String url_savejoin="http://7girls.byethost7.com/public/userevent/getuserevent" ;
//	public String url_insert="http://7girls.byethost7.com/public/event/viewallevent" ;
	NetworkConnection checknetwork;
	HTTPAdapter httpadapter ;
	EventModel model;
	EventParser eventparser;
	String userid ;
	ArrayList<EventModel> events;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View view = inflater.inflate(R.layout.activity_display_events, container,false);
		 httpadapter = new HTTPAdapter();
		 model= new EventModel();
		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		events= new ArrayList<EventModel>();
		checknetwork = new NetworkConnection(getActivity());
		displayEvents = (ListView) getView().findViewById(R.id.displayallevents);
		userid = sharedprefs.getDefaults("ID",getActivity());	

		
	     boolean connection = checknetwork.isNetworkOnline();
	     if(connection==true)
	     { 
	    		new GetEvents().execute(); 
	    		Log.d("caaameD", "hi");	
	    		
	    		
	    		
	     }
	     else
			{
				Toast.makeText(getActivity(),"please check your internet connection", Toast.LENGTH_LONG).show();
			}
	}
	
	public class GetEvents extends AsyncTask<String, String, JSONObject>
	{
		
		public   ProgressDialog pDialog;
		ArrayList<EventModel> res ;
		
		
				protected void onPreExecute() {
				super.onPreExecute();

				 pDialog = new ProgressDialog(getActivity());
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
			params.add(new BasicNameValuePair("user_id",userid));
			params.add(new BasicNameValuePair("type","save"));
			boolean serverconnect = checknetwork.isServerAvaliable(url_savejoin);
			if(serverconnect==true)
			{
			 json = httpadapter.makeHttpRequest(url_savejoin, "GET", params);
		
			 Log.d("iiiiiiddd",userid);
			 
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			
			if (result !=null)
			{
				
				try {
					 res = new ArrayList<EventModel>();
					eventparser = new EventParser();
					res= eventparser.getResponse(result);

					//send the list view to Adapter 
					displayEvents.setAdapter(new EventsAdapter(getActivity(), res));
					
					//Listener on click the list item to show event details
					displayEvents.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							  Intent i = new Intent(getActivity(),com.example.localevents.Event_Datails.class);
						      i.putExtra("event_details", res.get(position));
							  getActivity().startActivity(i);
						}
					});
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//pDialog.dismiss();
		}
		
		
		
		
		
	}
	
	
	
	

}
