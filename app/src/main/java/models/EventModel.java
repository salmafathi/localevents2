package models;

import java.io.Serializable;

public class EventModel implements Serializable
{
	static String table_name= "events";
	private int error;
	private String event_id;
	private String event_name;
	private String event_start_date;
	private String event_end_date;
	private String event_photo;
	private int is_deleted;
	private String event_description;
	private int cat_id;
	private int org_id;
	private String event_start_time;
	private String event_end_time;
	private String location_name;
	private String city_name ;
	private String cat_name;
	private String org_name;
	private String join_status ;
	private String save_status ;
	
	
	
	public void setCity_name(String city_name){
		this.city_name = city_name ;
	}
	
	public String getcity_name(){
		return this.city_name ;
	}
	
	public void setSave_Status(String save_status){
		this.save_status = save_status ;
	}
	
	public String getSave_Status(){
		return this.save_status ;
	}
	
	
	
	public void setJoin_Status(String join_status){
		this.join_status = join_status ;
	}
	
	public String getJoin_Status(){
		return this.join_status ;
	}
	
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	
	
	public static String getTable_name() {
		return table_name;
	}
	public static void setTable_name(String table_name) {
		EventModel.table_name = table_name;
	}
	
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	
	public String getEvent_start_date() {
		return event_start_date;
	}
	public void setEvent_start_date(String event_start_date) {
		this.event_start_date = event_start_date;
	}
	
	public String getEvent_end_date() {
		return event_end_date;
	}
	public void setEvent_end_date(String event_end_date) {
		this.event_end_date = event_end_date;
	}
	
	public String getEvent_photo() {
		return event_photo;
	}
	public void setEvent_photo(String event_photo) {
		this.event_photo = event_photo;
	}
	
	public int getIs_deleted() {
		return is_deleted;
	}
	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	
	public String getEvent_description() {
		return event_description;
	}
	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}
	
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	
	public int getOrg_id() {
		return org_id;
	}
	public void setOrg_id(int org_id) {
		this.org_id = org_id;
	}
	
	public String getEvent_start_time() {
		return event_start_time;
	}
	public void setEvent_start_time(String event_start_time) {
		this.event_start_time = event_start_time;
	}
	
	public String getEvent_end_time() {
		return event_end_time;
	}
	public void setEvent_end_time(String event_end_time) {
		this.event_end_time = event_end_time;
	}
	
	
	
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	@Override
	public String toString() {
		return "EventModel [event_name=" + event_name + "]";
	}
	
	
	
	
}
