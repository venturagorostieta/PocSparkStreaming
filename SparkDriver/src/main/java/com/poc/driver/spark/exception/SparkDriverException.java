package com.poc.driver.spark.exception;

public class SparkDriverException extends Exception {

	private static final long serialVersionUID = 1L;

	public SparkDriverException(String errorMessage) {
		super(errorMessage);
	}

	public SparkDriverException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}

}
