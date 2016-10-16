package com.exemplar.service;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Dog {
	
	private Integer id;
	private String name;
	private String breed;
	
	
	/**
	 * Create a new Dog passing in a JSON String representation
	 * @param jsonStr
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Dog(String jsonStr) throws IOException{

		ObjectMapper mapper = new ObjectMapper();
		Map m = mapper.readValue(jsonStr, Map.class);
		
		//Refactor this to use reflection
	    for(Object key: m.keySet())
	    {
	    	String field = key.toString();
	    	
	    	if("id".equals(field)){
	    		this.id = Integer.parseInt(m.get(key).toString());
	    		continue;
	    	}else if("name".equals(field)){
	    		this.name = m.get(key).toString();
	    		continue;
	    	}else if("breed".equals(field)){
	    		this.breed = m.get(key).toString();
	    		continue;
	    	}else{
	    		throw new IOException("ERROR, INVALID FIELD SPECIFIED");
	    	}
	   	}
	}
	
	/**
	 * Create a new dog passing in individual attributes
	 * @param id
	 * @param name
	 * @param breed
	 */
	public Dog(Integer id, String name, String breed){
		
		this.id = id;
		this.name = name;
		this.breed = breed;
	}
	
	/**
	 * Return a JSON representation of the object.   
	 * Do not name this method getXXX as it will cause a Jackson issue
	 * 
	 * @return String JSON representation of the Dog
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public String generateJson() throws IOException{//throws JsonGenerationException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

	    return mapper.writeValueAsString(this);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
}
