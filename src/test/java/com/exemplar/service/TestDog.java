package com.exemplar.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestDog {

	@Test
	public void testDog() throws Exception{
	
		Integer id = new Integer(1234);
		String name = "fido";
		String breed = "begal";
		
		String json = "{\"id\":"+id+", \"breed\":\""+breed+"\", \"name\":\""+name+"\"}";
		
		Dog dog = new Dog(json);
		
		assertEquals(id, dog.getId());
		assertEquals(name, dog.getName());
		assertEquals(breed, dog.getBreed());
	}
	
	@Test
	public void testToJson() throws Exception{
	
		Integer id = new Integer(1234);
		String name = "fido";
		String breed = "begal";
		
		Dog dog = new Dog(id, name, breed);
		
		String json = "{\"id\":"+id+",\"name\":\""+name+"\",\"breed\":\""+breed+"\"}";
		
		
		
		assertEquals(json, dog.generateJson());
	}
	

}
