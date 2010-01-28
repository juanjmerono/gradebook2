package org.sakaiproject.gradebook.gwt.sakai.rest.resource;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.sakaiproject.gradebook.gwt.client.exceptions.InvalidInputException;

@Path("/gradebook/rest/item")
public class Item extends Resource {
	
	@POST @Path("{uid}/{id}")
	@Consumes({"application/xml", "application/json"})
	public String create(@PathParam("uid") String gradebookUid, @PathParam("id") Long gradebookId, 
			String model) throws InvalidInputException {
		
		Map<String,Object> map = fromJson(model, Map.class);
		Map<String,Object> result = service.createItem(gradebookUid, gradebookId, map);
		
		return toJson(result);
	}
	
	@GET @Path("{uid}/{id}")
	@Produces("application/json")
	public String getList(@PathParam("uid") String gradebookUid, @PathParam("id") Long gradebookId, 
			@QueryParam("type") String itemType) {
		return toJson(service.getItem(gradebookUid, gradebookId, itemType));
	}
	
	@DELETE
	@Consumes({"application/xml", "application/json"})
	public String remove(String model) throws InvalidInputException {
		Map<String,Object> map = fromJson(model, Map.class);
		Map<String,Object> result = service.updateItem(map);
		
		return toJson(result);
	}
	
	@PUT
	@Consumes({"application/xml", "application/json"})
	public String update(String model) throws InvalidInputException {
		Map<String,Object> map = fromJson(model, Map.class);
		Map<String,Object> result = service.updateItem(map);
		
		return toJson(result);
	}

}