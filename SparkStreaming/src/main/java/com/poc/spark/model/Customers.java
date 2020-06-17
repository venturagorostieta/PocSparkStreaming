package com.poc.spark.model;

import java.io.Serializable;

public class Customers implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String first_name;
	private String last_name;
	private String email;
	private String gender;
	private String comments;
	private java.util.Date UPDATE_TS;

	public String getFirst_name(){
		return first_name;
	}

	public void setFirst_name(String first_name){
		this.first_name=first_name;
	}

	public String getLast_name(){
		return last_name;
	}

	public void setLast_name(String last_name){
		this.last_name=last_name;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getGender(){
		return gender;
	}

	public void setGender(String gender){
		this.gender=gender;
	}

	public String getComments(){
		return comments;
	}

	public void setComments(String comments){
		this.comments=comments;
	}

	public java.util.Date getUpdate_ts(){
		return UPDATE_TS;
	}

	public void setUpdate_ts(java.util.Date UPDATE_TS){
		this.UPDATE_TS=UPDATE_TS;
	}

	@Override
	public String toString() {
		return "Customers [first_name=" + first_name + ", last_name=" + last_name + ", email=" + email + ", gender="
				+ gender + ", comments=" + comments + ", UPDATE_TS=" + UPDATE_TS + "]";
	}		

}
