package models;

import android.R.integer;

public class UserModel
{
	// variables
//	// CREATE TABLE IF NOT EXISTS `user` (
//	  `user_id` int(11) NOT NULL AUTO_INCREMENT,
//	  `user_name` varchar(35) NOT NULL,
//	  `user_password` varchar(30) NOT NULL,
//	  `user_email` varchar(50) NOT NULL,
//	  `user_city` varchar(30) NOT NULL,
//	  `user_image` varchar(150) NOT NULL,
//	  `is_deleted` enum('0','1','','') DEFAULT NULL,
//	  PRIMARY KEY (`user_id`)
//	) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
	static String table_name= "user";
	private int error;
	private  String user_id;
	private String user_name;
	private String user_password;
	private String user_email;
	private String user_image;
	private int is_deleted;
	
	
	public int getError() {
		return error;
	}
	
	public void setError(int error) {
		this.error = error;
	}
	// setter & getters
	public String getUser_id() {
		return user_id;
	}
	
	//** I think no need to implement setter method here */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	
	public String getUser_name() {
		return user_name;
	}
	
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	
	public String getUser_password() {
		return user_password;
	}
	
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	
	
	public String getUser_email() {
		return user_email;
	}
	
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	
	
	public String getUser_image() {
		return user_image;
	}
	
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	
	
	public int getIs_deleted() {
		return is_deleted;
	}
	
	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	
}
