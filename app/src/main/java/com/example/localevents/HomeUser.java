package com.example.localevents;

import generics.CircleTransform;
import generics.HTTPAdapter;
import generics.NetworkConnection;
import generics.sharedprefs;

import java.util.ArrayList;


import parsers.CategoryParser;
import parsers.EventParser;

import com.example.localevents.R;
import com.squareup.picasso.Picasso;

import models.CategoriesHelper;
import models.CategoryModel;
import models.DBHelper;
import models.EventModel;
import models.InterestsHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeUser extends Fragment{
	Button joinedsavedbtn;
	ImageView edit ;
	ImageView signout ;
	TextView username;
	TextView useremail;
	TextView usercity;
	TextView userinterests;
	Button cats;
	SharedPreferences sharedpreferences;
	DBHelper db;
	InterestsHelper interestHelper;
	CategoriesHelper catHelper;
	String arg="Not Found" ;
	ImageView userimage;
	RoundImage roundedImage;
	Bitmap bm;
	NetworkConnection checknetwork;
	public String url_insert ;
	HTTPAdapter httpadapter;
	CategoryParser catparser;
	CategoryModel cat_model ;
	EventModel evt_model;
	public String url_events="http://7girls.byethost7.com/public/event/viewallevent" ;	
	EventParser eventparser;
	
	//String myID;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.home_user, container,false);
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		httpadapter = new HTTPAdapter();
		catparser= new CategoryParser();
		cat_model = new CategoryModel() ;
		evt_model= new EventModel();
		eventparser= new EventParser();
		db=new DBHelper(getActivity());
		
		
		InterestsHelper.cn = getActivity();
		CategoriesHelper.cn = getActivity();
		
		interestHelper = new InterestsHelper();
		catHelper = new CategoriesHelper();
		
		edit = (ImageView) getView().findViewById(R.id.setting);
		signout = (ImageView) getView().findViewById(R.id.logout);
		username = (TextView) getView().findViewById(R.id.username);
		useremail = (TextView) getView().findViewById(R.id.email);
		usercity = (TextView) getView().findViewById(R.id.city);
		userimage= (ImageView) getView().findViewById(R.id.userimage);
		userinterests = (TextView) getView().findViewById(R.id.interests);
	//	cats = (Button) getView().findViewById(R.id.cats);
		
		bm= BitmapFactory.decodeResource(getResources(),R.drawable.user);
		roundedImage = new RoundImage(bm);
		userimage.setImageDrawable(roundedImage);
		
//		sharedpreferences=this.getActivity().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
//		String MyID = sharedpreferences.getString("ID", arg);
		String MyID = sharedprefs.getDefaults("ID", getActivity());
		
		edit.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				 switch (event.getAction()) {
			        case MotionEvent.ACTION_DOWN: {
			            edit.setImageResource(R.drawable.editicon);
			            break;
			        }
			        case MotionEvent.ACTION_UP: {
			            edit.setImageResource(R.drawable.process);
			            break;
			        }
			        }
				return false;
			}
		});
		
		 edit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getActivity(),com.example.localevents.EditProfile.class);
				      getActivity().startActivity(i);
					
				}});
		// joinedsavedbtn
			joinedsavedbtn= (Button) getView().findViewById(R.id.joinsavebtn);
			/************************joinedsavedbtn to goto tabpager***************************/
		 joinedsavedbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
			
					
				      Intent i = new Intent(getActivity(),com.example.localevents.JoinedSavedActivity.class);
				      getActivity().startActivity(i);
//				      getActivity().finishAffinity();
					
				}
			});
		 
		 
//		 cats.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View arg0) {
//					url_insert = "http://7girls.byethost7.com/public/category/list";
//					checknetwork = new NetworkConnection(getActivity());					
//				     boolean connection = checknetwork.isNetworkOnline();
//				     if(connection==true)
//				     { 
//				    		new GetCategories().execute(); 
//	
//				     }
//						
//				     else
//						{
//							Toast.makeText(getActivity(),"please check your internet connection", Toast.LENGTH_LONG).show();
//						}
//					
//				}});
		 
		 signout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
			
					// TODO Auto-generated method stub
				
