package models;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class JoinedEventsHelper extends SQLiteOpenHelper {
	
	 private HashMap hp;
		public static final String DATABASE_NAME = "LocalEvents.db";
	    public static final String EVENT_TABLE_NAME =EventModel.table_name;
	    public static final String EVENT_COLUMN_NAME ="event_name";
	    public static final String EVENT_COLUMN_ID="event_id";
	    public static final String EVENT_COLUMN_IMAGE="event_photo";
	    public static final String EVENT_COLUMN_START_DATE="event_start_date";
	    public static final String EVENT_COLUMN_END_DATE="event_end_date";
	    public static final String EVENT_DESCRIPTION="event_description";
	    public static final String EVENT_CAT_ID="cat_id";
	    public static final String EVENT_ORGANIZATION_ID="org_id";	    
	    public static final String EVENT_START_TIME="event_start_time";
	    public static final String EVENT_END_TIME="event_end_time";
	    public static final String EVENT_LOCATION_NAME="location_name";
	    public static final String EVENT_CATEGORY_NAME="cat_name";
	    public static final String EVENT_ORGANIZATION_NAME="org_name";
	    
	    public static final String EVENT_PRIMARY_ID="primaryid";
	    
	    
	    public static final int DATABASE_VERSION=4;
	    

	public JoinedEventsHelper(Context context) {
		 super(context, DATABASE_NAME , null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL(
			      "CREATE TABLE events " +
			      "(eventprimaryid AUTOINCREMENT NOT NULL ,event_name text,event_id INTEGER,event_photo text,event_start_date text," +
			      "event_end_date text,event_description text,event_end_time text,event_start_time text,location_name text,org_name text)"
			      );
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS events");
	    onCreate(db);
	}
	
	
	public Boolean insertEvent(int event_id,String event_name,String event_start_date,String event_end_date,String event_photo,String event_description,String event_end_time,String event_start_time,String location, String org_name)
	{
		 SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put("event_id", event_id);  
	      contentValues.put("event_name", event_name);   
	      contentValues.put("event_start_date", event_start_date);	
	      contentValues.put("event_end_date",event_end_date);
	      contentValues.put("event_photo",event_photo);
	      contentValues.put("event_description",event_description);
	      contentValues.put("event_end_time",event_end_time);
	      contentValues.put("event_start_time",event_start_time);	      
	      contentValues.put("location_name",location);
	      contentValues.put("org_name",org_name);
	      db.insert("events", null, contentValues);
	      db.close();
	      return true;
	}
	
	
	public Integer deleteEvent (Integer eventid)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      
	      return db.delete("events", 
	      "id = ? ", 
	      new String[] { Integer.toString(eventid) });
	     // db.close();
	   }

	public boolean deleteData(){
		   SQLiteDatabase db = this.getReadableDatabase();
		   db.execSQL("delete from events");
		   return true;
		   
	   }
	
}
