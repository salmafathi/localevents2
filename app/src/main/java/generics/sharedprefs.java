package generics;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class sharedprefs {
	
	Context context ;
	
	public sharedprefs(Context context){
		this.context = context;
	}
	
	public static void setDefaults(String key, String value, Context context) {
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(key, value);
	    editor.commit();
	    
	}

	public static String getDefaults(String key, Context context) {
	    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	    return preferences.getString(key, "Not Found");
	}
	
	public static void deleteDefaults(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
	      editor.clear();
	      editor.commit();
		
	}
	
	public static boolean containid(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if(preferences.contains("ID")){
			return true;
		}
		else{
			return false;
		}
		
		
	}

}
