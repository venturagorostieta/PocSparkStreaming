package com.poc.spark.exception;

public class SparkStreamingException extends Exception {

	private static final long serialVersionUID = 1L;

	public SparkStreamingException(String errorMessage) {
		super(errorMessage);
	}

	public SparkStreamingException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}

}
