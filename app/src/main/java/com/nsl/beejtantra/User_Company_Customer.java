package com.nsl.beejtantra;

public class User_Company_Customer {

	//private variables
	int    _ucc_id;
	String _ucc_user_id;
	String _ucc_company_id;
	String _ucc_customer_id;
	String status;
	String map_start_date;
	String unmap_date;

	// Empty constructor
	public User_Company_Customer(){

	}
	// constructor
	public User_Company_Customer(int id, String ucc_user_id, String ucc_company_id, String ucc_customer_id,String status,String map_start_date,String unmap_date){
		this._ucc_id            = id;
		this._ucc_user_id       = ucc_user_id;
		this._ucc_company_id   = ucc_company_id;
		this._ucc_customer_id       = ucc_customer_id;
		this.status=status;
		this.map_start_date=map_start_date;
		this.unmap_date=unmap_date;


	}

	// constructor
	public User_Company_Customer(String ucc_user_id, String ucc_company_id, String ucc_customer_id){
		this._ucc_user_id    = ucc_user_id;
		this._ucc_company_id       = ucc_company_id;
		this._ucc_customer_id   = ucc_customer_id;

	}



	// getting ID
	public int getID(){
		return this._ucc_id;
	}
	
	// setting id
	public void setID(int id){
		this._ucc_id = id;
	}

	public String getuccUserId(){
		return this._ucc_user_id;
	}

	// setting name
	public void setuccUserId(String ucc_user_id){
		this._ucc_user_id = ucc_user_id;
	}


	public String getuccCustomerId(){
		return this._ucc_customer_id;
	}

	// setting name
	public void setuccCustomerId(String ucc_customer_id){
		this._ucc_customer_id = ucc_customer_id;
	}
	public String getuccCompanyId(){
		return this._ucc_company_id;
	}

	// setting name
	public void setuccCompanyId(String ucc_company_id){
		this._ucc_company_id = ucc_company_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMap_start_date() {
		return map_start_date;
	}

	public void setMap_start_date(String map_start_date) {
		this.map_start_date = map_start_date;
	}

	public String getUnmap_date() {
		return unmap_date;
	}

	public void setUnmap_date(String unmap_date) {
		this.unmap_date = unmap_date;
	}
}
