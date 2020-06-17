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
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Customers customer = mapper.readValue(jsonPayLoad, Customers.class);
		return customer;

	}
	
	public String cutJsonPayLoad(String originalJson) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonReduce = "";

		Map<String, Object> map = new HashMap<String, Object>();
		map = mapper.readValue(originalJson, new TypeReference<HashMap<String, Object>>() {
		});
		jsonReduce = map.get("payload").toString();

		return jsonReduce;
	}

	public  Customers rowToModel(String rowJson) throws JsonParseException, JsonMappingException, IOException {
		final String payload = getPayloadJson(rowJson);
		Customers cust = parserJsontoModel(payload);

		return cust;
	}

	public static void main(String args []) {
		JsonMapperUtil o =  new JsonMapperUtil();
		
		try {
			o.rowToModel(jsonTest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
