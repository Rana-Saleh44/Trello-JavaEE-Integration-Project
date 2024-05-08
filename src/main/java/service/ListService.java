package service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import model.ListEntity;
import model.User;
import model.Card;

@Stateless
public class ListService {
	
	@PersistenceContext( unitName = "hello")
	EntityManager entityManager;
	@Inject
	
	public Response createList(@PathParam("userId") Long userId, ListEntity listEntity) {
        User u = entityManager.find(User.class, userId);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("user not found.").build();
        }
        if (u.getRole().equals("Collaborator")) {
        	return Response.status(Response.Status.FORBIDDEN).entity("Forbidden").build();
        }
        for (Card card : listEntity.getCards()) {
            card.setList(listEntity);  
        }
        try {
            entityManager.persist(listEntity);
            entityManager.flush();
            return Response.status(Response.Status.OK).entity(listEntity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
	
	public Response deleteList(@PathParam("listId") Long listId, @PathParam("userId") Long userId) {
        ListEntity listEntity = entityManager.find(ListEntity.class, listId);
        User u = entityManager.find(User.class, userId);
        if (listEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("List not found.").build();
        }
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("user not found.").build();
        }
        if (u.getRole().equals("Collaborator")) {
        	return Response.status(Response.Status.FORBIDDEN).entity("Forbidden").build();
        }
        try {
            entityManager.remove(listEntity);
            entityManager.flush();
            return Response.status(Response.Status.OK).entity("List deleted successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
