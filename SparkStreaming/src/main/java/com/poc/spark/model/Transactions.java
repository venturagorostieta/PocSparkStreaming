package com.poc.spark.model;

import java.io.Serializable;
import java.util.Date;

public class Transactions  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int txnid;
	private Date txnts;
	private double total;
	private String company;
	private String currency;
	private int customerid;
	
	public int getTxnid() {
		return txnid;
	}
	public void setTxnid(int txnid) {
		this.txnid = txnid;
	}
	public Date getTxnts() {
		return txnts;
	}
	public void setTxnts(Date txnts) {
		this.txnts = txnts;
	}

	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public int getCustomerid() {
		return customerid;
	}
	public void setCustomerid(int customerid) {
		this.customerid = customerid;
	}
	@Override
	public String toString() {
		return "Transactions [txnid=" + txnid + ", txnts=" + txnts + ", total=" + total + ", company=" + company
				+ ", currency=" + currency + ", customerid=" + customerid + "]";
	}

}
