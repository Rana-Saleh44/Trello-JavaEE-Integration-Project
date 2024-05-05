package App;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Board;
import ejbs.ListEntity;

@Stateless
@RolesAllowed("TeamLeader")
@Path("/lists")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListManagementService {

    @PersistenceContext(unitName = "hello")
    EntityManager entityManager;

    @POST
    @Path("/create/{boardId}")
    public Response createList(@PathParam("boardId") Long boardId, ListEntity listEntity) {
        Board board = entityManager.find(Board.class, boardId);
        if (board == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Board not found.").build();
        }
        listEntity.setBoard(board);

        try {
            entityManager.persist(listEntity);
            return Response.status(Response.Status.OK).entity(listEntity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{listId}")
    public Response deleteList(@PathParam("listId") Long listId) {
        ListEntity listEntity = entityManager.find(ListEntity.class, listId);
        if (listEntity == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("List not found.").build();
        }

        try {
            entityManager.remove(listEntity);
            return Response.status(Response.Status.OK).entity("List deleted successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}

