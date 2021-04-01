package com.example.localevents;

import generics.HTTPAdapter;
import generics.NetworkConnection;
import generics.sharedprefs;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import models.CategoryModel;
import models.CityModel;
import models.DBHelper;
import models.EventModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.localevents.EventsByCategories.GetEvents;

import parsers.CategoryParser;
import parsers.CityParser;
import parsers.EventParser;
import Adapters.Spinner_Cat_Adapt;
import Adapters.Spinner_City_Adapter;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.EventLog.Event;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class Search extends Fragment implements OnClickListener
{
	
	private EditText fromDateEtxt;
    private EditText toDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Button searchEvents;
    NetworkConnection checknetwork;
    Context mcontext;
    
    //Spinners
    Spinner cityspinner;
    private Spinner_City_Adapter cityAdapter;
    String arraySpinner[];
    private Spinner categorySpinner;
    Spinner_Cat_Adapt categoryAdapter;
    
    //urls part
    String url_searchevents="http://7girls.byethost7.com/public/event/search";
    public String url_city="http://7girls.byethost7.com/public/city/list";
    String url_category="http://7girls.byethost7.com/public/category/list";
    
    // search part
    CityParser cityparser;
    EventParser eventparser; 
    EventModel eventmodel;
    HTTPAdapter httpadapter ;
    CityModel cityModel;
    CategoryModel catmodel;
    CategoryParser catparser;
    /// Search keys
    List<NameValuePair> searchData;
    String fromDate;
    String toDate;
    String city;
    String  catname;
    boolean connection;
    String userid ;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.search, container,false);
		    dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		    
		    fromDateEtxt = (EditText)view.findViewById(R.id.etxt_fromdate);    
	        fromDateEtxt.setInputType(InputType.TYPE_NULL);
	        fromDateEtxt.requestFocus();
	        toDateEtxt = (EditText) view.findViewById(R.id.etxt_todate);
	        toDateEtxt.setInputType(InputType.TYPE_NULL);
	        cityspinner = (Spinner) view.findViewById(R.id.citySpinner);
	        searchEvents= (Button) view.findViewById(R.id.SearchEvents);
	        setDateTimeField();
	        
	        
		    checknetwork = new NetworkConnection(getActivity());
		     connection = checknetwork.isNetworkOnline();
		     if(connection==true)
		     { 
		    		new GetCities().execute(); 	
		    		new GetCategories().execute();
		     }				
		     else
			 {
		    	 	Toast.makeText(getActivity(),"please check your internet connection", Toast.LENGTH_LONG).show();
			 }
		     
		
		     searchEvents.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					 checknetwork = new NetworkConnection(getActivity());
				     connection = checknetwork.isNetworkOnline();
				     
					fromDate=fromDateEtxt.getText().toString().trim();
					toDate=toDateEtxt.getText().toString().trim();
					 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
