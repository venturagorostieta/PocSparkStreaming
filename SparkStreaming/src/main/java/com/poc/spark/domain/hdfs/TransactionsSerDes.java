package com.poc.spark.domain.hdfs;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poc.spark.constant.Constants;
import com.poc.spark.model.Transactions;
import com.poc.spark.model.dto.TransactionsDTO;
import com.poc.spark.util.SparkStreamingUtils;

@Service
public class TransactionsSerDes  implements Serializable  {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(TransactionsSerDes.class);
	private static final String FILE_NAME ="/avro/Transactions.avsc";
	
	
	@Autowired
	private SparkStreamingUtils  sparkStreamingUtils;
	
	public Schema getSChemaFile() {

		Schema schema = null;
		try {
			final URI uri = CustomersSerDes.class.getResource(FILE_NAME).toURI();
			LOGGER.warn(" uri: " + uri.getPath());
			Map<String, String> env = new HashMap<>();
			env.put("create", "true");
			FileSystem zipfs = null;
			try {
				zipfs = FileSystems.newFileSystem(uri, Collections.emptyMap());
		    } catch (FileSystemAlreadyExistsException e) {
		    	zipfs = FileSystems.getFileSystem(uri);
		    }
			
			LOGGER.warn(" zipfs: " + zipfs.getRootDirectories().iterator());
			Path myFolderPath = Paths.get(uri);
			
			final byte[] bytes = Files.readAllBytes(myFolderPath);
			String fileContent = new String(bytes);

			schema = new Schema.Parser().parse(fileContent);

			LOGGER.warn("  File schema loaded: " + schema.getFullName());
			return schema;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		} 

		return schema;
	}

	
	public void saveRDD(JavaRDD<Transactions> transactions2, String operation) throws IOException {
		LOGGER.warn(" RecibienDO rdd");
		 if(!transactions2.isEmpty()) {
			 LOGGER.warn(" saveJavaRDD no es vacio ... ");
			 
			 String dir = sparkStreamingUtils.buildingPathW();
			 String hdfsdIR = sparkStreamingUtils.buildingPathHDFS(dir);
			 LOGGER.warn("hdfsdIR  Linux : " +hdfsdIR);
			 
			 String name = System.getProperty(Constants.OS_NAME).toLowerCase();
		      
		        if( name.indexOf(Constants.PREFIX_WIN) >= 0){
		        	 LOGGER.warn(" Windows ... ");
		        	 sparkStreamingUtils.validateDirectory(dir);
					 File avroOutput = new File(dir+operation+ Constants.PREFIX_TRANSACTION+ SparkStreamingUtils.getCurrentTime()+ Constants.EXT_AVRO);
					 
					 LOGGER.warn("avroOutput: "  + avroOutput.getPath());
					 transactions2.foreach(f -> 
							saveRDD(f, avroOutput)
						);					 				
		        }else {
					 sparkStreamingUtils.validateDirectory(hdfsdIR);
					 File avroOutput = new File(hdfsdIR+operation+Constants.PREFIX_TRANSACTION+ SparkStreamingUtils.getCurrentTime()+ Constants.EXT_AVRO);
					 
					 LOGGER.warn("avroOutput: "  + avroOutput.getPath());
					 transactions2.foreach(f -> 
							saveRDD(f, avroOutput)
						);
					 
					 //move localsystem to hdfs			
					 sparkStreamingUtils.mkdirHDFS(hdfsdIR);
					 sparkStreamingUtils.moveLocalSystem2HDFS(avroOutput.getPath(), hdfsdIR);		        	
		        }			
		 }			
	}	
	
	private  void saveRDD(Transactions txn, File file) throws IOException {
				
		TransactionsDTO txnDTO = fillCustomerDTO(txn);
		Schema	schemaLocal = getSChemaFile();
		try {

			GenericRecord e1 = new GenericData.Record(schemaLocal);
			e1 = fillGenericRecord(e1, txnDTO);
			DatumWriter<GenericRecord> bdPersonDatumWriter = new GenericDatumWriter<>(schemaLocal);
			DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(bdPersonDatumWriter);
			
			if(Files.exists(Paths.get(file.getPath()))) { 
				LOGGER.warn(" File Linux exists");
				dataFileWriter.appendTo(file);
				dataFileWriter.append(e1);
			}else {
				dataFileWriter.create(schemaLocal, file);
				LOGGER.warn("Linux  File create");
				dataFileWriter.append(e1);
			}									
			dataFileWriter.close();
		} catch (IOException e) {
			LOGGER.error(" Error Writing AVRO" +e.getMessage());
		}
	}

	private static TransactionsDTO fillCustomerDTO(Transactions txn) {
		TransactionsDTO txnDTO = new TransactionsDTO();
		txnDTO.setTxnid(txn.getTxnid());
		txnDTO.setTxnts(dateToMiliseconds(txn.getTxnts()));
		txnDTO.setCustomerid(txn.getCustomerid());
		txnDTO.setCompany(txn.getCompany());
		txnDTO.setCurrency(txn.getCurrency());
		txnDTO.setTotal(txn.getTotal());
		
		LOGGER.warn("fillCustomerDTO Retornando txnDTO");
		return txnDTO;
	}

	private static long dateToMiliseconds(Date date) {
		return date.getTime();
	}

	private static GenericRecord fillGenericRecord(GenericRecord generic, TransactionsDTO txnDTO) {
		
		generic.put(Constants.TRANSACTION_TXNID , txnDTO.getTxnid());
		generic.put(Constants.TRANSACTION_TXNTS, txnDTO.getTxnts());
		generic.put(Constants.TRANSACTION_CUSTOMERID, txnDTO.getCustomerid());
		generic.put(Constants.TRANSACTION_COMPANY, txnDTO.getCompany());
		generic.put(Constants.TRANSACTION_CURRENCY, txnDTO.getCurrency());
		generic.put(Constants.TRANSACTION_TOTAL, txnDTO.getTotal());
		
		LOGGER.warn("fillGenericRecord Retornando GenericRecord");
		return generic;
	}
	
}
