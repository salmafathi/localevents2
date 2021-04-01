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

import com.squareup.picasso.Picasso;

import parsers.EventParser;

import models.CategoriesHelper;
import models.DBHelper;
import models.EventModel;
import models.InterestsHelper;
import Adapters.EventsAdapter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ShowAllEvents extends Fragment 
{
	ListView displayEvents;
	public String url_insert="http://7girls.byethost7.com/public/event/interestsevents" ;
	NetworkConnection checknetwork;
	HTTPAdapter httpadapter ;
	EventModel model;
	EventParser eventparser;
	String userid ;
	ArrayList<EventModel> events;
	
	/// sql 
	DBHelper db;
	InterestsHelper interestHelper;
	CategoriesHelper catHelper;
	ArrayList<Integer> ints;
	String city ;
	
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

		// sql
		InterestsHelper.cn = getActivity();
		CategoriesHelper.cn = getActivity();
		db = new DBHelper(getActivity());
		interestHelper = new InterestsHelper();
		catHelper = new CategoriesHelper();
	     boolean connection = checknetwork.isNetworkOnline();
	     if(connection==true)
	     { 
	    	 	ints = new ArrayList<Integer>();
	    		ints = getInterestsArray();
	    		city = getCountry();
	    		new GetEvents().execute(); 
	    		Log.d("caaameD", "hi");	
	    		
	    		
	    		
	    		
	    		Toast.makeText(getActivity(),"interests : "+ints.toString(), Toast.LENGTH_LONG).show();
	    		Toast.makeText(getActivity(),"country : "+city, Toast.LENGTH_LONG).show();
	    		
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
		
//			
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
			//
			if(city!=null)
			{
				params.add(new BasicNameValuePair("city",getCountry()));
			}
			if(ints !=null)
			{
				String interests = "";
				for(int i = 0 ; i < ints.size() ; i++){
					interests += ints.get(i)+",";
				}
				params.add(new BasicNameValuePair("interests",interests));
			}

			boolean serverconnect = checknetwork.isServerAvaliable(url_insert);
			if(serverconnect==true)
			{	
			 json = httpadapter.makeHttpRequest(url_insert, "GET", params);
			// Toast.makeText(getActivity(), "user id = "+params+ "id = "+userid, Toast.LENGTH_LONG).show();
			 //send the list to the web service.
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
							// TODO Auto-generated method stub
							//res.get(position); //res is the event model object that holding all data about event...
							//Toast.makeText(getActivity(), "ssssss"+res.get(position).getEvent_description(), Toast.LENGTH_LONG).show();
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
	private ArrayList<Integer> getInterestsArray(){
		 ArrayList<Integer> catArray = new ArrayList<Integer>();
		 
		 Cursor cr_interests =  interestHelper.getInterests();
		    if(cr_interests!=null && cr_interests.getCount()>0)
		    {	
		    cr_interests.moveToFirst();
		    while(cr_interests.isAfterLast() == false)
		    {
			    Integer cat_id = cr_interests.getInt(cr_interests.getColumnIndex("interest_id"));
			    catArray.add(cat_id);
			    cr_interests.moveToNext();
		    }
		    
		    
		    }
		    return catArray;
	 }
	private String getCountry(){
		 String user_city =null;
		Cursor cr =  db.getData(Integer.parseInt(userid));
		
	    if(cr!=null && cr.getCount()>0)
	    {
	    cr.moveToFirst();
	    while(cr.isAfterLast() == false)
	    {
		    
		    user_city = cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_CITY));
		   // Toast.makeText(getActivity(),user_image, Toast.LENGTH_LONG).show();
		    cr.moveToNext();
	    }
	    }
	    db.close();
	    return user_city;
	}
	
}