//					 String str1 = "12/10/2013";
				     try {
						java.util.Date date1 = formatter.parse(fromDate);
						java.util.Date date2=  formatter.parse(toDate);
						 if (date1.compareTo(date2)>0)
					      {
							 Toast.makeText(getActivity(), "Sorry todate is greater than from date re-enter them again", Toast.LENGTH_LONG).show();                   
							 fromDateEtxt.setText("");
								toDateEtxt.setText("");
					      }
						 else
						 {
							 Toast.makeText(getActivity(), "search info is: "+fromDate+".."+toDate+"..."+city+"..."+catname, Toast.LENGTH_LONG).show();
								if(connection==true)
								{
									searchData = new ArrayList<NameValuePair>();
									if(!fromDate.equals(""))
									{
										searchData.add(new BasicNameValuePair("fromdate", fromDate));
									}
									if (!toDate.equals(""))
									{
										searchData.add(new BasicNameValuePair("todate",toDate));
									}
									searchData.add(new BasicNameValuePair("city",city));
									searchData.add(new BasicNameValuePair("cat", catname));
									searchData.add(new BasicNameValuePair("user_id", userid));
								}
								
								new  HTTPAsyncHelper().execute();
								fromDateEtxt.setText("");
								toDateEtxt.setText("");
							 
						 }
					    }
				     catch (ParseException e)
				     {
						// TODO Auto-generated catch block
//						e.printStackTrace();
				    	 searchData = new ArrayList<NameValuePair>();
				    	 if(!fromDate.equals(""))
				    	 {
				    		 searchData.add(new BasicNameValuePair("fromdate", fromDate));
				    		 fromDateEtxt.setText("");
				    	 }
				    	 else if(!toDate.equals(""))
				    	 {
				    		 searchData.add(new BasicNameValuePair("todate",toDate));
							toDateEtxt.setText("");
				    	 }
				    	 if(connection==true)
							{
					    	
					    	 searchData.add(new BasicNameValuePair("city",city));
							 searchData.add(new BasicNameValuePair("cat", catname));
							 searchData.add(new BasicNameValuePair("user_id", userid));
							 new  HTTPAsyncHelper().execute();
							}
					 }

				}});
		     
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		 httpadapter = new HTTPAdapter();
		 cityparser= new CityParser(mcontext);
		 cityModel = new CityModel() ;
		 eventparser=new EventParser();
		 eventmodel=new EventModel();
		 catmodel=new CategoryModel();
		 catparser=new CategoryParser();
		 cityspinner = (Spinner)getView().findViewById(R.id.citySpine);
		 categorySpinner = (Spinner)getView().findViewById(R.id.categorySpinner);
		 userid = sharedprefs.getDefaults("ID",getActivity());
		super.onStart();
	}

	
	 private void setDateTimeField() 
	 {
		 fromDateEtxt.setOnClickListener((OnClickListener) this);
	     toDateEtxt.setOnClickListener((OnClickListener) this);
	     Calendar newCalendar = Calendar.getInstance();
	        
	     fromDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
		  public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
		  {
		            Calendar newDate = Calendar.getInstance();
		            newDate.set(year, monthOfYear, dayOfMonth);
//		            newDate.get
		            fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
		  }
		 },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
	        
	        toDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
		        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		            Calendar newDate = Calendar.getInstance();
		          newDate.set(year, monthOfYear, dayOfMonth);
		           
		            toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
		        }

		    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
	 }
	
	 @Override
	public void onClick(View view) {
			if(view == fromDateEtxt) {
				fromDatePickerDialog.show();
			} 
			else if(view == toDateEtxt) 
			{
				toDatePickerDialog.show();
			}		
	 }
	 /********************* To get Cities from wen service *****************************************/
	 class GetCities extends AsyncTask<String, String, JSONObject>
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
				boolean serverconnect = checknetwork.isServerAvaliable(url_city);
				if(serverconnect==true)
				{
				 json = httpadapter.makeHttpRequestWithNoParams(url_city);
				 
				}
				return json;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
