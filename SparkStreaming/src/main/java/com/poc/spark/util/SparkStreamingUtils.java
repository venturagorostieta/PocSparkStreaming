package com.poc.spark.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsShell;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.poc.spark.constant.Constants;

@Component
public class SparkStreamingUtils implements Serializable {
	
	private static final Logger LOGGER = LogManager.getLogger(SparkStreamingUtils.class);

	private static final long serialVersionUID = 1L;

	public static File getFileFromResources(String fileName) {

		ClassLoader classLoader = SparkStreamingUtils.class.getClassLoader();

		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}

	}

	public static String getCurrentTime() {
		final String FORMAT = "yyyy-MM-dd HH-mm-ss";
		Instant instant = Instant.now();
		long timeStampMillis = instant.toEpochMilli();
		ZoneId zone = ZoneId.systemDefault();
		DateTimeFormatter df = DateTimeFormatter.ofPattern(FORMAT).withZone(zone);
		return df.format(Instant.ofEpochMilli(timeStampMillis));		
	}

	public Month getMonth() {
		LocalDate currentdate = LocalDate.now();
		return currentdate.getMonth();
	}

	public int getDay() {
		LocalDate currentdate = LocalDate.now();
		return currentdate.getDayOfMonth();
	}

	public int getYear() {
		LocalDate currentdate = LocalDate.now();
		return currentdate.getYear();
	}

	public boolean validateDirectory(String dir) {
		boolean result = false;
		Path dirPathObj = Paths.get(dir);
		boolean dirExists = Files.exists(dirPathObj);
		if (dirExists) {
			result = true;			
			LOGGER.warn("! Directory Already Exists !");
		} else {
			try {
				Files.createDirectories(dirPathObj);
				LOGGER.warn("! New Directory Successfully Created !");
				result = true;
			} catch (IOException ioExceptionObj) {
				LOGGER.warn(
						"Problem Occured While Creating The Directory Structure= " + ioExceptionObj.getMessage());
				result = false;
			}
		}

		return result;
	}

	public String buildingPathW() {
		StringBuilder fullPath = new StringBuilder();
		String anio = String.valueOf(getYear());
		String currentMonth = getMonth().toString();
		String currentDay = String.valueOf(getDay());

		fullPath.append(Constants.BASE_DIR_OUTPUT_W);
		fullPath.append(anio);
		fullPath.append(Constants.WINDOWS_SEPARATOR);
		fullPath.append(currentMonth);
		fullPath.append(Constants.WINDOWS_SEPARATOR);
		fullPath.append(currentDay);
		fullPath.append(Constants.WINDOWS_SEPARATOR);
		return fullPath.toString();
	}

	public String buildingPathHDFS(String path) {
		String baseUrl = FilenameUtils.getPath(path);
		LOGGER.warn(baseUrl);
		LOGGER.warn(Constants.UNIX_SEPARATOR + FilenameUtils.separatorsToUnix(baseUrl));
		return Constants.UNIX_SEPARATOR + FilenameUtils.separatorsToUnix(baseUrl);
	}

	/**
	 * create directory in hdfs
	 * 
	 * @param dir
	 * @throws IOException
	 */
	public  void mkdirHDFS(String dir) throws IOException {
		LOGGER.warn("mkdirHDFS validating... " + "hdfs://sandbox-hdp.hortonworks.com:8020" + dir);
		LOGGER.warn("mkdirHDFS [method] ");
		Configuration conf = new Configuration();
		conf.addResource(new org.apache.hadoop.fs.Path("/etc/hadoop/conf/core-site.xml"));
		FileSystem fileSystem = FileSystem.get(conf);

		org.apache.hadoop.fs.Path path = new org.apache.hadoop.fs.Path("hdfs://sandbox-hdp.hortonworks.com:8020" + dir);
		if (fileSystem.exists(path)) {
			LOGGER.warn("Dir " + dir + "   exists  HDFS .. for MKDIR ");

		} else {
			LOGGER.warn("Dir   No existe  HDFS .. se  creara ");
			fileSystem.mkdirs(path);
			LOGGER.warn("hdfs path ::: " + "creado ....");

			
			FsShell shell = new FsShell(conf);
			LOGGER.warn("hdfs path ::: " + "agregando permisos ....");
			try {
				shell.run(new String[] { "-chmod", "-R", "777", dir });
			} catch (Exception e) {
				LOGGER.warn("Couldnt change the file permissions ");
				e.printStackTrace();
			}
		}
	}

	public  void moveLocalSystem2HDFS(String src, String hdfs) {
		LOGGER.warn("MoveLocalSystem 2 HDFSir " + "- src: " + src);
		LOGGER.warn("MoveLocalSystem 2 HDFSir " + "- hdfs: " + hdfs);
		Configuration conf = new Configuration();
		conf.addResource(new org.apache.hadoop.fs.Path("/etc/hadoop/conf/core-site.xml"));
		try {
			FileSystem fs = FileSystem.get(conf);
			fs.copyFromLocalFile(new org.apache.hadoop.fs.Path(src),
					new org.apache.hadoop.fs.Path("hdfs://sandbox-hdp.hortonworks.com:8020" + hdfs));
			LOGGER.warn("Avro movido a HDFS");
			
		} catch (IllegalArgumentException | IOException e) {
			LOGGER.warn("Ocurrio un error al mover a HDFS");
			e.printStackTrace();
		}finally {
		}
		
	}

}
