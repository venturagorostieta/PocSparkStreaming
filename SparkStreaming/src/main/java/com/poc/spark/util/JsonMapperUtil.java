package com.poc.spark.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.spark.model.Customers;
import com.poc.spark.model.Transactions;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonMapperUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	static String jsonTest  ="{   \"schema\":{      \"type\":\"struct\",\r\n" + 
			"      \"fields\":[         {\r\n" + 
			"            \"type\":\"int32\",\r\n" + 
			"            \"optional\":false,\r\n" + 
			"            \"field\":\"id\"\r\n" + 
			"         \r\n" + 
			"},\r\n" + 
			"         {\r\n" + 
			"            \"type\":\"string\",\r\n" + 
			"            \"optional\":true,\r\n" + 
			"            \"field\":\"first_name\"\r\n" + 
			"         \r\n" + 
			"},\r\n" + 
			"         {\r\n" + 
			"            \"type\":\"string\",\r\n" + 
			"            \"optional\":true,\r\n" + 
			"            \"field\":\"last_name\"\r\n" + 
			"         \r\n" + 
			"},\r\n" + 
			"         {\r\n" + 
			"            \"type\":\"string\",\r\n" + 
			"            \"optional\":true,\r\n" + 
			"            \"field\":\"email\"\r\n" + 
			"         \r\n" + 
			"},\r\n" + 
			"         {\r\n" + 
			"            \"type\":\"string\",\r\n" + 
			"            \"optional\":true,\r\n" + 
			"            \"field\":\"gender\"\r\n" + 
			"         \r\n" + 
			"},\r\n" + 
			"         {\r\n" + 
			"            \"type\":\"string\",\r\n" + 
			"            \"optional\":true,\r\n" + 
			"            \"field\":\"comments\"\r\n" + 
			"         \r\n" + 
			"},\r\n" + 
			"         {\r\n" + 
			"            \"type\":\"int64\",\r\n" + 
			"            \"optional\":false,\r\n" + 
			"            \"name\":\"org.apache.kafka.connect.data.Timestamp\",\r\n" + 
			"            \"version\":1,\r\n" + 
			"            \"field\":\"UPDATE_TS\"\r\n" + 
			"         \r\n" + 
			"}\r\n" + 
			"      \r\n" + 
			"],\r\n" + 
			"      \"optional\":false,\r\n" + 
			"      \"name\":\"customers\"\r\n" + 
			"   \r\n" + 
			"},\r\n" + 
			"   \"payload\":{\r\n" + 
			"      \"id\":6,\r\n" + 
			"      \"first_name\":\"44\",\r\n" + 
			"      \"last_name\":\"ggggg\",\r\n" + 
			"      \"email\":\"fgfgf@mail.com\",\r\n" + 
			"      \"gender\":\"FEMALE\",\r\n" + 
			"      \"comments\":\"GHGHG\",\r\n" + 
			"      \"UPDATE_TS\":1591723848000\r\n" + 
			"   \r\n" + 
			"}\r\n" + 
			"}\r\n" + 
			"";
	
	static String jsonTestCassandra="{\"schema\":{\"type\":\"struct\",\"fields\":[{\"type\":\"int32\",\"optional\":true,\"field\":\"id\"},{\"type\":\"int64\",\"optional\":true,\"name\":\"org.apache.kafka.connect.data.Timestamp\",\"version\":1,\"field\":\"updatets\"},{\"type\":\"string\",\"optional\":true,\"field\":\"comments\"},{\"type\":\"string\",\"optional\":true,\"field\":\"email\"},{\"type\":\"string\",\"optional\":true,\"field\":\"firstname\"},{\"type\":\"string\",\"optional\":true,\"field\":\"gender\"},{\"type\":\"string\",\"optional\":true,\"field\":\"lastname\"},{\"type\":\"string\",\"optional\":true,\"field\":\"rfc\"}],\"optional\":false,\"name\":\"demo.customers\"},\"payload\":{\"id\":3,\"updatets\":1592407272284,\"comments\":\"tercer dato\",\"email\":\"lalala@correo.com\",\"firstname\":\"Casandra\",\"gender\":\"FEMALE\",\"lastname\":\"Base\",\"rfc\":\"AIIA000600K11\"}}";
	
	public  String getPayloadJson(String originalJson) {
		
		String jsonReduce = "";
		try {
			
			JsonFactory factory = new JsonFactory();
			ObjectMapper mapper = new ObjectMapper(factory);
			JsonNode rootNode = mapper.readTree(originalJson);

			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
			while (fieldsIterator.hasNext()) {

				Map.Entry<String, JsonNode> field = fieldsIterator.next();
				if (field.getKey().equals("payload")) {

					jsonReduce = field.getValue().toString();
					return jsonReduce;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonReduce;
	}

	public  Customers parserJsontoModel(String jsonPayLoad)
			throws  IOException {

		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Customers customer = mapper.readValue(jsonPayLoad, Customers.class);
		return customer;

	}
	
	public  Transactions parserJsontoTransaction(String jsonPayLoad)
			throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Transactions txn = mapper.readValue(jsonPayLoad, Transactions.class);
		return txn;

	}
	
	public String cutJsonPayLoad(String originalJson) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonReduce = "";

		Map<String, Object> map = new HashMap<String, Object>();
		map = mapper.readValue(originalJson, new TypeReference<HashMap<String, Object>>() {
		});
		jsonReduce = map.get("payload").toString();

		return jsonReduce;
	}

	public  Customers rowToModel(String rowJson) throws IOException {
		final String payload = getPayloadJson(rowJson);
		return parserJsontoModel(payload);
	}
	
	public  Transactions rowToTransaction(String rowJson) throws IOException {
		final String payload = getPayloadJson(rowJson);
		return parserJsontoTransaction(payload);		
	}

	public static void main(String args []) {
		JsonMapperUtil o =  new JsonMapperUtil();
		
		try {
			o.rowToModel(jsonTestCassandra);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
