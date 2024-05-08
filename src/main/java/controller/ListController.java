package controller;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.ListEntity;
import service.ListService;;

@RolesAllowed("TeamLeader")
@Path("/lists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListController {
	
	@EJB
	private ListService listService;
	
	@POST
    @Path("/create/{userId}")
	public Response createList(@PathParam("userId") Long userId, ListEntity listEntity) {
		
		return listService.createList(userId, listEntity);
	}
	
	@DELETE
    @Path("/{listId}/{userId}")
    public Response deleteList(@PathParam("listId") Long listId, @PathParam("userId") Long userId) {
		
		return listService.deleteList(listId, userId);
	}
}
