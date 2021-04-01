package com.example.localevents;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

//import com.facebook.android.DialogError;
//import com.facebook.android.Facebook;
//import com.facebook.android.FacebookError;
//import com.facebook.android.Util;
//import com.facebook.android.Facebook.DialogListener;
//import com.facebook.widget.LoginButton;

import generics.HTTPAdapter;
import generics.sharedprefs;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;		
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Home extends Activity  {
	
	Button register;
	Button login;
	Intent intent;
	ActionBar actionBar;
	SharedPreferences sharedpreferences ;
	public static final String MyPREFERENCES = "MyPrefs" ;
	
	String APP_ID;
	//Facebook fb;
	//LoginButton fbButton;
	ImageView pic;
	String access_token;
	Long expires;
	
	Editor editor;
	EditText text1;
	HTTPAdapter httpadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.home);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		actionBar=getActionBar();
		SpannableString s = new SpannableString("Local Events");
		s.setSpan(new TypefaceSpan("fonts/GOTHICBI.TTF"), 0, s.length(),
	            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		actionBar.setTitle(s);
		
		getActionBar().setIcon(R.drawable.logo);
		Typeface font = Typeface.createFromAsset(getAssets(), "fonts/GOTHICBI.TTF");
		
		// Add code to print out the key hash
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.example.localevents", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }
		
		APP_ID=getString(R.string.APP_ID);
		//fb=new Facebook(APP_ID);
		
		register=(Button) findViewById(R.id.register);
		login=(Button) findViewById(R.id.login);
		register.setTypeface(font);
		login.setTypeface(font);
		
	//	final LoginButton button=(LoginButton) findViewById(R.id.loginWithFacebook);
	//	button.setTypeface(font);
		
	//	pic=(ImageView) findViewById(R.id.pic);
	//	text1=(EditText) findViewById(R.id.edittext1);
		//button.setOnClickListener(this);
		//getUserDataIfUserLoginWzFb();
		
		if(sharedprefs.containid(Home.this))
		{
			Intent intent=new Intent(Home.this,MainActivityUser.class);
			startActivity(intent);
		    Home.this.finish();
		}
//		sharedpreferences=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
//		access_token=sharedpreferences.getString("access_token", null);
//		expires=sharedpreferences.getLong("access_expires", 0);
//		if(access_token!=null){
//			fb.setAccessToken(access_token);
//		}
//		if(expires!=0){
//			fb.setAccessExpires(expires);
//		}
//		

		
//		sharedpreferences=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
//		if(sharedpreferences.contains("ID")){
//			Intent intent=new Intent(Home.this,MainActivityUser.class);
//			startActivity(intent);
//		    Home.this.finish();
//			
//		}
		
	}

    

//	private void getUserDataIfUserLoginWzFb() {
//		// TODO Auto-generated method stub
//		if(fb.isSessionValid())
//		{
//			//Intent intent=new Intent(Home.this,MainActivityUser.class);
//			//startActivity(intent);
//			//Home.this.finish();
//			//fbButton.setImageResource(R.drawable.logout_button);
//			JSONObject jsonObject=null;
//			URL img_url=null;
//			String id,name,email,city,mm;
//			id=name=email=null;
//			Bitmap bitmap=null;
//			try {
//				String JsonUser=fb.request("me");
//				jsonObject=Util.parseJson(JsonUser);
//				id=jsonObject.optString("id");
//				name=jsonObject.optString("name");
//				email=jsonObject.optString("email");
//				city=jsonObject.getJSONObject("location").getString("name");;
//				img_url=new URL("https://graph.facebook.com/"+id+"/picture?type=normal");
//				bitmap=BitmapFactory.decodeStream(img_url.openConnection().getInputStream());
//				pic.setImageBitmap(bitmap);
//				text1.setText(city);
//				
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (FacebookError e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
////			intent=new Intent(Home.this,MainActivityUser.class);
////			intent.putExtra("FbID", id);
////			intent.putExtra("FbName", name);
////			intent.putExtra("FbEmail", email);
////			intent.putExtra("FbBitmap", bitmap);
////			startActivity(intent);
////			Home.this.finish();
//			/*************************/
//			
//		}
//	}



	public void register(View v){
		intent=new Intent(this,Register.class);
		startActivity(intent);
	}
	public void login(View v){
		intent=new Intent(this,Login.class);
		startActivity(intent);
	}



	//@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if(fb.isSessionValid()){
//			//button close our session- logout of facebook
//			
//				
//					//fb.logout(Home.this);
//			getUserDataIfUserLoginWzFb();
//				
//			
//			
//		}else{
//			//login user with facebook
//			//get permission from user
//			fb.authorize(Home.this,new String[] {"email","user_friends","user_location"} ,new DialogListener() {
//				
//				@Override
//				public void onFacebookError(FacebookError e) {
//					// TODO Auto-generated method stub
//					Toast.makeText(Home.this, "Error to login with facebook", Toast.LENGTH_LONG).show();
//				}
//				
//				@Override
//				public void onError(DialogError e) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onComplete(Bundle values) {
//					// TODO Auto-generated method stub
//					//Toast.makeText(Home.this, "User Login with facbook", Toast.LENGTH_LONG).show();
////					editor=sharedpreferences.edit();
////					editor.putString("access_token", fb.getAccessToken());
////					editor.putLong("access_expires", fb.getAccessExpires());
////					editor.commit();
//					getUserDataIfUserLoginWzFb();
//				}
//				
//				@Override
//				public void onCancel() {
//					// TODO Auto-generated method stub
//					Toast.makeText(Home.this, "You cancel login with facebook", Toast.LENGTH_LONG).show();
//				}
//			});
//		}
//	}



//@Override
//protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	// TODO Auto-generated method stub
//	super.onActivityResult(requestCode, resultCode, data);
//	fb.authorizeCallback(requestCode, resultCode, data);
//}
	
}
