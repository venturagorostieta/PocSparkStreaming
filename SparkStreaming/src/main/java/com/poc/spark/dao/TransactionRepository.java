package com.poc.spark.dao;

import com.poc.spark.model.Transactions;

public interface TransactionRepository {

	void saveTransactions(Transactions txn);
}
