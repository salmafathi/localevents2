package models;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper  extends SQLiteOpenHelper
{
	
    private HashMap hp;
	public static final String DATABASE_NAME = "LocalEvents.db";
    public static final String USER_TABLE_NAME =UserModel.table_name;
    public static final String USER_COLUMN_NAME ="user_name";
    public static final String USER_COLUMN_ID="user_id";
    public static final String USER_COLUMN_IMAGE="user_image";
    public static final String USER_COLUMN_EMAIL="user_email";
    public static final String USER_COLUMN_CITY="user_city";
    public static final int DATABASE_VERSION=9;

    public static final String INTERESTS_TABLE_NAME ="interests";
   
    public DBHelper(Context context)
    {
       super(context, DATABASE_NAME , null, DATABASE_VERSION);
      
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		 db.execSQL(
			      "CREATE TABLE IF NOT EXISTS user " +
			      "(user_id INTEGER PRIMARY KEY,user_name text NULL,user_email text NULL,user_image text NULL,user_city text NULL)"
			      );
		 
		 db.execSQL(
			      "CREATE TABLE IF NOT EXISTS interests " +
			      "(user_id INTEGER NULL,interest_id INTEGER NULL)"
			      ); 
		 db.execSQL(
			      "CREATE TABLE IF NOT EXISTS cats " +
			      "(cat_id INTEGER PRIMARY KEY,cat_name text NULL)"
			      );
	}

	@Override
	public void onUpgrade(SQLiteDatabase  db, int oldVersion, int newVersion) {
		 db.execSQL("DROP TABLE IF EXISTS user");
	      onCreate(db);
		
	}
	
	public Boolean insertUser(int user_id,String user_name,String user_email,String user_image,String user_city)
	{	
		//Toast.makeText(cn,"insert method", Toast.LENGTH_LONG).show();
		 SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put("user_id", user_id);  
	      contentValues.put("user_name", user_name);   
	      contentValues.put("user_email", user_email);	
	      contentValues.put("user_image",user_image); 
	      contentValues.put("user_city",user_city);  
	      db.insert("user", null, contentValues);
//	      db.close();
	      return true;
	}
	public Cursor getData(int id){
	      SQLiteDatabase db = this.getReadableDatabase();
	      
	      Cursor res =  db.rawQuery( "select * from user where user_id="+id, null );
	      
//	      db.close();
	      return res;
	      
	   }
	   public int numberOfRows(){
	      SQLiteDatabase db = this.getReadableDatabase();
	      int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_TABLE_NAME);
	      db.close();
	      return numRows;
	   }
	   
	   public int updateContact (Integer user_id, String user_name, String user_email, String user_image , String user_city)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put("user_name", user_name);   
	      contentValues.put("user_email", user_email);
	      if(user_image != null)
	    	  contentValues.put("user_image",user_image); 
	      contentValues.put("user_city",user_city); 
	      return db.update("user", contentValues, "user_id = ? ", new String[] { Integer.toString(user_id) } );
//	      db.close();
	      
	   }
	   public int updateImage(Integer user_id,String user_image){
		   SQLiteDatabase db = this.getWritableDatabase();
		      ContentValues contentValues = new ContentValues();  
		      if(user_image != null)
		    	  contentValues.put("user_image",user_image);  
		      return db.update("user", contentValues, "user_id = ? ", new String[] { Integer.toString(user_id) } );
		   
	   }
	   
	   public Integer deleteContact (Integer id)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      
	      return db.delete("contacts", 
	      "id = ? ", 
	      new String[] { Integer.toString(id) });
	   }
	   
	   public ArrayList<String>  getAllContacts()
	   {
	      ArrayList<String> array_list = new ArrayList<String>();
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from user", null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
	      array_list.add(res.getString(res.getColumnIndex(USER_COLUMN_NAME)));
	      res.moveToNext();
	      }
	   return array_list;
	   }
	   public boolean deleteData(){
		   SQLiteDatabase db = this.getReadableDatabase();
		   db.execSQL("delete from user");
		   
		   return true;
		   
	   }
	   
	  
	   

}
