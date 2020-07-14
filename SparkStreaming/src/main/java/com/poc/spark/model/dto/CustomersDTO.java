package com.poc.spark.model.dto;

import java.io.Serializable;

public class CustomersDTO  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private long updatets;
	private String comments;
	private String email;
	private String firstname;
	private String gender;
	private String lastname;
	private String rfc;
	private int age;
	
	
	@Override
	public String toString() {
		return "CustomersDTO [id=" + id + ", updatets=" + updatets + ", comments=" + comments + ", email=" + email
				+ ", firstname=" + firstname + ", gender=" + gender + ", lastname=" + lastname + ", rfc=" + rfc
				+ ", age=" + age + "]";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getUpdatets() {
		return updatets;
	}
	public void setUpdatets(long updatets) {
		this.updatets = updatets;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	

}
