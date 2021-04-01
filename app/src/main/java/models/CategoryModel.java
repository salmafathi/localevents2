package models;

import java.io.Serializable;

import android.R.integer;
import android.graphics.Bitmap;

public class CategoryModel implements Serializable
{

	static String table_name= "categories";
	private int error;
	private  int cat_id;
	private String cat_name;
	private Bitmap bitmap;
	
	private String cat_photo;
	private int is_deleted;
	public static String getTable_name() {
		return table_name;
	}
	public static void setTable_name(String table_name) {
		CategoryModel.table_name = table_name;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public String getCat_photo() {
		return cat_photo;
	}
	public void setCat_photo(String cat_photo) {
		this.cat_photo = cat_photo;
	}
	public int getIs_deleted() {
		return is_deleted;
	}
	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	@Override
	public String toString() {
		return "CategoryModel [cat_name=" + cat_name + ", cat_photo="
				+ cat_photo + "]";
	}
	public Bitmap getbitmap(){
		return bitmap ;
	}
	public void setbitmap(Bitmap bitmap){
		this.bitmap = bitmap ;
	}
	
	
}
