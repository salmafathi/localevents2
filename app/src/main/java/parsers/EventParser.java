package parsers;

import java.util.ArrayList;
import models.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import interfaces.EventParserInterface;

public class EventParser implements EventParserInterface  
{
	Context cn;
	JSONObject jObj;
	ArrayList<EventModel> eventsParsedData;
	ArrayList<EventModel> eventsArray;
	EventModel parsedData;
	ArrayList<EventModel> data;
	
	
	@Override
	public ArrayList<EventModel> getResponse(JSONObject ja) throws JSONException 
	{
		int errors=0;
		data=new ArrayList<EventModel>();
		JSONArray jArray = ja.getJSONArray("contents");
		
		for(int i=0;i<jArray.length();i++)
		{
			 jObj = jArray.getJSONObject(i);

			 if(i==0)
			 {
				 Log.d("first object   ","000");
				
				 errors= Integer.parseInt(jObj.getString("error"));
				 
				 	 if(errors==0)
				 	 {
				 		Log.d("No errors   ","Noha");
				 		 continue;
				 	 }
				 	else
				 	 {
				 		Log.d("out from for ","okay");
				 		
				 		 break;
				 	 }
//				 	 
			 }
			 
			 else
			 {
				 Log.d("object catched",jObj.toString());
				 parsedData= (EventModel) parseEventData(jObj, i);
				 Log.d("object parsed","jii");
				 data.add(parsedData);
			 }
			 
//			 Log.d("object parsed",jObj.toString());
			 
			 
		}
		return data;
	}
	
	public Object parseEventData(JSONObject eventdata,int i) throws JSONException
	{
		
		
		EventModel events=new EventModel();
		
		String event_name=jObj.getString("event_name");
		String event_photo=jObj.getString("event_photo");
		String event_start_date=jObj.getString("event_start_date");
		String event_end_date=jObj.getString("event_end_date");
		String org_name=jObj.getString("organizer_name");
		String cat_name=jObj.getString("category_name");
		String location_name=jObj.getString("location_name");
		String event_id=jObj.getString("event_id");
		String event_description = jObj.getString("event_description");
		String event_start_time = jObj.getString("event_start_time");
		String event_end_time = jObj.getString("event_end_time");
		String join = jObj.getString("join");
		String save = jObj.getString("save");
		String city_name = jObj.getString("city_name");
		
		
			Log.d("error message", "correct data");
			events.setCat_name(cat_name);
			events.setEvent_name(event_name);
			events.setEvent_photo(event_photo);
			events.setEvent_start_date(event_start_date);
			events.setEvent_end_date(event_end_date);
			events.setOrg_name(org_name);
			events.setLocation_name(location_name);
			events.setEvent_id(event_id);
			events.setEvent_description(event_description);
			events.setEvent_start_time(event_start_time);
			events.setEvent_end_time(event_end_time);
			events.setJoin_Status(join);
			events.setSave_Status(save);
			events.setCity_name(city_name);
			Log.d("name", event_name);
		
	
		  return events;
	}

}
