
package parsers;
import java.util.ArrayList;
import java.util.List;



import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;



import models.CategoryModel;

import interfaces.CatParserInterface;


public class CategoryParser implements CatParserInterface{
	
	Context cn;
	JSONObject jObj;
	ArrayList<CategoryModel> data;
	List<NameValuePair> params = new ArrayList<NameValuePair>(); //preparing a list of key/value for mail/password
	ArrayList<CategoryModel>catArr;
	ArrayList<CategoryModel> catss;
	
	
	public CategoryParser() {
		
	}
	
	@Override
	public ArrayList<CategoryModel> getResponse(JSONObject userJA) throws JSONException {
		// TODO Auto-generated method stub
		
		int errors=0;
			JSONArray jArray = userJA.getJSONArray("contents");
		catArr=new ArrayList<CategoryModel>();
		for(int i = 0 ; i < jArray.length();i++){
			 jObj = jArray.getJSONObject(i);
			Log.d("object catched",jObj.toString());
			if(i==0)
			{
				Log.d("first object   ","000");
			 errors= Integer.parseInt(jObj.getString("error"));
			 	 if(errors==0)

			 	 {
			 		Log.d("No errors   ","salma0");
			 		 continue;
			 	 }
			 	 else
			 	 {
			 		Log.d("out from for ","salma20");
			 		
			 		 break;
			 	 }
			 	 
			}
			
			else
			{
				Log.d("parcing data   ","welcome");
				// parse data
			 data =parseCategoriesData(jObj, i);
			 catArr.addAll(data);
//			 Log.d("data here"+i, data.toString());		
			}	
		}
		
		return catArr;
	}
	
	public  ArrayList<CategoryModel> parseCategoriesData(JSONObject catData,int i ) throws JSONException
	{
		catss=new ArrayList<CategoryModel>();
		CategoryModel CatMod= new CategoryModel();
		String catname = jObj.getString("category_name");  
		String catID = jObj.getString("category_id");
		String catPhoto = jObj.getString("cat_photo");
		
		CatMod.setCat_name(catname);
		CatMod.setCat_id(Integer.parseInt(catID));
		CatMod.setCat_photo(catPhoto);
		
		catss.add(CatMod);
		return catss;
		
	}
	

}
