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

import parsers.CategoryParser;
import parsers.EventParser;

//import com.example.localevents.HomeUser.GetCategories;

import models.EventModel;
import models.CategoryModel;
import Adapters.EventsAdapter;
import Adapters.EventsAdapter;
import Adapters.Spinner_Cat_Adapt;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class EventsByCategories extends Fragment  {

	
	private List<EventModel> EventsList = new ArrayList<EventModel>();
    private List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
    
    private ListView listEvents;
    private Spinner categorySpinner;
    
    private EventsAdapter eventadapter;
    private Spinner_Cat_Adapt categoryAdapter;
    
    
    NetworkConnection checknetwork;
	public String url_insert ;
	public String url_events ;
	HTTPAdapter httpadapter ;
	CategoryParser catparser;
	CategoryModel model  ;
	String userid ;
        

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.events_by_categories, container,false);
		
		checknetwork = new NetworkConnection(getActivity());
		
	     boolean connection = checknetwork.isNetworkOnline();
	     if(connection==true)
	     { 
	    		new GetCategories().execute(); 

	    		
	     }				
	     else
			{
				Toast.makeText(getActivity(),"please check your internet connection", Toast.LENGTH_LONG).show();
			}
		return view; 
  
	}

		
	
	
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		 httpadapter = new HTTPAdapter();
		 catparser= new CategoryParser();
		 model = new CategoryModel() ;
		 userid = sharedprefs.getDefaults("ID",getActivity());
		
		categorySpinner = (Spinner)getView().findViewById(R.id.citySpinner);
		listEvents = (ListView) getView().findViewById(R.id.address_listview);
		
		
		url_insert = "http://7girls.byethost7.com/public/category/list";
		url_events = "http://7girls.byethost7.com/public/event/viewbycategory?name=";
	}






	class GetCategories extends AsyncTask<String, String, JSONObject>
	{
		public   ProgressDialog pDialog;
		//start progress dialog 		
				protected void onPreExecute() {
					// TODO Auto-generated method stub
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
			 boolean serverconnect = checknetwork.isServerAvaliable(url_insert);
			 if(serverconnect==true)
			{
				 
			 json = httpadapter.makeHttpRequestWithNoParams(url_insert);
			 //send the list to the web service.
			 
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			getResponse

            if(result != null)
            	try{
            		pDialog.dismiss();
            		ArrayList<CategoryModel> res =catparser.getResponse(result);   // function of parse the result json object         		
            		
            		categoryAdapter = new Spinner_Cat_Adapt(getActivity(),res);
//                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            		

                    categorySpinner.setAdapter(categoryAdapter);
                   
              //      categorySpinner.setPopupBackgroundResource( R.layout.events_by_categories);
            		categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            			@Override
            			public void onItemSelected(AdapterView<?> parent, View view,
            					int position, long id) {
            				// TODO Auto-generated method stub
            				
            				categorySpinner.setPrompt("Select your category!");
            				CategoryModel category = (CategoryModel) categoryAdapter.getItem(position);
            				String catname = category.getCat_name();
            				
            				new GetEvents().execute(catname);
            				//Call the other Asyn for events...............
            				           				
            				
            			}

            			@Override
            			public void onNothingSelected(AdapterView<?> arg0) {
            				// TODO Auto-generated method stub
            				
            			}
            		});
            		      		
            	
            	}
            catch (JSONException e){
            	e.printStackTrace();
            	}
				
			else           
            	Toast.makeText(getActivity(), "No data or No server", Toast.LENGTH_LONG).show(); 
			
		}
}
	
	
	
	
	
	class GetEvents extends AsyncTask<String, String, JSONObject>
	{
		EventParser eventparse = new EventParser();
		ArrayList<EventModel> res ;

		public   ProgressDialog pDialog;
		//start progress dialog 		
				protected void onPreExecute() {
					// TODO Auto-generated method stub
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
			 List<NameValuePair> params = new ArrayList<NameValuePair>(); //preparing a list of key/value for userid
			 params.add(new BasicNameValuePair("user_id",userid));
			 boolean serverconnect = checknetwork.isServerAvaliable(url_events);
			 if(serverconnect==true)
			{
				 
			// json = httpadapter.makeHttpRequestWithNoParams(url_events+arg0[0]);
			 json = httpadapter.makeHttpRequestWithNoParams(url_events+arg0[0]+"&user_id="+userid);
			 
			 Log.d("this is events jsooooon",json.toString());
			 //send the list to the web service.
			 
			}
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
			super.onPostExecute(result);
//			getResponse

            if(result != null){
            	
            	try {
				    res =eventparse.getResponse(result);
					listEvents.setAdapter(new EventsAdapter(getActivity(), res));
					
					listEvents.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							  Intent i = new Intent(getActivity(),com.example.localevents.Event_Datails.class);
						      i.putExtra("event_details", res.get(position));
							  getActivity().startActivity(i);
						}
					});
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
//            	Toast.makeText(getApplicationContext(),""+result, Toast.LENGTH_LONG).show();
            }
            	  	
            	
            else {          
            	Toast.makeText(getActivity(), "No data or No server", Toast.LENGTH_LONG).show(); }
			
		}
}

	
}
