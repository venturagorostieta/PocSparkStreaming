package com.poc.spark.domain.hdfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

import com.poc.spark.model.Customers;
import com.poc.spark.model.dto.CustomersDTO;
import com.poc.spark.util.SparkStreamingUtils;

@Service
public class CustomersSerDes implements Serializable {

	private static final Logger LOGGER = LogManager.getLogger(CustomersSerDes.class);
	private static final long serialVersionUID = 1L;
	private static final String FILE_NAME = "avro/customer.avsc";

	@Autowired
	private SparkStreamingUtils sparkStreamingUtils;

	public Schema getSChemaFile() {

		Schema schema = null;
		try {
			final URI uri = CustomersSerDes.class.getResource("/avro/customer.avsc").toURI();
			LOGGER.warn(" uri: " + uri.getPath());
			Map<String, String> env = new HashMap<>();
			env.put("create", "true");
			
			FileSystem zipfs = null;
			try {
				zipfs = FileSystems.newFileSystem(uri, Collections.emptyMap());
		    } catch (FileSystemAlreadyExistsException e) {
		    	zipfs = FileSystems.getFileSystem(uri);
		    }
			
			
			//FileSystem zipfs = FileSystems.newFileSystem(uri, env);
			LOGGER.warn(" zipfs: " + zipfs.getRootDirectories().iterator());
			Path myFolderPath = Paths.get(uri);
		

			final byte[] bytes = Files.readAllBytes(myFolderPath);
			String fileContent = new String(bytes);

			System.out.println(fileContent);

			schema = new Schema.Parser().parse(fileContent);

			LOGGER.warn("  File schema loaded: " + schema.getFullName());
			return schema;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return schema;
	}

	public static void main(String[] args) {

		try {

			final Path path = Paths.get(CustomersSerDes.class.getResource("/avro/customer.avsc").toURI());
			final byte[] bytes = Files.readAllBytes(path);
			String fileContent = new String(bytes);

			System.out.println(fileContent);
		} catch (IOException e) {
			System.out.println("error leer archivo");
			e.printStackTrace();

		} catch (URISyntaxException e) {
			System.out.println("error URISyntaxException");
			e.printStackTrace();
		}
	}

	public void saveRDD(JavaRDD<Customers> customers2, String operation) throws IOException {
		LOGGER.warn(" RecibienDO rdd Customers");
		if (!customers2.isEmpty()) {
			LOGGER.warn(" saveJavaRDD no es vacio ... ");
			String dir = sparkStreamingUtils.buildingPathW();
			String hdfsdIR = sparkStreamingUtils.buildingPathHDFS(dir);
			LOGGER.warn("hdfsdIR  Linux : " + hdfsdIR);

			String name = System.getProperty("os.name").toLowerCase();

			if (name.indexOf("win") >= 0) {
				LOGGER.warn("Windows : " + dir);
				sparkStreamingUtils.validateDirectory(dir);
				File avroOutput = new File(
						dir + operation + "customers_" + SparkStreamingUtils.getCurrentTime() + ".avro");
				LOGGER.warn("avroOutput: " + avroOutput.getPath());
				customers2.foreach(f -> saveRDD(f, avroOutput));

				// MOVER localsystem to hdfs
				// sparkStreamingUtils.mkdirHDFS(hdfsdIR);
				// sparkStreamingUtils.moveLocalSystem2HDFS(avroOutput.getPath(), hdfsdIR);

			} else {
				LOGGER.warn("Linux : " + hdfsdIR);
				sparkStreamingUtils.validateDirectory(hdfsdIR);
				File avroOutput = new File(
						hdfsdIR + operation + "customers_" + SparkStreamingUtils.getCurrentTime() + ".avro");
				LOGGER.warn("avroOutput: " + avroOutput.getPath());
				customers2.foreach(f -> saveRDD(f, avroOutput));

				LOGGER.warn("mover linux to HDFS: ");
				// MOVER localsystem to hdfs
				sparkStreamingUtils.mkdirHDFS(hdfsdIR);
				sparkStreamingUtils.moveLocalSystem2HDFS(avroOutput.getPath(), hdfsdIR);

			}
		}
	}

	private void saveRDD(Customers cust, File file) throws IOException {

		CustomersDTO custDTO = fillCustomerDTO(cust);
		Schema schemaLocal = getSChemaFile();
		try {

			GenericRecord e1 = new GenericData.Record(schemaLocal);
			e1 = fillGenericRecord(e1, custDTO);
			DatumWriter<GenericRecord> bdPersonDatumWriter = new GenericDatumWriter<>(schemaLocal);
			DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(bdPersonDatumWriter);

			if (Files.exists(Paths.get(file.getPath()))) {
				LOGGER.warn("Linux File exists");
				dataFileWriter.appendTo(file);
				dataFileWriter.append(e1);
			} else {
				dataFileWriter.create(schemaLocal, file);
				LOGGER.warn("Linux File create");
				dataFileWriter.append(e1);
			}

			dataFileWriter.close();
		} catch (IOException e) {
			LOGGER.error(" Error Writing AVRO" + e.getMessage());
		}
	}

	private static CustomersDTO fillCustomerDTO(Customers cust) {
		CustomersDTO cusDTO = new CustomersDTO();
		cusDTO.setId(cust.getId());
		cusDTO.setAge(cust.getAge());
		cusDTO.setComments(cust.getComments());
		cusDTO.setEmail(cust.getEmail());
		cusDTO.setFirstname(cust.getFirstname());
		cusDTO.setGender(cust.getGender());
		cusDTO.setLastname(cust.getLastname());
		cusDTO.setRfc(cust.getRfc());
		cusDTO.setUpdatets(dateToMiliseconds(cust.getUpdatets()));
		LOGGER.warn("fillCustomerDTO Retornando CustomersDTO");
		return cusDTO;
	}

	private static long dateToMiliseconds(Date date) {
		return date.getTime();
	}

	private static GenericRecord fillGenericRecord(GenericRecord generic, CustomersDTO cust) {

		generic.put("id", cust.getId());
		generic.put("firstname", cust.getFirstname());
		generic.put("lastname", cust.getLastname());
		generic.put("comments", cust.getComments());
		generic.put("email", cust.getEmail());
		generic.put("gender", cust.getGender());
		generic.put("rfc", cust.getRfc());
		generic.put("age", cust.getAge());
		generic.put("updatets", cust.getUpdatets());
		LOGGER.warn("fillGenericRecord Retornando GenericRecord");
		return generic;
	}

}
