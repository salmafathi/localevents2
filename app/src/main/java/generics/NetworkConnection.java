package generics;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkConnection {
	Context cn;
	
	public NetworkConnection(Context cn){
		this.cn=cn;
	}
	
	public boolean isNetworkOnline() {
	    boolean status=false;
	    try{
	        ConnectivityManager cm = (ConnectivityManager) cn.getSystemService(Context.CONNECTIVITY_SERVICE);
	      // cn.getSystemService(cn.CONNECTIVITY_SERVICE).getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	        NetworkInfo netInfo = cm.getNetworkInfo(0);
	        if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
	            status= true;
	        }else {
	            netInfo = cm.getNetworkInfo(1);
	            if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
	                status= true;
	        }
	    }catch(Exception e){
	        e.printStackTrace();  
	        return false;
	    }
	    return status;

	    }  
	
	//check server Availability
	public boolean isServerAvaliable(String serverurl){
		boolean serverstatus=true;
		//int res = 0 ;
		try{
//			HttpGet httpRequest = new HttpGet(serverurl);
//			HttpEntity httpEntity = null;
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpResponse response = httpclient.execute(httpRequest);
//			 res = response.getStatusLine().getStatusCode();
//			Toast.makeText(cn,res+"", Toast.LENGTH_LONG).show();
//			Log.i("eeeeeeeeeeeeeror",res+"hna");
			
			URL url = new URL(serverurl);
			//Log.i("the url",url.toString() );
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			
			urlconnection.connect();
			//http response = 200 when it is OK with get and post (there are another values of sucess!!)
			//Toast.makeText(cn,urlconnection.getResponseCode(), Toast.LENGTH_LONG).show();
			if(urlconnection.getResponseCode()==200){
				serverstatus = true ;
			}
			else{
				serverstatus = false ;
			}
			
	     }
		catch(Exception e)
		{
	        e.printStackTrace();  
	        return false;
	    }
		return serverstatus ;
	}

}
