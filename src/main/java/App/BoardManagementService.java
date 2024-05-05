package App;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Board;
import ejbs.User;

@Stateless
@Path("/boards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoardManagementService {

	@PersistenceContext(unitName = "hello")
	EntityManager entityManager;
	
	@POST
	public Response createBoard(Board board) {
		User teamLeader = entityManager.find(User.class, board.getTeamLeader().getId());
		board.setTeamLeader(teamLeader);
		board.getCollaborators().add(teamLeader);
		try {
			entityManager.persist(board);
			return Response.status(Response.Status.OK).entity(board).build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	
	
}
