package parsers;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.example.localevents.Register;

import models.UserModel;
import interfaces.ParserInterface;
// variables
//// CREATE TABLE IF NOT EXISTS `user` (
//  `user_id` int(11) NOT NULL AUTO_INCREMENT,
//  `user_name` varchar(35) NOT NULL,
//  `user_password` varchar(30) NOT NULL,
//  `user_email` varchar(50) NOT NULL,
//  `user_city` varchar(30) NOT NULL,
//  `user_image` varchar(150) NOT NULL,
//  `is_deleted` enum('0','1','','') DEFAULT NULL,
//  PRIMARY KEY (`user_id`)
//) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

public class UserParser implements ParserInterface{
	private UserModel userObj = new UserModel();
	private ArrayList<UserModel> userArrayList = new ArrayList<UserModel> ();
	

	@Override
	public void getResponse(JSONObject userJA) throws JSONException {
		// TODO Auto-generated method stub
		
		
			JSONArray jArray = userJA.getJSONArray("contents");
		
		for(int i = 0 ; i < jArray.length();i++){
			JSONObject jObj = jArray.getJSONObject(i);
			userObj.setError(Integer.parseInt(jObj.getString("error")));
			
			//Log.d("++++++  ",jObj.getString("id"));
			
			if(jObj.has("id")){
				userObj.setUser_id(jObj.getString("id"));
			}
			
			
//			userObj.setUser_name(jObj.getString("user_name"));
//			userObj.setUser_password(jObj.getString("user_password"));
//			userObj.setUser_email("user_email");
//			userObj.setUser_image("user_image");
//			userObj.setIs_deleted(Integer.parseInt(jObj.getString("is_deleted")));
			userArrayList.add(userObj);
			
			
		}
		
		
	}
	public ArrayList<UserModel> getUsersArrayList(){
		return userArrayList;
	}
	public UserModel getUserModel(){
		return userObj;
	}
	

}
