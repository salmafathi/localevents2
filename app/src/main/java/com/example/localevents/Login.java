package com.example.localevents;


import generics.HTTPAdapter;
import generics.NetworkConnection;
import generics.Validation;
import generics.sharedprefs;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

import models.CategoriesHelper;
import models.DBHelper;
import models.InterestsHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	TextView error;
	EditText email;
	EditText password;
	String userPasword;
	String userEmail;
	Button presslogin;
	Validation validation=new Validation();
	NetworkConnection checknetwork;
	String url_insert ;
	public static final String MyPREFERENCES = "MyPrefs" ;  //shared preference file named with MYPrefs
	SharedPreferences sharedpreferences; //shared preference object
	
	HTTPAdapter httpadapter = new HTTPAdapter(); 
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 url_insert = "http://7girls.byethost7.com/public/user/login";
		checknetwork = new NetworkConnection(this.getApplicationContext());
		
		setContentView(R.layout.login);
		setTitle("Login");
        getActionBar().setIcon(R.drawable.logo);
		
		error = (TextView) findViewById(R.id.error);
		email=(EditText) findViewById(R.id.email);
		password=(EditText) findViewById(R.id.password);
		presslogin=(Button) findViewById(R.id.login);
		
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHICBI.TTF");
		presslogin.setTypeface(font);
		//click on Login button
		presslogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
		
				// TODO Auto-generated method stub
				
		        boolean connection = checknetwork.isNetworkOnline(); //check Internet connection.
				if(connection == true)
				{
					userPasword=password.getText().toString();
					userEmail=email.getText().toString().trim();
	
					if(!(validation.isValidMail(userEmail))) //check mail syntax validation
						{
						email.setError("Not Valid Email !");
						}
					else{
						userPasword=validation.passwordMD5Hash(userPasword).toString(); //hashing password
						Toast.makeText(getApplicationContext(), userEmail+":"+userPasword, Toast.LENGTH_LONG).show();
						new LoginOp().execute();	//Start Asynchronous class
		    		    }
				}
				else
				{
					Toast.makeText(Login.this,"please check your internet connection", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		
	}
	


	public class LoginOp extends AsyncTask<String, String, JSONObject>{
	public   ProgressDialog pDialog;
//start progress dialog 		
		protected void onPreExecute() {
//			// TODO Auto-generated method stub
			super.onPreExecute();
			 pDialog = new ProgressDialog(Login.this);
	            pDialog.setMessage("please wait . . . ");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();		
		}
		
		@Override
		protected JSONObject doInBackground(String... args0) {
			// TODO Auto-generated method stub
		
			boolean serverconnect = checknetwork.isServerAvaliable(url_insert); //check server is available or not.
			if(serverconnect==true) //check Server is Available....
			{
				JSONObject json = null;
				List<NameValuePair> params = new ArrayList<NameValuePair>(); //preparing a list of key/value for mail/password
				params.add(new BasicNameValuePair("email",userEmail));
				params.add(new BasicNameValuePair("password",userPasword));
				 json = httpadapter.makeHttpRequest(url_insert,"POST", params);  //send the list to the web service.
				
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
            	try{
            		pDialog.dismiss();
            	//	Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            		parseData(result);   // function of parse the result json object         		
            		}
            catch (JSONException e){
            	e.printStackTrace();
            	}
				
			else           
            	Toast.makeText(getApplicationContext(), "No data or No server", Toast.LENGTH_LONG).show(); 
		}
		
		
		//function of parsing the response  
		protected void parseData(JSONObject result) throws JSONException
		{
			
			/*
			 *     {"contents":[{ 	"error":0,
			 *     					"username" :"salma",
			 *     					"userid" : "5",
			 *     					"useremail" : "ss@gg.com"
			 *     					}]			
			 *     }
			 * 
			 * 
			 * {"contents:[{"error:1"}]"}
			 */
//			Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
			JSONArray jArray = result.getJSONArray("contents");
			String myID = null ;
			
			
			for(int i = 0 ; i < jArray.length();i++)
			{
				JSONObject jObj = jArray.getJSONObject(i);
				int errormap = Integer.parseInt(jObj.getString("error"));
				//Toast.makeText(getApplicationContext(), jObj.getString("error"), Toast.LENGTH_LONG).show();
				
				if(errormap==0)  //check error number
				{
//					Toast.makeText(getApplicationContext(),"error:"+errormap, Toast.LENGTH_LONG).show();
					
					String myname = jObj.getString("user_name");  
//					Toast.makeText(getApplicationContext(),"name:"+myname, Toast.LENGTH_LONG).show();
					 myID = jObj.getString("user_id");
//					 Toast.makeText(getApplicationContext(),"ID:"+myID, Toast.LENGTH_LONG).show();
					String myemail = jObj.getString("user_email");
//					Toast.makeText(getApplicationContext(),"mail:"+myemail, Toast.LENGTH_LONG).show();
					String myimage =null;
					if(jObj.has("user_image"))
					{
						myimage = "http://7girls.byethost7.com/public/"+jObj.getString("user_image");
					}
//					Toast.makeText(getApplicationContext(),"image:"+myimage,Toast.LENGTH_SHORT).show();
				
					String mycity = null;
					if(jObj.has("city_name")){
						mycity = jObj.getString("city_name");
						
					}
//					Toast.makeText(getApplicationContext(),"city:"+mycity, Toast.LENGTH_SHORT).show();
//					Toast.makeText(getApplicationContext(), "interests : "+interests, Toast.LENGTH_LONG).show();
		
					//save user ID in a shared preference file.
					
//				      sharedpreferences.getString("ID", "not found");
//				      Toast.makeText(getApplicationContext(), "shared : "+sharedpreferences.getString("ID", "not found"), Toast.LENGTH_LONG).show();
				      JSONArray interests = null;
				      if(jObj.has("interests")){
				    	  interests = jObj.getJSONArray("interests");
//				    	  Toast.makeText(getApplicationContext(),"interestsssssssssssssssssssss",Toast.LENGTH_LONG).show();
				    	  
				      }
				      
				      //save other data in SQLite database
				      
				      DBHelper DB = new DBHelper(getApplicationContext());
				      InterestsHelper.cn = getApplicationContext();
				      CategoriesHelper.cn = getApplicationContext();
				      
				      InterestsHelper interestsHelper = new InterestsHelper();
				      CategoriesHelper categoriesHelper = new CategoriesHelper();
				      
				      if(DB.insertUser(Integer.parseInt(myID),myname, myemail, myimage,mycity)){
				    	  if(interests!=null){
						      for(int j = 0 ; j < interests.length() ; j++){
						    	  if(interestsHelper.insertInterests(Integer.parseInt(myID), interests.getJSONObject(j).getInt("cat_id")) != 0)
						    		  categoriesHelper.insertCat(interests.getJSONObject(j).getInt("cat_id"), interests.getJSONObject(j).getString("cat_name"));
//						    		  Toast.makeText(getApplicationContext(), "Interests added successfully", Toast.LENGTH_LONG).show();  
						      }
						   }
				    	  sharedprefs.setDefaults("ID", myID,Login.this); 
//				    	  sharedpreferences=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
//						  Editor editor = sharedpreferences.edit();
//					      editor.putString("ID", myID);
//					      editor.commit();
//				    	  Toast.makeText(getApplicationContext(), "data updated successfully", Toast.LENGTH_LONG).show();

					      
					  }else{
						  Toast.makeText(getApplicationContext(),"error !", Toast.LENGTH_LONG).show();
						  
						  
					  }
				      
				      
				     // Toast.makeText(getApplicationContext(), "insert is "+DB.insertUser(myname, myemail, myID), Toast.LENGTH_LONG).show();
				      
//				      Cursor cr = DB.getData(Integer.parseInt(myID));
//				      String user_name = cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_NAME));
//				      Toast.makeText(getApplicationContext(), "name = "+user_name, Toast.LENGTH_LONG).show();
				      
				      Intent intent = new Intent(getApplicationContext(),MainActivityUser.class);
				      //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				      startActivity(intent);
				      finishAffinity();
				      
				      
				}
				
				
				else if(errormap == 1)
				{
					Login.this.error.setText("Wrong email or password !");
					Login.this.error.setTextColor(Color.RED);
//				Toast.makeText(getApplicationContext(),"You are Not a Member, Please Sign up!", Toast.LENGTH_LONG).show();
//				Intent intent = new Intent(getBaseContext(),HomeUser.class);
//				intent.putExtra("ID", myID);
				
				//startActivity(new Intent(getApplicationContext(),Home.class));
				}else if(errormap == 2){
					Login.this.error.setText("Email and password are required !");
					Login.this.error.setTextColor(Color.RED);
					
				}else if(errormap == 3){
					Login.this.error.setText("Email form is not valid !");
					Login.this.error.setTextColor(Color.RED);
					
				}else{
					Login.this.error.setText("Something went wrong, try again later !");
					Login.this.error.setTextColor(Color.RED);
					
				}
				
			}
	
		}
	
	}
	
	}





