package models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class InterestsHelper {
    public static final String INTERESTS_TABLE_NAME ="interests";
    public static Context cn;
	DBHelper dbhelper = new DBHelper(cn);
    
    public Cursor getInterests(){
	      SQLiteDatabase db = dbhelper.getReadableDatabase();
	      
	      Cursor res =  db.rawQuery( "select * from "+INTERESTS_TABLE_NAME, null );
	      
	     // db.close();
	      return res;
	      
	   }
 public long insertInterests(int user_id,int interest_id)
	{
		 SQLiteDatabase db = dbhelper.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put("user_id", user_id);  
	      contentValues.put("interest_id", interest_id);    
	      long insert = db.insert(INTERESTS_TABLE_NAME, null, contentValues);
	      db.close();
	      return insert;
	}
 public boolean deleteInterests(){
	   SQLiteDatabase db = dbhelper.getReadableDatabase();
	   db.execSQL("delete from "+INTERESTS_TABLE_NAME);
	   return true;
	   
 }
 public int updateInterests (Integer user_id, Integer interest_id)
 {
    SQLiteDatabase db = dbhelper.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("interest_id",interest_id); 
   return db.update(INTERESTS_TABLE_NAME, contentValues, "user_id = ? ", new String[] { Integer.toString(user_id) } );
//    db.close();
    
 }

}