//				      Editor editor = sharedpreferences.edit();
//				      editor.clear();
//				      editor.commit();
					sharedprefs.deleteDefaults(getActivity());
				      interestHelper.deleteInterests();
				      db.deleteData();
				      catHelper.deleteCats();
				      Intent i = new Intent(getActivity(),com.example.localevents.Home.class);
				      getActivity().startActivity(i);
				      getActivity().finishAffinity();
					
				}
			});
		 
		 
		 	Cursor cr =  db.getData(Integer.parseInt(MyID));
		    cr.moveToFirst();
		    while(cr.isAfterLast() == false)
		    {
			    String user_name = cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_NAME));
			    String user_email= cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_EMAIL));
			    String user_image = cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_IMAGE));
			    
			    String user_city = cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_CITY));
			   // Toast.makeText(getActivity(),user_image, Toast.LENGTH_LONG).show();
			    
			    if(user_image!="" && user_image!=null)
			    {
			    	Picasso.with(getActivity()).load(user_image).resize(120, 120).noFade().transform(new CircleTransform()).into(userimage);
//			    	bm= BitmapFactory.decodeFile(user_image);
//			        roundedImage = new RoundImage(bm);
//			        userimage.setImageDrawable(roundedImage);
			    }
			    else
			    {
			    	bm= BitmapFactory.decodeResource(getResources(),R.drawable.user);
			        roundedImage = new RoundImage(bm);
			        userimage.setImageDrawable(roundedImage);
			    }
		    
				username.setText(user_name);
				useremail.setText(user_email);
				if(user_city != "" && user_city != null){
					usercity.setText(user_city);
				}else{
					usercity.setText("no city to show");
					
				}
				
				cr.moveToNext();
		    }
		    db.close();
		    // for interests
		    String interests = "";
		    Cursor cr_interests =  catHelper.getCats();
//		    Toast.makeText(getActivity(),cr_interests.toString(), Toast.LENGTH_LONG).show();
		    if(cr_interests!=null && cr_interests.getCount()>0)
		    {	
		    cr_interests.moveToFirst();
		    while(cr_interests.isAfterLast() == false)
		    {
		    	
			    String cat_name = cr_interests.getString(cr_interests.getColumnIndex(CategoriesHelper.CAT_COLUMN_NAME));
//			    Toast.makeText(getActivity(),cat_name, Toast.LENGTH_LONG).show();
			    if(cr_interests.isLast()){
			    	interests += cat_name+ ".";
			    	
			    }else{
			    	interests += cat_name+", ";
			    	
			    }
			    
			  
				
				cr_interests.moveToNext();
		    }
		    userinterests.setText(interests);
		    }else{
		    	userinterests.setText("no interests to show");
		    }
		    catHelper.dbhelper.close();
		    
		    
		    
		    
		    
	}
	
	
		 
//	class GetCategories extends AsyncTask<String, String, JSONObject>
//	{
//		public   ProgressDialog pDialog;
//		//start progress dialog 		
//				protected void onPreExecute() {
////					// TODO Auto-generated method stub
//					super.onPreExecute();
//					
//						pDialog = new ProgressDialog(getActivity());
//			            pDialog.setMessage("please wait . . . ");
//			            pDialog.setIndeterminate(false);
//			            pDialog.setCancelable(true);
//			            pDialog.show();		
//				}
//				
//		@Override
//		protected JSONObject doInBackground(String... arg0)
//		{
//			 JSONObject json = null;
//			boolean serverconnect = checknetwork.isServerAvaliable(url_insert);
//			if(serverconnect==true)
//			{
//			 json = httpadapter.makeHttpRequestWithNoParams(url_insert);
//			 //send the list to the web service.
//			 
//			}
//			return json;
//		}
//
//		@Override
//		protected void onPostExecute(JSONObject result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//
//            if(result != null)
//            	try{
//            		ArrayList<CategoryModel> res =catparser.getResponse(result);   // function of parse the result json object         		
//            		
//            		 Intent in = new Intent(getActivity(),Categories.class);
//     					   in.putExtra("catdata",res);
//     					    startActivity(in);
//            		 
//            		 
////            		Toast.makeText(getApplicationContext(),"the data is : "+g.toString(), Toast.LENGTH_LONG).show();
//            		
//            		pDialog.dismiss();
//            	//	Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
//            	//	Toast.makeText(getApplicationContext(),"the result is : "+result, Toast.LENGTH_LONG).show();
//            	}
//            catch (JSONException e){
//            	e.printStackTrace();
//            	}
//				
//			else           
//            	Toast.makeText(getActivity(), "No data or No server", Toast.LENGTH_LONG).show(); 
//			
//		}
//		
//	
//
//}
	
	
}
