package com.example.localevents;
import generics.HTTPAdapter;
import generics.NetworkConnection;
import generics.Validation;
import generics.sharedprefs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.DBHelper;
import models.UserModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import parsers.*;

public class Register extends Activity {
	public static final String MyPREFERENCES = "MyPrefs";
	
	Button signup;
	EditText name;
	EditText password;
	EditText email;
	TextView error;
	
	String userName;
	String userPassword;
	String userEmail;
	String url ;
	
	UserModel userModel = new UserModel();
	UserParser userParser = new UserParser();
	Validation validation=new Validation();
	HTTPAdapter httpAdapter = new HTTPAdapter();
	
	SharedPreferences sharedpreferences;
	
	//HTTPAsyncHelper httpAsynchelper ;
	NetworkConnection networkConnection ;

	boolean userInputsNotEmpty,userInputsValid;
	
	boolean emptyEmail,emptyUserName,emptyPassword;
	boolean validEmail,validUserName,validPassword;
	
	
	
	List<NameValuePair> userData;
	public Register(){
		
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = "http://7girls.byethost7.com/public/user/add";
        networkConnection = new NetworkConnection(this.getApplicationContext());
       
        setContentView(R.layout.activity_register);
        setTitle("Register");
        getActionBar().setIcon(R.drawable.logo);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        signup=(Button) findViewById(R.id.button1);
        name=(EditText) findViewById(R.id.editText1);
		password=(EditText) findViewById(R.id.editText2);
		email=(EditText) findViewById(R.id.editText3);
		error = (TextView) findViewById(R.id.error);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHICBI.TTF");
		signup.setTypeface(font);
		
		signup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userName=name.getText().toString().trim();
				userPassword=password.getText().toString();
				userEmail=email.getText().toString().trim();
				
				
				showMessageIfUserInputsEmpty(userName, userPassword, userEmail);
				showMessageIfUserInputsNotValid(userName, userPassword, userEmail);
				
				userInputsNotEmpty=UserInputsNotEmpty(emptyEmail,emptyPassword,
						emptyUserName);
				userInputsValid=UserInputsValid(validEmail,validUserName,validPassword);
				if(userInputsNotEmpty&&userInputsValid){
					// here goes my registration process
					userPassword = validation.passwordMD5Hash(userPassword).toString();
					if(networkConnection.isNetworkOnline()){
						
							userData = new ArrayList<NameValuePair>();
							userData.add(new BasicNameValuePair("username", userName));
							userData.add(new BasicNameValuePair("password", userPassword));
							userData.add(new BasicNameValuePair("email", userEmail));
							//httpAsynchelper.list = userData;
							new HTTPAsyncHelper().execute();
							
						
						
					}else{
						Toast.makeText(getApplicationContext(),"Check Your network connection !", Toast.LENGTH_LONG).show();
						
					}
					
				}
				
			}
			
		});
        
    }
    
    
    
    public void showMessageIfUserInputsEmpty(String userName,String userPasword,
    		String userEmail)
    {
    	emptyEmail=validation.isEmptyMail(userEmail);
    	emptyUserName=validation.isEmptyUserName(userName);
    	emptyPassword=validation.isEmptyPassword(userPasword);
    	
    	if(emptyEmail)
    	{
    		email.setError(" Email is Empty !");
    		
    	}
    	if(emptyUserName)
    	{
    		name.setError(" username is Empty !");
    		
    	}
    	if(emptyPassword)
    	{
    		password.setError(" password is Empty !");
    		
    	}	
    	
    }
    
    public boolean UserInputsNotEmpty(Boolean emptyEmail,Boolean emptyPassword,
    		Boolean emptyUserName){
    	if((!emptyEmail && !emptyPassword)&& !emptyUserName)
    		return true;
    	else 
			return false;
    }
   
    public void showMessageIfUserInputsNotValid(String userName,String userPasword,
    		String userEmail)
    {
    	
    	boolean validationUserInputs;
		validEmail=validation.isValidMail(userEmail);
		validUserName=validation.isValidUserName(userName);
		validPassword=validation.isValidPassword(userPasword);
		
		if(!validEmail)
		{
			email.setError("! Not Valid Email");
		}
		if(!validUserName)
		{
			name.setError("Please enter username < 50 character ");
		}
			
		if(!validPassword)
		{
			password.setError("Please Enter Password > 8 character");
		}
    }
   
    public boolean UserInputsValid(Boolean validEmail,Boolean validUserName,
    		Boolean validPassword){
    	boolean validationUserInputs=(validEmail && validUserName)&& validPassword;
    	if(validationUserInputs)
    		return true;
    	else 
			return false;
    }
   
		
    public void ifUserInputsIsCorrect(String userName,String userPasword,
    		String userEmail)
    {
    	userPasword=validation.passwordMD5Hash(userPasword).toString();
    	Intent intent=new Intent(this,HomeUser.class);
		intent.putExtra("username", userName);
		intent.putExtra("password", userPasword);
		intent.putExtra("email", userEmail);
		startActivity(intent);
    }
    
    public class HTTPAsyncHelper extends AsyncTask<JSONObject, Void, JSONObject>{
   	// Context cn;
   	 //String url;
   	 //public List<NameValuePair> list = new ArrayList<NameValuePair>();
    	public   ProgressDialog pDialog;
    	//start progress dialog 		
    			protected void onPreExecute() {
//    				// TODO Auto-generated method stub
    				super.onPreExecute();
    				 pDialog = new ProgressDialog(Register.this);
    		         pDialog.setMessage("please wait . . . ");
    		         pDialog.setIndeterminate(false);
    		         pDialog.setCancelable(true);
    		         pDialog.show();		
    			}
   	
   	@Override
   	protected JSONObject doInBackground(JSONObject... params) {
   		// TODO Auto-generated method stub
   		
   		//Check Server is available or not !
   	//	NetworkConnection checknetwork = new NetworkConnection(Register.this);
 //  		boolean serverconnect = checknetwork.isServerAvaliable(url);
  		if(networkConnection.isServerAvaliable(url)==true) //Server is Available....
 		{
   			JSONObject json = null;
   			HTTPAdapter jp = new HTTPAdapter();
   			json = jp.makeHttpRequestPost(url,userData);
   			return json;
   		}
   		
   		else //Server Not Available
   		{
   			return null ;
   		}

  // 		String jString = json.toString();
   		
   	}
   	
   	protected void onPostExecute(JSONObject result){
   		
   		super.onPostExecute(result);
   		
   		if(result!=null) //When Server is Available
   		{
   		//parseData(result);
   			try {
   				pDialog.dismiss();
				userParser.getResponse(result);
				userModel = userParser.getUserModel();
				int error = userModel.getError();
				//Toast.makeText(Register.this, error, Toast.LENGTH_LONG).show();
				//Log.d("errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr ", ""+error);
				if(error == 0){
					
					String myID = userModel.getUser_id();
					sharedprefs.setDefaults("ID", myID,Register.this); 
//					sharedpreferences=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
//					  Editor editor = sharedpreferences.edit();
//				      editor.putString("ID", myID);
//				      editor.commit();
				      
				      //save other data in SQLite database
				      DBHelper DB = new DBHelper(getApplicationContext());
				      DB.insertUser(Integer.parseInt(myID),userName, userEmail, null,null);
				     // Toast.makeText(Register.this, "number of rows = "+DB.numberOfRows(), Toast.LENGTH_LONG).show();
				      
					Intent intent=new Intent(getApplicationContext(),MainActivityUser.class);
					startActivity(intent);
					
					finishAffinity();
					
				}else if(error == 1){
					Register.this.error.setText("Signup not complete !");
					Register.this.error.setTextColor(Color.RED);
					
					
				}else if(error == 2){
					Register.this.error.setText("This name or email already exists !");
					Register.this.error.setTextColor(Color.RED);
					
				}else if(error == 3){
					Register.this.error.setText("Email form is not valid !");
					Register.this.error.setTextColor(Color.RED);
					
				}else if(error == 4){
					Register.this.error.setText("Empty fields are not allowed !");
					Register.this.error.setTextColor(Color.RED);
					
				}else if(error == 5){
					Register.this.error.setText("Username only contains characters !");
					Register.this.error.setTextColor(Color.RED);
					
				}else{
					Register.this.error.setText("Something went wrong, try again later  !");
					Register.this.error.setTextColor(Color.RED);
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   		//Toast.makeText(Register.this, result.toString(), Toast.LENGTH_LONG).show();
   		}else{ // When Server is Not Available
   			Toast.makeText(Register.this, "Server Not Found  "+result, Toast.LENGTH_LONG).show();
   		}
   		
   	}

   }

}
    
    

