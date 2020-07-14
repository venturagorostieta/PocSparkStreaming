package com.poc.spark.dao;

import com.poc.spark.model.Customers;

public interface CustomerRepository {

	void saveCustomer(Customers cust);
	
}
