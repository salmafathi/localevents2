package generics;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import generics.HTTPAdapter;
import com.example.localevents.Login;
import com.example.localevents.MainActivityUser;
import com.example.localevents.ShowAllEvents;
import com.example.localevents.ShowAllEvents.GetEvents;

import models.DBHelper;
import models.EventModel;
import models.JoinedEventsHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class JoinEvents {
	
	static String userid ;
	static String eventid = "";
	static String eventname ;
	static String eventdate ;
	static String eventtime ;
	static String eventphoto ;
	static String eventlocation ;
	static String eventdescription ;
	static String eventorg ;
	
	static String jointype = null;
	static String url_join ;
	static String url_unjoin ;
	static String url_save;
	static String url_unsave;
	static Context context;
	EventModel event;
	HTTPAdapter httpadapter = new HTTPAdapter(); 
	static NetworkConnection checknetwork;
	
	
	//constructor
	public JoinEvents(Context context){
		JoinEvents.context = context ;
	}

	
	//join function takes all event detail and id to insert it in SQLite and send event id+user id to web service.... 
	public  void join(Context context, String event_id, String event_name, String event_date, String event_time,
			String event_photo, String event_location, String event_description,String event_org)
	{
		 eventid=event_id;
		 eventname = event_name ;
		 eventdate = event_date;
		 eventtime = event_time ;
		 eventphoto = event_photo ;
		 eventlocation = event_location ;
		 eventdescription = event_description ;
		 eventorg = event_org ;
		 JoinEvents.jointype = "join";
		 
		 userid = sharedprefs.getDefaults("ID",context);
		//Toast.makeText(context, userid+" Event ID: " + event_id,Toast.LENGTH_LONG).show();	
		
		  url_join = "http://7girls.byethost7.com/public/userevent/add";
		 checknetwork = new NetworkConnection(context);
		  boolean connection = checknetwork.isNetworkOnline(); //check Internet connection.
			if(connection == true)
			{
				new SendUserInfo().execute();
			}
			else
			{
				Toast.makeText(context,"please check your internet connection", Toast.LENGTH_LONG).show();
			}
			
		 
		
		
	}

	public class SendUserInfo extends AsyncTask<String, String, JSONObject>{
		public   ProgressDialog pDialog;
		protected void onPreExecute() {
//			// TODO Auto-generated method stub
			super.onPreExecute();
			 pDialog = new ProgressDialog(context);
	            pDialog.setMessage("please wait . . . ");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();		
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean serverconnect = checknetwork.isServerAvaliable(url_join); //check server is available or not.
			if(serverconnect==true) //check Server is Available....
			{
				JSONObject json = null;
				List<NameValuePair> param = new ArrayList<NameValuePair>(); //preparing a list of key/value for mail/password
				param.add(new BasicNameValuePair("user_id",userid));
				param.add(new BasicNameValuePair("event_id",eventid));
				param.add(new BasicNameValuePair("type",jointype));
			 json = httpadapter.makeHttpRequest(url_join,"GET", param);  //send the list to the web service.
				
			return json; //return the response from the web service.
			}
			else{
				return null ;
				}
			
		}
		
		@Override
		protected void onPostExecute(JSONObject result) 
		{

			// TODO Auto-generated method stub
			
			super.onPostExecute(result);		
			
            if(result != null)
            {
            	pDialog.dismiss();
            	try {
            			parseJoin(result);
					} 
            	catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	//Toast.makeText(context, "joined successfully thank you ", Toast.LENGTH_LONG).show(); 
            	
            }
				
			else           
            	Toast.makeText(context, "No data join or No server", Toast.LENGTH_LONG).show(); 
		}
		
		
		protected void parseJoin(JSONObject result) throws JSONException
		{
			
			/*
			 *     {"contents":[{ 	"error":0	}]			
			 *     }

			 */
			JSONArray jArray = result.getJSONArray("contents");
			for(int i = 0 ; i < jArray.length();i++)
			{
				JSONObject jObj = jArray.getJSONObject(i);
				int errormap = Integer.parseInt(jObj.getString("error"));
				if(errormap==0)  //if successed insert the event that user joined in the service
				{
						 
					Toast.makeText(context, "Event : "+ eventname +"  has inserted in webservice Successfully", Toast.LENGTH_LONG).show();
					 //ShowAllEvents a = new ShowAllEvents();
					  //a.new GetEvents().execute() ;
				      //save events details  in SQLite database
//					  JoinedEventsHelper event = new JoinedEventsHelper(context);					  
//					  Boolean inserted = event.insertEvent(Integer.parseInt(eventid), eventname, eventdate, null, eventphoto, eventdescription, null, eventtime, eventlocation, eventorg);
//					  									//(int event_id,String event_name,String event_start_date,String event_end_date,String event_photo,String event_description,String event_end_time,String event_start_time,String location, String org_name)
//				      if(inserted)
//				      {
//				    	  Toast.makeText(context, "Event : "+eventname+"  has inserted in SQLite Successfully", Toast.LENGTH_LONG).show();
//				      }
//				     
				      
				}
				
				
				else if(errormap == 1)
				{
					Toast.makeText(context, "You have joined before", Toast.LENGTH_LONG).show();
				}
				else
				{
					
					Toast.makeText(context, "No Data to insert it ! you send empty data to server", Toast.LENGTH_LONG).show();
				}
				
			}
	
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	//////////////////////////////////////// UnJoin Event ////////////////////////////////////////////
	public  void unjoin(Context context, String event_id)
	{
		 eventid=event_id;
		 JoinEvents.jointype = "join";
		 userid = sharedprefs.getDefaults("ID",context);
		//Toast.makeText(context, userid+" Event ID: " + event_id,Toast.LENGTH_LONG).show();	
		
		  url_unjoin = "http://7girls.byethost7.com/public/userevent/delete";
		 checknetwork = new NetworkConnection(context);
		  boolean connection = checknetwork.isNetworkOnline(); //check Internet connection.
			if(connection == true)
			{
				new SendEventInfo().execute();
			}
			else
			{
				Toast.makeText(context,"please check your internet connection", Toast.LENGTH_LONG).show();
			}
			
	}
	
	
	public class SendEventInfo extends AsyncTask<String, String, JSONObject>{
		public   ProgressDialog pDialog;
		protected void onPreExecute() {
//			// TODO Auto-generated method stub
			super.onPreExecute();
			 pDialog = new ProgressDialog(context);
	            pDialog.setMessage("please wait . . . ");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();		
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean serverconnect = checknetwork.isServerAvaliable(url_unjoin); //check server is available or not.
			if(serverconnect==true) //check Server is Available....
			{
				JSONObject json = null;
				List<NameValuePair> param = new ArrayList<NameValuePair>(); //preparing a list of key/value for mail/password
				param.add(new BasicNameValuePair("user_id",userid));
				param.add(new BasicNameValuePair("event_id",eventid));
				Log.d("unjointyyype",jointype+" event_id : "+eventid);
				param.add(new BasicNameValuePair("type",jointype));
				Log.d("url", "url for unjoin" + url_unjoin);
			 json = httpadapter.makeHttpRequest(url_unjoin,"GET", param);  //send the list to the web service.
			 Log.d("M.Gabr.jsooon", "result is : "+json.toString());
				
			return json; //return the response from the web service.
			}
			else{
				Log.d("M.Gabr.jsooon", "connection error");
				return null ;
				}
			
		}
		
		@Override
		protected void onPostExecute(JSONObject result) 
		{

			// TODO Auto-generated method stub
			
			super.onPostExecute(result);		
			
            if(result != null)
            {
            	pDialog.dismiss();
            	try {
            			Log.d("M.Gabr.jsooon", result.toString());
            			parseUnjoin(result);
            			
            			
					} 
            	catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	//Toast.makeText(context, "joined successfully thank you ", Toast.LENGTH_LONG).show(); 
            	
            }
				
			else           
            	Toast.makeText(context, "No data unjoin or No server", Toast.LENGTH_LONG).show(); 
		}
		
		
		protected boolean parseUnjoin(JSONObject result) throws JSONException
		{
			
			/*
			 *     {"contents":[{ 	"error":0	}]			
			 *     }

			 */
			JSONArray jArray = result.getJSONArray("contents");
			String myID = null ;			
//			for(int i = 0 ; i < jArray.length();i++)
//			{
				JSONObject jObj = jArray.getJSONObject(0);
				int errormap = Integer.parseInt(jObj.getString("error"));
				if(errormap==0)  //if successed insert the event that user joined in the service
				{
						 
					Toast.makeText(context, "Event : "+ eventname +"  has deleted from webservice Successfully", Toast.LENGTH_LONG).show();
					
				      //save events details  in SQLite database
//					  JoinedEventsHelper event = new JoinedEventsHelper(context);					  
//					  int deleted = event.deleteEvent(Integer.parseInt(eventid));
//					  									//(int event_id,String event_name,String event_start_date,String event_end_date,String event_photo,String event_description,String event_end_time,String event_start_time,String location, String org_name)
//				      if(deleted == 0)
//				      {
//				    	  Toast.makeText(context, "Event : "+eventname+"  is NOT in the joinevents in SQLite ", Toast.LENGTH_LONG).show();
//				      }
//				      else{
//				    	  Toast.makeText(context, "Event : "+eventname+" has deleted Succefully from SQLite ", Toast.LENGTH_LONG).show();
//				      }
//				     
				    return true;  
				}
				
				
				else 
				{
					Toast.makeText(context, "you didn't join this event to make unjoin!", Toast.LENGTH_LONG).show();
					return false;
				}
			
				
			//}
	
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	//////////////////////////////////////////save//////////////////////////////////////////////////
	public  void save(Context context, String event_id)
	{
		 eventid=event_id;
		 JoinEvents.jointype = "save";
		 userid = sharedprefs.getDefaults("ID",context);
		//Toast.makeText(context, userid+" Event ID: " + event_id,Toast.LENGTH_LONG).show();	
		
		  url_save = "http://7girls.byethost7.com/public/userevent/add";
		  checknetwork = new NetworkConnection(context);
		  boolean connection = checknetwork.isNetworkOnline(); //check Internet connection.
			if(connection == true)
			{
				new SendEventSaveInfo().execute();
			}
			else
			{
				Toast.makeText(context,"please check your internet connection", Toast.LENGTH_LONG).show();
			}
			
	}
	public class SendEventSaveInfo extends AsyncTask<String, String, JSONObject>{
		public   ProgressDialog pDialog;
		protected void onPreExecute() {
//			// TODO Auto-generated method stub
			super.onPreExecute();
			 pDialog = new ProgressDialog(context);
	            pDialog.setMessage("please wait . . . ");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();		
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean serverconnect = checknetwork.isServerAvaliable(url_save); //check server is available or not.
			if(serverconnect==true) //check Server is Available....
			{
				JSONObject json = null;
				List<NameValuePair> param = new ArrayList<NameValuePair>(); //preparing a list of key/value for mail/password
				param.add(new BasicNameValuePair("user_id",userid));
				param.add(new BasicNameValuePair("event_id",eventid));
			//	Log.d("unjointyyype",jointype+" event_id : "+eventid);
				param.add(new BasicNameValuePair("type","save"));
			//	Log.d("url", "url for unjoin" + url_unjoin);
			 json = httpadapter.makeHttpRequest(url_save,"GET", param);  //send the list to the web service.
		//	 Log.d("M.Gabr.jsooon", "result is : "+json.toString());
				
			return json; //return the response from the web service.
			}
			else{
			//	Log.d("M.Gabr.jsooon", "connection error");
				return null ;
				}
			
		}
		
		@Override
		protected void onPostExecute(JSONObject result) 
		{

			// TODO Auto-generated method stub
			
			super.onPostExecute(result);		
			
            if(result != null)
            {
            	pDialog.dismiss();
            	try {
            			//Log.d("M.Gabr.jsooon", result.toString());
            			parseSave(result);
            			
            			
					} 
            	catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
				
			else           
            	Toast.makeText(context, "No data unjoin or No server", Toast.LENGTH_LONG).show(); 
		}
		
		
		protected void parseSave(JSONObject result) throws JSONException
		{
			
			/*
			 *     {"contents":[{ 	"error":0	}]			
			 *     }

			 */
			JSONArray jArray = result.getJSONArray("contents");

				JSONObject jObj = jArray.getJSONObject(0);
				int errormap = Integer.parseInt(jObj.getString("error"));
				if(errormap==0)  //if successed save the event that user joined in the service
				{
						 
					Toast.makeText(context, "Event has saved from webservice Successfully", Toast.LENGTH_LONG).show();
					
				      //save events details  in SQLite database
//					  JoinedEventsHelper event = new JoinedEventsHelper(context);					  
//					  int deleted = event.deleteEvent(Integer.parseInt(eventid));
//					  									//(int event_id,String event_name,String event_start_date,String event_end_date,String event_photo,String event_description,String event_end_time,String event_start_time,String location, String org_name)
//				      if(deleted == 0)
//				      {
//				    	  Toast.makeText(context, "Event : "+eventname+"  is NOT in the joinevents in SQLite ", Toast.LENGTH_LONG).show();
//				      }
//				      else{
//				    	  Toast.makeText(context, "Event : "+eventname+" has deleted Succefully from SQLite ", Toast.LENGTH_LONG).show();
//				      }
//				     
				}
				
				
				else if(errormap == 1)
				{
					Toast.makeText(context, "You have save this event before", Toast.LENGTH_LONG).show();
				}
				else
				{
					
					Toast.makeText(context, "No Data to insert it ! you send empty data to server", Toast.LENGTH_LONG).show();
				}

		}
		
		
	}
	
	
	
	
	
	
	
	
	//////////////////////////////////////// UnSave Event ////////////////////////////////////////////
	public  void unsave(Context context, String event_id)
	{
		 eventid=event_id;
		 JoinEvents.jointype = "save";
		 userid = sharedprefs.getDefaults("ID",context);
		//Toast.makeText(context, userid+" Event ID: " + event_id,Toast.LENGTH_LONG).show();	
		
		  url_unsave = "http://7girls.byethost7.com/public/userevent/delete";
		 checknetwork = new NetworkConnection(context);
		  boolean connection = checknetwork.isNetworkOnline(); //check Internet connection.
			if(connection == true)
			{
				new UnSaveEventInfo().execute();
			}
			else
			{
				Toast.makeText(context,"please check your internet connection", Toast.LENGTH_LONG).show();
			}
			
	}
	
	
	public class UnSaveEventInfo extends AsyncTask<String, String, JSONObject>{
		public   ProgressDialog pDialog;
		protected void onPreExecute() {
//			// TODO Auto-generated method stub
			super.onPreExecute();
			 pDialog = new ProgressDialog(context);
	            pDialog.setMessage("please wait . . . ");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();		
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean serverconnect = checknetwork.isServerAvaliable(url_unsave); //check server is available or not.
			if(serverconnect==true) //check Server is Available....
			{
				JSONObject json = null;
				List<NameValuePair> param = new ArrayList<NameValuePair>(); //preparing a list of key/value for mail/password
				param.add(new BasicNameValuePair("user_id",userid));
				param.add(new BasicNameValuePair("event_id",eventid));
			//	Log.d("unjointyyype",jointype+" event_id : "+eventid);
				param.add(new BasicNameValuePair("type",jointype));
			//	Log.d("url", "url for unjoin" + url_unjoin);
			 json = httpadapter.makeHttpRequest(url_unsave,"GET", param);  //send the list to the web service.
		// Log.d("M.Gabr.jsooon", "result is : "+json.toString());
				
			return json; //return the response from the web service.
			}
			else{
				//Log.d("M.Gabr.jsooon", "connection error");
				return null ;
				}
			
		}
		
		@Override
		protected void onPostExecute(JSONObject result) 
		{

			// TODO Auto-generated method stub
			
			super.onPostExecute(result);		
			
            if(result != null)
            {
            	pDialog.dismiss();
            	try {
            			Log.d("M.Gabr.jsooon", result.toString());
            			parseUnsave(result);
            			
            			
					} 
            	catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	//Toast.makeText(context, "joined successfully thank you ", Toast.LENGTH_LONG).show(); 
            	
            }
				
			else           
            	Toast.makeText(context, "No data unjoin or No server", Toast.LENGTH_LONG).show(); 
		}
		
		
		protected void parseUnsave(JSONObject result) throws JSONException
		{
			
			/*
			 *     {"contents":[{ 	"error":0	}]			
			 *     }

			 */
			JSONArray jArray = result.getJSONArray("contents");
			String myID = null ;			

				JSONObject jObj = jArray.getJSONObject(0);
				int errormap = Integer.parseInt(jObj.getString("error"));
				if(errormap==0)  //if successed insert the event that user joined in the service
				{
						 
					Toast.makeText(context, "Event has unsaved from webservice Successfully", Toast.LENGTH_LONG).show();
					
				      //save events details  in SQLite database
//					  JoinedEventsHelper event = new JoinedEventsHelper(context);					  
//					  int deleted = event.deleteEvent(Integer.parseInt(eventid));
//					  									//(int event_id,String event_name,String event_start_date,String event_end_date,String event_photo,String event_description,String event_end_time,String event_start_time,String location, String org_name)
//				      if(deleted == 0)
//				      {
//				    	  Toast.makeText(context, "Event : "+eventname+"  is NOT in the joinevents in SQLite ", Toast.LENGTH_LONG).show();
//				      }
//				      else{
//				    	  Toast.makeText(context, "Event : "+eventname+" has deleted Succefully from SQLite ", Toast.LENGTH_LONG).show();
//				      }
//				     
				}
				
				
				else 
				{
					Toast.makeText(context, "you didn't save this event to make unsave!", Toast.LENGTH_LONG).show();
				}
			
					
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
