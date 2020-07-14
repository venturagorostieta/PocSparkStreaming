package com.poc.spark.domain.hdfs;

import java.io.IOException;
import java.io.Serializable;

import org.apache.avro.Schema;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.springframework.stereotype.Service;

import scala.Tuple2;

@Service
public class SparkAvroSaver implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(SparkAvroSaver.class);

	
	 public <T> void saveJavaRDD(JavaRDD<T> javaRDD, Schema avroSchema, String path) {
	        if(!javaRDD.isEmpty()) {	        	
	        	LOGGER.warn("RDD NO es vacio guardar RDD full");
		        JavaPairRDD<AvroKey<T>, NullWritable> javaPairRDD = javaRDD.mapToPair(r->new Tuple2<AvroKey<T>, NullWritable>(new AvroKey<T>(r), NullWritable.get()));	        
		        saveJavaPairRDDKeys(javaPairRDD, avroSchema, path);	  
	        }
	    }
	   
	    public <K, V> void saveJavaPairRDDKeys(JavaPairRDD<K, V> javaPairRDD, Schema avroSchema, String path) {
	        
	        Job job = getJob(avroSchema);
	        LOGGER.warn("Persistir avro");
	        javaPairRDD.saveAsNewAPIHadoopFile(path, AvroKey.class, NullWritable.class, AvroKeyOutputFormat.class, job.getConfiguration());
	    }
	    
	    private Job getJob(Schema avroSchema) {
	        
	        Job job;	      
	        try {
	            job = Job.getInstance();
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }	        
	        AvroJob.setOutputKeySchema(job, avroSchema);
	        return job;
	    }
}
