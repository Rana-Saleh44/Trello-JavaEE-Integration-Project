package App;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Board;
import ejbs.User;

@Stateless
@RolesAllowed("TeamLeader")
@Path("/boards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardManagementService {

	@PersistenceContext(unitName = "hello")
	EntityManager entityManager;
	
	@POST
	@Path("/create")
	public Response createBoard(Board board) {
		/*User teamLeader = entityManager.find(User.class, board.getTeamLeader().getId());
		board.setTeamLeader(teamLeader);
		board.getCollaborators().add(teamLeader);*/
		entityManager.persist(board);
		try {
			entityManager.persist(board);
			return Response.status(Response.Status.OK).entity(board).build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	public List<Board> getAllBoardsByTeamLeader(@QueryParam("teamLeaderId")Long teamLeaderId){
		return entityManager.createQuery("SELECT b From Board b WHERE b.teamLeader.id = :teamLeaderId", Board.class).setParameter("teamLeaderId", teamLeaderId).getResultList();
	}
	
	@PUT
	@Path("/{boardId}/invite/{userId}")
	public Response inviteCollaborator(@PathParam("boardId")Long boardId, @PathParam("userId")Long userId) {
		Board board = entityManager.find(Board.class, boardId);
		User collaborator = entityManager.find(User.class, userId);
		if(board == null || collaborator == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Board or user not found.").build();
		}
		if(board.getTeamLeader().getId().equals(userId)) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Team leader cannot be added as a collaborator.").build();
		}
		board.getCollaborators().add(collaborator);
		entityManager.merge(board);
		return Response.status(Response.Status.OK).entity("Collaborator invited successfully.").build();
	}
	
	@DELETE
    @Path("/{boardId}")
    public Response deleteBoard(@PathParam("boardId") Long boardId) {
        Board board = entityManager.find(Board.class, boardId);
        if (board == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Board not found.").build();
        }
        try {
            entityManager.remove(board);
            return Response.status(Response.Status.OK).entity("Board deleted successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
	
}
