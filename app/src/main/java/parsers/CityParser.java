package parsers;

import java.util.ArrayList;

import models.CityModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import interfaces.ParserInterface;

public class CityParser implements ParserInterface{
	Context cn;
	private CityModel cityObj ;
	public ArrayList<CityModel> cityArrayList = new ArrayList<CityModel>();
	
	

	public CityParser(Context cn) {
		
		this.cn = cn;
	}
	@Override
	public void getResponse(JSONObject cityJA) throws JSONException {
		// TODO Auto-generated method stub
		JSONArray jArray = cityJA.getJSONArray("contents");
		
		for(int i = 0 ; i < jArray.length();i++){
			JSONObject jObj = jArray.getJSONObject(i);
			if(jObj.has("error")){
			CityModel.error = Integer.parseInt(jObj.getString("error"));
			}else if(jObj.has("city_id") && jObj.has("city_name")){
				cityObj = new CityModel();
				cityObj.setCity_id(jObj.getString("city_id"));
				cityObj.setCity_name(jObj.getString("city_name"));
				cityArrayList.add(cityObj);
				//Toast.makeText(cn, "city parser "+cityObj.getCity_name(), Toast.LENGTH_LONG).show();
			}
						
			
		}
//		for(int i = 0 ; i < cityArrayList.size() ; i++){
//			Toast.makeText(cn, "city parser "+cityArrayList.get(i).getCity_name(), Toast.LENGTH_LONG).show();
//			
//		}
		
		
	}
	public ArrayList<CityModel> getCitiesArrayList(){
		return cityArrayList;
	}
	public CityModel getCityModel(){
		return cityObj;
	}

}
