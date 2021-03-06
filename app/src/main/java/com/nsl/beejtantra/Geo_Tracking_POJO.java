package com.nsl.beejtantra;

import static com.google.android.gms.internal.zzbfq.NULL;

public class Geo_Tracking_POJO {



	public String getPolyline() {
		return polyline;
	}

	public void setPolyline(String polyline) {
		this.polyline = polyline;
	}

	String polyline;
	//private variables
	int    _geo_id;
	String _geo_masterid;
	String _geo_visit_type;
	String _geo_user_id;
	String _geo_check_in_lat_lon;
	String _geo_check_out_lat_lon;
	String _geo_route_path_lat_lon;
	String _geo_distance;
	String _geo_visit_date;
	String _geo_check_in_time;
	String _geo_check_out_time;
	String _geo_status;
	String _geo_ffmid;
	String _geo_cdatetime;
	String _geo_udatetime;
	String meter_reading_checkin_image;
	String meter_reading_checkin_text=null;
	String meter_reading_checkout_image=null;
	String meter_reading_checkout_text=null;
	String vehicle_type=null;
	String personal_uses_km=null;
	String checkin_comment=null;
	String pause;
	String resume;

	// Empty constructor
	public Geo_Tracking_POJO(){

	}

	// constructor
	public Geo_Tracking_POJO(String geo_master_id,
							 String geo_visit_type,
							 String geo_user_id,
							 String geo_check_in_lat_lon,
							 String geo_check_out_lat_lon,
							 String geo_route_path_lat_lon,
							 String geo_distance,
							 String geo_visit_date,
							 String geo_check_in_time,
							 String geo_check_out_time,
							 String geo_status,
							 String geo_ffmid,
							 String geo_cdatetime,
							 String geo_udatetime,
							 String polyline,
							 String meter_reading_checkin_image,
							 String meter_reading_checkin_text,
							 String meter_reading_checkout_image,
							 String meter_reading_checkout_text,
							 String vehicle_type,
							 String personal_uses_km, String checkin_comment,
							 String pause,
							 String resume){

		this._geo_masterid = geo_master_id;
		this._geo_visit_type = geo_visit_type;
		this._geo_user_id = geo_user_id;
		this._geo_check_in_lat_lon = geo_check_in_lat_lon;
		this._geo_check_out_lat_lon = geo_check_out_lat_lon;
		this._geo_route_path_lat_lon = geo_route_path_lat_lon;
		this._geo_distance = geo_distance;
		this._geo_visit_date = geo_visit_date;
		this._geo_check_in_time = geo_check_in_time;
		this._geo_check_out_time = geo_check_out_time;
		this._geo_status = geo_status;
		this._geo_ffmid = geo_ffmid;
		this._geo_cdatetime = geo_cdatetime;
		this._geo_udatetime = geo_udatetime;
		this.polyline=polyline;
		this.meter_reading_checkin_image=meter_reading_checkin_image;
		this.meter_reading_checkin_text=meter_reading_checkin_text;
		this.meter_reading_checkout_image=meter_reading_checkout_image;
		this.meter_reading_checkout_text=meter_reading_checkout_text;
		this.vehicle_type=vehicle_type;
		this.personal_uses_km=personal_uses_km;
		this.checkin_comment=checkin_comment;
		this.pause=pause;
		this.resume=resume;

	}


	// getting ID
	public int getID() {
		return this._geo_id;
	}

	// setting id
	public void setID(int id) {
		this._geo_id = id;
	}

	public String getGeoMasterId() {
		return this._geo_masterid;
	}

	// setting name
	public void setGeoMasterID(String geo_masterid) {
		this._geo_masterid = geo_masterid;
	}


	// getting name
	public String getGeoVisitType() {
		return this._geo_visit_type;
	}

	// setting name
	public void setGeoVisitType(String geo_visit_type) {
		this._geo_visit_type = geo_visit_type;
	}


	public String get_Geo_user_id() {
		return this._geo_user_id;
	}

	// setting name
	public void set_Geo_user_id(String geo_user_id) {
		this._geo_user_id = geo_user_id;
	}