//				getResponse
				pDialog.dismiss();
	            if(result != null)
	            	try{
	            		
	            		cityparser.getResponse(result);
	            		ArrayList<CityModel> res = cityparser.cityArrayList;// function of parse the result json object
	            		CityModel cit=new CityModel();
	            		cit.setCity_name("All");
	            		res.add(0, cit);
	            		cityAdapter = new Spinner_City_Adapter(getActivity(),res);
	            		cityspinner.setAdapter(cityAdapter);
	            		cityspinner.setOnItemSelectedListener(new OnItemSelectedListener() 
	            		{

							@Override
							public void onItemSelected(AdapterView<?> parent, View view,
	            					int position, long id) {
									cityspinner.setPrompt("Select City!");
									CityModel cityObject = (CityModel) cityAdapter.getItem(position);
									city=cityObject.getCity_name();
								
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub
								
							}});
	            
	            	}
	            catch (JSONException e){
	            	e.printStackTrace();
	            	}
					
				else           
	            	Toast.makeText(getActivity(), "No data or No server", Toast.LENGTH_LONG).show(); 
				
			}
	}
	 
	  /****************************Get Categories from webservice****************************************/
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
			protected JSONObject doInBackground(String... arg0) {
				 JSONObject json = null;
					boolean serverconnect = checknetwork.isServerAvaliable(url_category);
					if(serverconnect==true)
					{
					 json = httpadapter.makeHttpRequestWithNoParams(url_category);
					 
					}
					return json;
			}
			@Override
			protected void onPostExecute(JSONObject result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				pDialog.dismiss();
			 if(result != null)
			 {
        		try {
					ArrayList<CategoryModel> res =catparser.getResponse(result);
					CategoryModel cat = new CategoryModel();
					cat.setCat_name("All");
					res.add(0,cat);
					categoryAdapter = new Spinner_Cat_Adapt(getActivity(),res);
                    categorySpinner.setAdapter(categoryAdapter);
                    categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            			@Override
            			public void onItemSelected(AdapterView<?> parent, View view,
            					int position, long id) {
            				// TODO Auto-generated method stub
            				
            				categorySpinner.setPrompt("Select your category!");
            				CategoryModel category = (CategoryModel) categoryAdapter.getItem(position);
            				catname = category.getCat_name();
            				
            			
            				//Call the other Asyn for events...............
            				           				
            				
            			}

            			@Override
            			public void onNothingSelected(AdapterView<?> arg0) {
            				// TODO Auto-generated method stub
            				
            			}
            		});
            		    
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			}
			
			
	 
		}
	 /*********************** Async Class for get Request************************************/
	 public class HTTPAsyncHelper extends AsyncTask<JSONObject, Void, JSONObject>{
		 public   ProgressDialog pDialog;
		    	//start progress dialog 		
		 protected void onPreExecute() 
		 {
		    				// TODO Auto-generated method stub
			super.onPreExecute();
			 pDialog = new ProgressDialog(getActivity());
	         pDialog.setMessage("please wait . . . ");
	         pDialog.setIndeterminate(false);
	         pDialog.setCancelable(true);
	         pDialog.show();		
		}
		   	
		   	@Override
		   	protected JSONObject doInBackground(JSONObject... params) {
		   		boolean serverconnect = checknetwork.isServerAvaliable(url_searchevents);
				if(serverconnect==true){
		   			JSONObject json = null;
		   			HTTPAdapter jp = new HTTPAdapter();
		   			for (NameValuePair nvp : searchData) {
		   			    String name = nvp.getName();
		   			    String value = nvp.getValue();
		   			    Log.d(name,value);
		   			}
		   			json = jp.makeHttpRequestGet(url_searchevents, searchData);
		   			Log.d("jsooooooooooooon",json.toString());
		   			
		   			return json;
		   			
		   		}
		   		
		   		else 
		   		{
//		   			Toast.makeText(getActivity(), "No data or No server check network connection", Toast.LENGTH_LONG).show(); 
		   			Log.d("sorry", "no server");
		   			
		   			return null ;
		   		}
		   		
		   	}
		   	
		   	protected void onPostExecute(JSONObject result)
		   	{
		   		
		   		super.onPostExecute(result);
		   		
		   		if(result!=null) //When Server is Available
		   		{
		   			try 
		   			{
		   				pDialog.dismiss();
		   				ArrayList<EventModel> res = new ArrayList<EventModel>();
		   				res= eventparser.getResponse(result);
		   			   
		   			    Log.d("result",res.toString());
		   			   
		   			    if(res.toString() !="[]")
		   			    {
		   			     Intent i = new Intent(getActivity(),Searchbydatecity.class);
		   			    	i.putExtra("events", res);
			   			    Toast.makeText(getActivity(), res.toString(), Toast.LENGTH_LONG).show();
							getActivity().startActivity(i);
		   			    }
		   			    if (res.toString()=="[]")
		   			    {
		   			    	pDialog.dismiss();
		   			    	Toast.makeText(getActivity(), " Sorry There is No events ", Toast.LENGTH_LONG).show();
		   			    }
		   			
						
					} 
		   			catch (JSONException e)
		   			{
			   			Toast.makeText(getActivity(), "No events in related to these search keys", Toast.LENGTH_LONG).show(); 

						e.printStackTrace();
					}
		   		
		   		}
		   		else
		   		{
		   			pDialog.dismiss();
		   			Toast.makeText(getActivity(), " Sorry There is No events ", Toast.LENGTH_LONG).show();
		   		}
		   		
		   		
		   }

		}
}
