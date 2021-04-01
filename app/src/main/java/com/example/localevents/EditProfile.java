package com.example.localevents;

import generics.CircleTransform;
import generics.HTTPAdapter;
import generics.NetworkConnection;
import generics.sharedprefs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import models.CategoriesHelper;
import models.CategoryModel;
import models.CityModel;
import models.DBHelper;
import models.InterestsHelper;
import models.UserModel;
import generics.Validation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.localevents.Register.HTTPAsyncHelper;
import com.example.localevents.CustomMultiPartEntity;
import com.example.localevents.UploadProgressListener;
import com.squareup.picasso.Picasso;

import parsers.CategoryParser;
import parsers.CityParser;
import parsers.UserParser;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
//import android.support.v4.widget.SearchViewCompatIcs.MySearchView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class EditProfile extends Activity {
	EditText username;
	EditText useremail;
	NetworkConnection checknetwork;
	public String url ;
	HTTPAdapter httpadapter ;
	CategoryParser catparser;
	CityParser cityparser;
	LinearLayout ll ;
	Spinner cityspinner;
	String[] arraySpinner;
	ImageView userimage;
	RoundImage roundedImage;
	Bitmap bm;
	SharedPreferences sharedpreferences;
	DBHelper db;
	String arg="Not Found" ;
	
	private static int RESULT_LOAD_IMG = 1;
    Button load;
    ImageView upload;
    Bitmap photo;
    List<NameValuePair> data;
    NetworkConnection networkConnection ;
    String MyID;
    String editURL;
    ArrayList<CheckBox> cbCatArrayList;
    String name;
    String email;
    String country;
    boolean userInputsNotEmpty;
    boolean userInputsValid;
    boolean emptyEmail,emptyUserName,emptyPassword;
	boolean validEmail,validUserName,validPassword;
	Validation validation;
	CheckBox box ;
	String ids = "";
	ArrayList<Integer> idArray;
	TextView error;
	String user_city;
	
	ProgressDialog progressDialog;
	final static String UPLOAD_SERVER_URI = "http://7girls.byethost7.com/public/user/testimage";
	String imagePath;
	String imageName;
	long imageSize = 0;
	final static int REQUEST_CODE = 1;
	InterestsHelper interestHelper;
	CategoriesHelper catHelper;
	
	Context cn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		setTitle("Edit Profile");
		
        getActionBar().setIcon(R.drawable.logo);
        
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#66CDAA"));
        getActionBar().setBackgroundDrawable(colorDrawable);
        
        userimage= (ImageView) findViewById(R.id.userimage);
		bm= BitmapFactory.decodeResource(getResources(),R.drawable.user);
        roundedImage = new RoundImage(bm);
        userimage.setImageDrawable(roundedImage);
        validation=new Validation();
        box = new CheckBox(this);
        
        upload = (ImageView) findViewById(R.id.upload);
        upload.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				 switch (event.getAction()) {
			        case MotionEvent.ACTION_DOWN: {
			            upload.setImageResource(R.drawable.upload_press);
			            break;
			        }
			        case MotionEvent.ACTION_UP: {
			            upload.setImageResource(R.drawable.upload);
			            break;
			        }
			        }
				return false;
			}
		});
        username = (EditText) findViewById(R.id.username);
        useremail = (EditText) findViewById(R.id.email);
        db=new DBHelper(getApplicationContext());
        networkConnection = new NetworkConnection(this.getApplicationContext());
        data = new ArrayList<NameValuePair>();
        idArray = new ArrayList<Integer>();
        error = (TextView) findViewById(R.id.error);
        
        InterestsHelper.cn = getApplicationContext();
		 interestHelper = new InterestsHelper();
		 
		 CategoriesHelper.cn = getApplicationContext();
		  catHelper=new CategoriesHelper();
       
        
        
        editURL = "http://7girls.byethost7.com/public/user/update";
        
        cbCatArrayList = new ArrayList<CheckBox>();
        cn = getApplicationContext();
       
        
    
	    MyID=sharedprefs.getDefaults("ID", getApplicationContext());
	    if(MyID != arg){
	    	
	  	  // Toast.makeText(this,"id is : "+MyID , Toast.LENGTH_LONG).show();
	    	Cursor cr = db.getData(Integer.parseInt(MyID));
	  	  //  Toast.makeText(HomeUser.this,"Data is : "+db.getAllContacts() , Toast.LENGTH_LONG).show();
	  	    cr.moveToFirst();
	  	    while(cr.isAfterLast() == false){
	  	    String user_name = cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_NAME));
	  	    String user_email= cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_EMAIL));
	  	    user_city = cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_CITY));
	  	    String user_image = cr.getString(cr.getColumnIndex(DBHelper.USER_COLUMN_IMAGE));
	  	    
	  		username.setText(user_name);
	  		useremail.setText(user_email);
	  		if(user_image!="" && user_image!=null)
		    {
	  			Picasso.with(cn).load(user_image).resize(120, 120).noFade().transform(new CircleTransform()).into(userimage);
		    }
		    else
		    {
		    	bm= BitmapFactory.decodeResource(getResources(),R.drawable.user);
		        roundedImage = new RoundImage(bm);
		        userimage.setImageDrawable(roundedImage);
		    }
	  		
	  		cr.moveToNext();
	  	    }
	    	
	    }
		
        url = "http://7girls.byethost7.com/public/category/list";
		checknetwork = new NetworkConnection(getApplicationContext());
		httpadapter =  new HTTPAdapter();
		catparser= new CategoryParser();
		cityparser = new CityParser(this);
		cityspinner = (Spinner) findViewById(R.id.city);
		userimage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Create intent to Open Image applications like Gallery, Google Photos
		        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
		                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		        // Start the Intent
		        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
		     // UPLOAD IMAGE
				
