package generics;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.localevents.Register;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import generics.HTTPAdapter;

 public class HTTPAsyncHelper extends AsyncTask<JSONObject, Void, JSONObject>{
	 Context cn;
	 String url;
	 public List<NameValuePair> list = new ArrayList<NameValuePair>();
	 public HTTPAsyncHelper(Context _cn,String _url){
		 cn = _cn;
		 url = _url;
		 
	 }
	@Override
	protected JSONObject doInBackground(JSONObject... params) {
		// TODO Auto-generated method stub
		
		//Check Server is available or not !
		NetworkConnection checknetwork = new NetworkConnection(cn);
		boolean serverconnect = checknetwork.isServerAvaliable(url);
		//if(serverconnect==true) //Server is Available....
		//{
			JSONObject json = null;
			HTTPAdapter jp = new HTTPAdapter();
			json = jp.makeHttpRequestPost(url,list);
			return json;
		//}
		
		//else //Server Not Available
		//{
			//return null ;
		//}

		//String jString = json.toString();
		
	}
	
	protected void onPostExecute(JSONObject result){
		
		super.onPostExecute(result);
		
		if(result!=null) //When Server is Available
		{
		//parseData(result);
		Toast.makeText(cn, result.toString(), Toast.LENGTH_LONG).show();
		}
		else{ // When Server is Not Available
			Toast.makeText(cn, "Server Not Found", Toast.LENGTH_LONG).show();
		}
		
	}

}