	public String get_Geo_check_in_lat_lon() {

		return this._geo_check_in_lat_lon;
	}

	public void set_Geo_check_in_lat_lon(String geo_check_in_lat_lon) {
		this._geo_check_in_lat_lon = geo_check_in_lat_lon;
	}

	// getting phone number
	public String getGeo_check_out_lat_lon() {

		return this._geo_check_out_lat_lon;
	}

	// setting phone number
	public void setGeo_check_out_lat_lon(String geo_check_out_lat_lon) {

		this._geo_check_out_lat_lon = geo_check_out_lat_lon;
	}


	public String getGeo_route_path_lat_lon() {

		return this._geo_route_path_lat_lon;
	}

	// setting region_Id
	public void setGeo_route_path_lat_lon(String geo_route_path_lat_lon) {

		this._geo_route_path_lat_lon = geo_route_path_lat_lon;
	}

	public String getGeo_distance() {

		return this._geo_distance;
	}

	// setting street
	public void setGeo_distance(String geo_distance) {

		this._geo_distance = geo_distance;
	}

	public String getGeo_visit_date() {

		return this._geo_visit_date;
	}

	// setting phone number
	public void setGeo_visit_date(String geo_visit_date) {

		this._geo_visit_date = geo_visit_date;
	}

	public String getGeo_check_in_time() {

		return this._geo_check_in_time;
	}

	// setting address
	public void setGeo_check_in_time(String geo_check_in_time) {

		this._geo_check_in_time = geo_check_in_time;
	}


	public String getGeo_check_out_time() {

		return this._geo_check_out_time;
	}

	// setting address
	public void setGeo_check_out_time(String geo_check_out_time) {

		this._geo_check_out_time = geo_check_out_time;
	}


	// getting status
	public String getGeostatus() {

		return this._geo_status;
	}

	// setting status
	public void setGeostatus(String geo_status) {

		this._geo_status = geo_status;
	}
	// getting status
	public String getGeoffmid() {

		return this._geo_ffmid;
	}

	// setting status
	public void setGeoffmid(String geo_ffmid) {

		this._geo_ffmid = geo_ffmid;
	}

	public String getGeocdatetime() {

		return this._geo_cdatetime;
	}

	// setting phone number
	public void setGeocdatetime(String geo_cdatetime) {

		this._geo_cdatetime = geo_cdatetime;
	}

	public String getGeoudatetime() {

		return this._geo_udatetime;
	}

	// setting phone number
	public void setGeoudatetime(String geo_udatetime) {

		this._geo_udatetime = geo_udatetime;
	}
	public String getMeter_reading_checkin_image() {
		return meter_reading_checkin_image;
	}

	public void setMeter_reading_checkin_image(String meter_reading_checkin_image) {
		this.meter_reading_checkin_image = meter_reading_checkin_image;
	}

	public String getMeter_reading_checkin_text() {
		return meter_reading_checkin_text;
	}

	public void setMeter_reading_checkin_text(String meter_reading_checkin_text) {
		this.meter_reading_checkin_text = meter_reading_checkin_text;
	}

	public String getMeter_reading_checkout_image() {
		return meter_reading_checkout_image;
	}

	public void setMeter_reading_checkout_image(String meter_reading_checkout_image) {
		this.meter_reading_checkout_image = meter_reading_checkout_image;
	}

	public String getMeter_reading_checkout_text() {
		return meter_reading_checkout_text;
	}

	public void setMeter_reading_checkout_text(String meter_reading_checkout_text) {
		this.meter_reading_checkout_text = meter_reading_checkout_text;
	}

	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	public String getPersonal_uses_km() {
		return personal_uses_km;
	}

	public void setPersonal_uses_km(String personal_uses_km) {
		this.personal_uses_km = personal_uses_km;
	}

	public String getCheckin_comment() {
		return checkin_comment;
	}

	public void setCheckin_comment(String checkin_comment) {
		this.checkin_comment = checkin_comment;
	}
	public String getPause() {
		return pause;
	}

	public void setPause(String pause) {
		this.pause = pause;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

}