//				if(imagePath == null){
//					// IF NO IMAGE SELECTED DO NOTHING
//					Toast.makeText(getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
//					return;
//				}else
				
//				progressDialog = createDialog();
//				progressDialog.show();
				
				// EXECUTED ASYNCTASK TO UPLOAD IMAGE
//				new ImageUploader().execute();
		       
				
			}});

		
		ll = (LinearLayout) findViewById(R.id.linearLayout1);
		new GetCategories().execute();
		new GetCities().execute();
		upload.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(imagePath != null){
					
					new ImageUploader().execute();
					}else{
						Toast.makeText(getApplicationContext(),"Click the image to pick one", Toast.LENGTH_LONG).show();
						
					}
				
		       
				
			}});
		
      
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        if(item.getItemId() == R.id.saveUser){
        	name = username.getText().toString().trim(); 
    		email = useremail.getText().toString().trim();
    		country = cityspinner.getSelectedItem().toString().trim();
    		
    		showMessageIfUserInputsEmpty(name, email);
    		showMessageIfUserInputsNotValid(name, email);
    		userInputsNotEmpty=UserInputsNotEmpty(emptyEmail, emptyUserName);
    		userInputsValid=UserInputsValid(validEmail, validUserName);
    		
			
			// EXECUTED ASYNCTASK TO UPLOAD IMAGE
			
    		if(userInputsNotEmpty&&userInputsValid){
				// here goes my registration process
    			if(MyID != arg){
//    				if(photo != null){
    					for(int i = 0 ; i < idArray.size() ; i++){
        	        		ids += idArray.get(i)+",";
//        	        		Log.d("iddddddddddds", ids);
        	        		Toast.makeText(getApplicationContext(), ids, Toast.LENGTH_LONG).show();
        	        		
        	        	}
        	            new EditUserInfo().execute();
//    				}else{
//    					error.setError("Image is required !");
//    					error.setTextColor(Color.RED);
//    					
//    				}
    	        	
    	        	}else{
    	        		Toast.makeText(getApplicationContext(),"No user found", Toast.LENGTH_LONG).show();
    	        		
    	        	}
				
				
			}
    		
        	
        }
 
        return super.onOptionsItemSelected(item);
    }
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
 
                Uri selectedImage = data.getData();
                
                // GET IMAGE PATH
    			imagePath = getPath(selectedImage);
    			// IMAGE NAME
    			imageName = imagePath.substring(imagePath.lastIndexOf("/"));
    			
    			imageSize = this.getFileSize(imagePath);
             
                photo = BitmapFactory.decodeFile(imagePath);
                roundedImage = new RoundImage(photo);
                userimage.setImageDrawable(roundedImage);
                
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong"+e, Toast.LENGTH_LONG)
                    .show();
        }
 
    }
	private String getPath(Uri uri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	private long getFileSize(String imagePath){
		long length = 0;

		try {

			File file = new File(imagePath);
			length = file.length();
			length = length / 1024;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return length;
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.savebutton, menu);
	    return true;
	}
	class GetCities extends AsyncTask<String, String, JSONObject>
	{
		public   ProgressDialog pDialog;
		//start progress dialog 		
				protected void onPreExecute() {
//					// TODO Auto-generated method stub
					super.onPreExecute();
					
					//??? 
					 pDialog = new ProgressDialog(EditProfile.this);
			            pDialog.setMessage("please wait . . . ");
			            pDialog.setIndeterminate(false);
			            pDialog.setCancelable(true);
			            pDialog.show();		
				}
				
		@Override
		protected JSONObject doInBackground(String... arg0)
		{
			 JSONObject json = null;
			boolean serverconnect = checknetwork.isServerAvaliable("http://7girls.byethost7.com/public/city/list");
			if(serverconnect==true)
			{
			 json = httpadapter.makeHttpRequestWithNoParams("http://7girls.byethost7.com/public/city/list");
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
            		cityparser.getResponse(result);
            		ArrayList<CityModel> res = cityparser.cityArrayList;   // function of parse the result json object         		
            		Toast.makeText(getApplicationContext(), "res size "+res.size(), Toast.LENGTH_LONG).show();
            		ArrayList<String> citynames = new ArrayList<String>();
            		//hna ana hamla el check boxes elly ana ma7tethash asln !!!
            		for(int i = 0 ; i < res.size() ; i++){
            			citynames.add(res.get(i).getCity_name());
            			//Toast.makeText(getApplicationContext(), "city name "+res.get(i).getCity_name(), Toast.LENGTH_LONG).show();
            			
            			//res.get(i).getCity_name()
            			
            			//ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfile.this,android.R.layout.simple_spinner_item, arraySpinner);
            			
            			
            		}
            		arraySpinner = citynames.toArray(new String[citynames.size()]);
            		ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfile.this,android.R.layout.simple_spinner_item, arraySpinner);
            		cityspinner.setAdapter(adapter);
            		if (user_city!=null && user_city != "") {
            	        int spinnerPostion = adapter.getPosition(user_city);
            	        cityspinner.setSelection(spinnerPostion);
            	        spinnerPostion = 0;
            	    }
            		 
            		 
//            		Toast.makeText(getApplicationContext(),"the data is : "+g.toString(), Toast.LENGTH_LONG).show();
            		
            		pDialog.dismiss();
            	//	Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            	//	Toast.makeText(getApplicationContext(),"the result is : "+result, Toast.LENGTH_LONG).show();
            	}
            catch (JSONException e){
            	e.printStackTrace();
            	}
				
			else           
            	Toast.makeText(getApplicationContext(), "No data or No server", Toast.LENGTH_LONG).show(); 
			
		}
		
		
		


	

}
	class GetCategories extends AsyncTask<String, String, JSONObject>
	{
		public  ProgressDialog pDialog;
		//start progress dialog 		
				protected void onPreExecute() {
//					// TODO Auto-generated method stub
					super.onPreExecute();
					
					//??? 
					 pDialog = new ProgressDialog(EditProfile.this);
			            pDialog.setMessage("please wait . . . ");
			            pDialog.setIndeterminate(false);
			            pDialog.setCancelable(true);
			            pDialog.show();		
				}
				
		@Override
		protected JSONObject doInBackground(String... arg0)
		{
			
			 JSONObject json = null;
			boolean serverconnect = checknetwork.isServerAvaliable(url);
			if(serverconnect==true)
			{
			 json = httpadapter.makeHttpRequestWithNoParams(url);
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
            		ArrayList<CategoryModel> res =catparser.getResponse(result);   // function of parse the result json object         		
            		//hna ana hamla el check boxes elly ana ma7tethash asln !!!
            		for(int i = 0 ; i < res.size() ; i++){
            			CheckBox cb = new CheckBox(EditProfile.this);
            			cb.setId(res.get(i).getCat_id());
            			cb.setText(res.get(i).getCat_name());
            			cb.setTag(i);
            			cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            				
            				@Override
            				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            					// TODO Auto-generated method stub
            					if(isChecked){
            						idArray.add(buttonView.getId());
            						
            					}else{
            						for(int i = 0 ; i < idArray.size() ; i++){
            							if(idArray.get(i) == buttonView.getId()){
            								idArray.remove(i);
            								
            							}
            							
            						}
            						
            					}
            				}

            				
            			});
            			ArrayList<Integer> catIds = getInterestsArray();
            			for(int j = 0 ; j < catIds.size() ; j++){
            				if(cb.getId() == catIds.get(j)){
            					cb.setChecked(true);
            				}
            				
            				
            			}
            			cbCatArrayList.add(cb);
            			ll.addView(cb);
            			
            		}
            		 
            		 
