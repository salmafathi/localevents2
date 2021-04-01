package models;

public class CityModel {
	public static int error;
	private  String city_id;
	private String city_name;
	public int getError() {
		return error;
	}
	
	
	// setter & getters
	public String getCity_id() {
		return city_id;
	}
	
	//** I think no need to implement setter method here */
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	
	
	public String getCity_name() {
		return city_name;
	}
	
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	

}
