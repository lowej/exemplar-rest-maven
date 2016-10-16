package com.exemplar.service;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes	;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;


/**
 * Dependencies
 * Jersey REST 
 * Jackson 1.9.0
 * Log4j 1.2.17
 * @author jonathanlowe
 *
 */

//Sets the path to base URL + /dogs
@Path("/dogs")
public class DogService {
	
  public static final HashMap<Integer,Dog> dogs = new HashMap<Integer,Dog>();
  static Logger log = Logger.getLogger(DogService.class.getName());
	
  public DogService(){
	  
	  //Seed some test dogs.  
	  //Constructor is called for every invocation of service, so only construct test data if empty
	  if( dogs.size() == 0){
		  dogs.put(new Integer(1234), new Dog(new Integer(1234), "Fido", "Begal"));
		  dogs.put(new Integer(5678), new Dog(new Integer(5678), "Floernce", "Spaniel"));
		  dogs.put(new Integer(9876), new Dog(new Integer(9876), "Rex", "Poodle"));
	  }
  }

  //Get all dogs
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDogs() {
    
	StringBuffer sbuf = new StringBuffer();
	//Start the json array for the dogs collection
	sbuf.append("[");
    Collection<Dog> dogCol = dogs.values();
    
    for(Dog dog: dogCol){
    	try {
			sbuf.append(dog.generateJson());
			sbuf.append(",");
		} catch (IOException e) {
			log.error("Error generating json from dog in GET /dogs");
			//Return 500 error
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"Error\":\"Error generating reponse\"}").build(); 
		}
    }
   
    //Drop in the array close at end of json string
    sbuf.replace(sbuf.length()-1, sbuf.length(), "]");
    
    //Return 200
    return Response.status(Response.Status.OK).entity(sbuf.toString()).build();
    //return Response.status(Response.Status.OK).entity("{\"test\":\"test\"}").build();
  }
  
  
  //Get a specific dog by its id
  @GET @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDogsById(@PathParam(value = "id") int id) {
	  
	Dog dog = dogs.get(new Integer(id));
	if(null == dog){
		//Return a 404 error
		return Response.status(Response.Status.NOT_FOUND).entity("{\"Error\":\"Dog not found\"}").build();
	}
	
	String json=null;
	try {
		json = dog.generateJson();
		log.debug("Dog is: " + json);
	} catch (IOException e) {
		log.error("Error generating json from dog in GET /dogs/{id}");
		//Return 500 error
		Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"Error\":\"Error generating reponse\"}").build(); 
	}
	
	//Return 200
    return Response.status(Response.Status.OK).entity(json).build();
  }

  
  //Delete a dog by its id
  @DELETE @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteDog(@PathParam(value = "id") int id) {
	  
	  Integer key = new Integer(id);
	  
	  if(!dogs.containsKey(key)){
		//Return a 404 error
		return Response.status(Response.Status.NOT_FOUND).entity("{\"Error\":\"Dog not found\"}").build(); 
	  }
	  dogs.remove(key);
	  
	//Return 200
    return Response.status(Response.Status.OK).build();
  }

  
  //Create a new dog
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createDog(String json) {

	  Dog dog = null;
	  String outJson = null;

	  log.debug("Just recieved a new dog to save: " + json);
	  
	  try {
		  dog = new Dog(json);
		  dogs.put(dog.getId(), dog);
		  outJson = dog.generateJson();
		} catch (IOException e) {
			log.error("Failed to create dog " + json +" in POST /dogs");
			//Return 409 Error
			return Response.status(Response.Status.CONFLICT).entity("{\"Error\":\"Invalid data supplied\"}").build(); 
		}
	  
	  //Return 201
	  return Response.status(Response.Status.CREATED).entity(outJson).build();
  }
  
  
  //Update an existing dog
  @PUT @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateDog(String json, @PathParam(value = "id") int id) {

	  Dog dog = null;
	  String outJson = null;
	  
	  if(null == dogs.get(id)){
		  log.debug("Dog " + id + " not found.");
		  //Return 404 as nothing to update
		  return Response.status(Response.Status.NOT_FOUND).entity("{\"Error\":\"Dog "+ id + " not found\"}").build();
	  }
	  
	  try {
		  dog = new Dog(json);
		  dogs.put(dog.getId(), dog);
		  outJson = dog.generateJson();
		} catch (IOException e) {
			log.error("Failed to update dog " + id +" in PUT /dogs/{id} invalid data supplied " + json);
			//Return 409
			return Response.status(Response.Status.CONFLICT).entity("{\"Error\":\"Invalid data supplied\"}").build(); 
		}
	  
	  //Return 200
	  return Response.status(Response.Status.OK).entity(outJson).build();
  }
  
  

} 