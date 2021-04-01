package models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoriesHelper {
	public static Context cn;
	public static final String CATS_TABLE_NAME ="cats";
	public static final String CAT_COLUMN_NAME ="cat_name";
	public DBHelper dbhelper = new DBHelper(cn);

	public Cursor getCats(int id){
	      SQLiteDatabase db = dbhelper.getReadableDatabase();
	      
	      Cursor res =  db.rawQuery( "select * from "+CATS_TABLE_NAME+" where cat_id="+id, null );
	      
	     // db.close();
	      return res;
	      
	   }
	public Cursor getCats(){
	      SQLiteDatabase db = dbhelper.getReadableDatabase();
	      
	      Cursor res =  db.rawQuery( "select * from "+CATS_TABLE_NAME, null );
	      
	     // db.close();
	      return res;
	      
	   }
 public Boolean insertCat(int cat_id,String cat_name)
	{
		 SQLiteDatabase db = dbhelper.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put("cat_id", cat_id);  
	      contentValues.put("cat_name", cat_name);    
	      db.insert(CATS_TABLE_NAME, null, contentValues);
	      db.close();
	      return true;
	}
 public boolean deleteCats(){
	   SQLiteDatabase db = dbhelper.getReadableDatabase();
	   db.execSQL("delete from "+CATS_TABLE_NAME);
	   return true;
	   
 }
 public int updateCats (Integer cat_id, String cat_name)
 {
    SQLiteDatabase db = dbhelper.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("cat_name",cat_name); 
    return db.update(CATS_TABLE_NAME, contentValues, "cat_id = ? ", new String[] { Integer.toString(cat_id) } );
//    db.close();
   
 }
}