//            		Toast.makeText(getApplicationContext(),"the data is : "+g.toString(), Toast.LENGTH_LONG).show();
            		
            		pDialog.dismiss();
            	//	Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            	//	Toast.makeText(getApplicationContext(),"the result is : "+result, Toast.LENGTH_LONG).show();
            	}
            catch (JSONException e){
            	e.printStackTrace();
            	}
				
			else           
            	Toast.makeText(getApplicationContext(), "No data or No server", Toast.LENGTH_LONG).show(); 
			
		}
		

}
	class EditUserInfo extends AsyncTask<String, Integer, JSONObject> {
		public  ProgressDialog pDialog;
		//start progress dialog 		
				protected void onPreExecute() {
//					// TODO Auto-generated method stub
					super.onPreExecute();
					
					//??? 
					 pDialog = new ProgressDialog(EditProfile.this);
			            pDialog.setMessage("updating . . . ");
			            pDialog.setIndeterminate(false);
//			            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			            pDialog.setProgress(0);
			            pDialog.setCancelable(true);
			            pDialog.show();
			            
			            
				}
		@Override
		protected JSONObject doInBackground(String... arg0) {
		InputStream is;
		BitmapFactory.Options bfo;
		Bitmap bitmapOrg;
		ByteArrayOutputStream bao ;
		JSONObject res = null;
		bfo = new BitmapFactory.Options();
		bfo.inSampleSize = 2;
		
//		for(int i = 0 ; i < cbCatArrayList.size() ; i++){
//			ids += cbCatArrayList.get(i).getId()+",";
//			
//		}
		//bitmapOrg = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + customImage, bfo);
		
			data.add(new BasicNameValuePair("userid",MyID));
			data.add(new BasicNameValuePair("username",name));
			data.add(new BasicNameValuePair("useremail",email));
			data.add(new BasicNameValuePair("city",country));
			data.add(new BasicNameValuePair("interests",ids));
			
		 
		
		
		//data.add(new BasicNameValuePair("cmd","image_android"));
		if(networkConnection.isServerAvaliable(editURL)==true) //Server is Available....
 		{
   			JSONObject json = null;
   			HTTPAdapter jp = new HTTPAdapter();
   			json = jp.makeHttpRequestPost(editURL,data);
   			return json;
   		}
   		
   		else //Server Not Available
   		{
   			return null ;
   		}
		
		
		
		// (null);
		}
		 
		@Override
	    protected void onProgressUpdate(Integer... progress) {
	        //pDialog.setProgress(progress[0]);
//	        super.onProgressUpdate(progress);
//	        if (progress[0] < 5) {            
//	            pDialog.setProgress(0);
//	        } else if (progress[0] < 40) {            
//	            pDialog.setProgress(20);
//	        }
//	        else if (progress[0] < 60) {            
//	            pDialog.setProgress(40);
//	        }
//	        else if (progress[0] < 80) {            
//	            pDialog.setProgress(60);
//	        }
//	        else if (progress[0] < 100) {            
//	            pDialog.setProgress(80);
//	        }
//	        else if (progress[0] == 100) {            
//	            pDialog.setProgress(100);//
//	        }
	    }
		 
		@Override
		protected void onPostExecute(JSONObject result) {
			pDialog.dismiss();
			UserParser userParser = new UserParser();
			UserModel userModel = new UserModel();
			if(result != null){
				try {
					userParser.getResponse(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				userModel = userParser.getUserModel();
				 
				if(userModel.getError() == 0){
					DBHelper DB = new DBHelper(getApplicationContext());
					if(DB.updateContact(Integer.parseInt(MyID), name, email, null, country) != 0){
						if(cbCatArrayList.size() > 0){
							interestHelper.deleteInterests();
							catHelper.deleteCats();
						for(int i = 0 ; i < cbCatArrayList.size() ; i++){
							if(cbCatArrayList.get(i).isChecked()){
								long test = interestHelper.insertInterests(Integer.parseInt(MyID), cbCatArrayList.get(i).getId());
								if(test != 0){
									catHelper.insertCat(cbCatArrayList.get(i).getId(), cbCatArrayList.get(i).getText().toString());
									
//									Toast.makeText(getApplicationContext(), "interests and cats", Toast.LENGTH_LONG).show();
								}
								
							}
							
						}
					}
						
						finish();
						
						
					}else{
						Toast.makeText(getApplicationContext(), "Error updating data", Toast.LENGTH_LONG).show();
						
					}
				    
					
				}
				
//				Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
//				for (NameValuePair nvp : data) {
//				    String name = nvp.getName();
//				    String value = nvp.getValue();
//				    Toast.makeText(getApplicationContext(),"name :"+name+", value:"+value , Toast.LENGTH_LONG).show();
//				}
				
				
			}
		}
	}
	public void showMessageIfUserInputsEmpty(String userName,String userEmail)
    {
    	emptyEmail=validation.isEmptyMail(userEmail);
    	emptyUserName=validation.isEmptyUserName(userName);
    	
    	if(emptyEmail)
    	{
    		useremail.setError(" Email is Empty !");
    		
    	}
    	if(emptyUserName)
    	{
    		username.setError(" username is Empty !");
    		
    	}
    		
    	
    }
	 public void showMessageIfUserInputsNotValid(String userName,String userEmail)
	    {
	    	
	    	boolean validationUserInputs;
			validEmail=validation.isValidMail(userEmail);
			validUserName=validation.isValidUserName(userName);
		
			
			if(!validEmail)
			{
				useremail.setError("! Not Valid Email");
			}
			if(!validUserName)
			{
				username.setError("Please enter username < 50 character ");
			}
				
			
	    }
	 public boolean UserInputsValid(Boolean validEmail,Boolean validUserName){
	    	boolean validationUserInputs=validEmail && validUserName;
	    	if(validationUserInputs)
	    		return true;
	    	else 
				return false;
	    }
	 public boolean UserInputsNotEmpty(Boolean emptyEmail,
	    		Boolean emptyUserName){
	    	if(!emptyEmail && !emptyUserName)
	    		return true;
	    	else 
				return false;
	    }
	 /**
		 * This class is responsible for uploading data
		 * @author lauro
		 *
		 */
		 private class ImageUploader extends AsyncTask<Void, Integer, JSONObject> implements UploadProgressListener {

			 
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					progressDialog = createDialog();
					progressDialog.show();
				}

				@Override
				protected JSONObject doInBackground(Void... params) {
					 InputStream is = null;
					 JSONObject jObj = null;
					try{
					
								
			       
			        	
			        	InputStream inputStream = new FileInputStream(new File(imagePath));
			            
			            //*** CONVERT INPUTSTREAM TO BYTE ARRAY
			             
			            byte[] data = this.convertToByteArray(inputStream);
			                 
			            HttpClient httpClient = new DefaultHttpClient();
			            httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,System.getProperty("http.agent"));
			           
			            HttpPost httpPost = new HttpPost(UPLOAD_SERVER_URI);
			           
			            // STRING DATA
			            StringBody dataString = new StringBody("This is the sample image");
			            StringBody id = new StringBody(MyID);
			           
			            // FILE DATA OR IMAGE DATA
			            InputStreamBody inputStreamBody = new InputStreamBody(new ByteArrayInputStream(data),imageName);
			            
			           // MultipartEntity multipartEntity = new MultipartEntity();
			            CustomMultiPartEntity  multipartEntity = new CustomMultiPartEntity();
			            
			            // SET UPLOAD LISTENER
			            multipartEntity.setUploadProgressListener(this);
			           
			            //*** ADD THE FILE
			            multipartEntity.addPart("image", inputStreamBody);
			                
			            //*** ADD STRING DATA
			            multipartEntity.addPart("description",dataString);  
			            multipartEntity.addPart("user_id",id); 
			            
			            httpPost.setEntity(multipartEntity);
			            httpPost.setEntity(multipartEntity);
			            
			            // EXECUTE HTTPPOST
			            HttpResponse httpResponse = httpClient.execute(httpPost);
			            
			            // THE RESPONSE FROM SERVER	         
			            HttpEntity httpEntity = httpResponse.getEntity();
		                is = httpEntity.getContent();
		                BufferedReader reader = new BufferedReader(new InputStreamReader(
		                        is, "iso-8859-1"), 8);
		                StringBuilder sb = new StringBuilder();

		                String line = null;
		                while ((line = reader.readLine()) != null) {
		                    sb.append(line + "\n");

		                }
		                is.close();
		                String json = sb.toString();
//		                Log.v("nnnnnnnnnnnnnnnnnnnnnnnnnnn", json);

							 jObj = new JSONObject(json);

//							 Toast.makeText(EditProfile.this, "jObj: "+jObj, Toast.LENGTH_SHORT).show();
				
					 
				
				}catch (Exception e) {
					// TODO: handle exception
					Log.d("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv", e.toString())	;			}
					return jObj;
				}
				
				/**
				 * 
				 */
				@Override
				public void transferred(long num) {
					
					// COMPUTE DATA UPLOADED BY PERCENT
					
					long dataUploaded = ((num / 1024) * 100 ) / imageSize;
					 
					// PUBLISH PROGRESS
					
					this.publishProgress((int)dataUploaded);
					
				}
				
				/**
				 * Convert the InputStream to byte[]
				 * @param inputStream
				 * @return
				 * @throws IOException
				 */
				private byte[] convertToByteArray(InputStream inputStream) throws IOException{
					
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
		               
	                int next = inputStream.read();
	                while (next > -1) {
	                    bos.write(next);
	                    next = inputStream.read();
	                }
	                
	                bos.flush();
	                
	                return bos.toByteArray();
				}
				
				
				
				@Override
				protected void onProgressUpdate(Integer... values) {
					super.onProgressUpdate(values);
					
					// UPDATE THE PROGRESS DIALOG
					
					progressDialog.setProgress(values[0]);
					
					
					
				}

				@Override
				protected void onPostExecute(JSONObject uploaded) {
					// TODO Auto-generated method stub
					super.onPostExecute(uploaded);
					
					if(uploaded != null){
					try {
						JSONArray data = uploaded.getJSONArray("contents");
						//Toast.makeText(EditProfile.this, "data:"+data, Toast .LENGTH_SHORT).show();

						for(int i = 0 ; i < data.length() ; i++){
							JSONObject obj = data.getJSONObject(i);
							int error = Integer.parseInt(obj.getString("error"));
							if(error == 0 && obj.has("user_image")){
								String image = "http://7girls.byethost7.com/public/"+obj.getString("user_image");
								if(db.updateImage(Integer.parseInt(MyID),image) != 0){
									Toast.makeText(EditProfile.this, "Image updated successfully", Toast.LENGTH_SHORT).show();
									
									
								}
								
							}else if(error == 1){
								Toast.makeText(EditProfile.this, "Error updating data!", Toast .LENGTH_SHORT).show();

								
							}else if(error == 2){
								Toast.makeText(EditProfile.this, "Error uploading image", Toast .LENGTH_SHORT).show();

							}else if(error == 3){
								Toast.makeText(EditProfile.this, "Empty data is not allowed", Toast .LENGTH_SHORT).show();

							}else if(error == 4){
								Toast.makeText(EditProfile.this, "Make sure You're logged in", Toast .LENGTH_SHORT).show();

							}else if(error == 5){
								Toast.makeText(EditProfile.this, "Image only is allowed !", Toast .LENGTH_SHORT).show();

							}else if(error == 6){
								Toast.makeText(EditProfile.this, "Image size must be less than 1 MB", Toast .LENGTH_SHORT).show();

							}else{
								Toast.makeText(EditProfile.this, "Something went wrong !", Toast .LENGTH_SHORT).show();
								
							}
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}else{
						Toast.makeText(EditProfile.this, "Response "+uploaded, Toast.LENGTH_SHORT).show();

						
					}
					progressDialog.dismiss();

					
					
//					if(uploaded){
						
						// UPLOADING DATA SUCCESS
						
						
						
					
//					}else{
//						
//						// UPLOADING DATA FAILED
//						
//						progressDialog.setMessage("Uploading Failed");
//						progressDialog.setCancelable(true);
//						
//						
//					}
					
					
				}

				

			}
		 private ProgressDialog createDialog(){
		    	ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
		        progressDialog.setMessage("Please wait.. Uploading File");
		        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		        progressDialog.setCancelable(false);
		        
		        return progressDialog;
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
		 
		   
}
