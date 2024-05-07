package App;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ejbs.ListEntity;
import ejbs.User;
import ejbs.Card;

@Stateless
@RolesAllowed("TeamLeader")
@Path("/lists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListManagementService {

    @PersistenceContext(unitName = "hello")
    EntityManager entityManager;

    @POST
    @Path("/create/{userId}")
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

    @DELETE
    @Path("/{listId}/{userId}")
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

