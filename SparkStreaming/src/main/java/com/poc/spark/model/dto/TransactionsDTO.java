package com.poc.spark.model.dto;

import java.io.Serializable;

public class TransactionsDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int txnid;
	private long txnts;
	private double total;
	private String company;
	private String currency;
	private int customerid;
	
	
	@Override
	public String toString() {
		return "TransactionsDTO [txnid=" + txnid + ", txnts=" + txnts + ", total=" + total + ", company=" + company
				+ ", currency=" + currency + ", customerid=" + customerid + "]";
	}
	
	public int getTxnid() {
		return txnid;
	}
	public void setTxnid(int txnid) {
		this.txnid = txnid;
	}
	public long getTxnts() {
		return txnts;
	}
	public void setTxnts(long txnts) {
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
	
	
}
