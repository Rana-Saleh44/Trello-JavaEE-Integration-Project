package service;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Board;
import model.User;

@Stateless
@RolesAllowed("TeamLeader")
public class BoardService {

	@PersistenceContext(unitName = "hello")
	EntityManager entityManager;
	
	public Response createBoard(Long userId, String name) {

		try {
			User u = entityManager.find(User.class, userId);
			if (u == null) {
				return Response.status(Response.Status.NOT_FOUND).entity("user not found.").build();
			}
			if (u.getRole().equals("Collaborator")) {
				return Response.status(Response.Status.FORBIDDEN).entity("Forbidden").build();
			}
			Board board = new Board();
			board.setName(name);
			entityManager.persist(board);
			entityManager.flush();
			return Response.status(Response.Status.OK).entity(board).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	
	public List<Board> getAllBoardsByTeamLeader( Long teamLeaderId) {
		return entityManager.createQuery("SELECT b From Board b WHERE b.teamLeader.id = :teamLeaderId", Board.class)
				.setParameter("teamLeaderId", teamLeaderId).getResultList();
	}

	public Response inviteCollaborator(Long boardId,int userId) {
		Board board = entityManager.find(Board.class, boardId);
		User collaborator = entityManager.find(User.class, userId);
		if (board == null || collaborator == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Board or user not found.").build();
		}
		if (collaborator.getRole().equals("TeamLeader")) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Team leader cannot be added as a collaborator.")
					.build();
		}
		board.getCollaborators().add(collaborator);
		entityManager.merge(board);
		entityManager.flush();
		return Response.status(Response.Status.OK).entity("Collaborator invited successfully.").build();
	}

	
	public Response deleteBoard(Long boardId,  int userId) {
		Board board = entityManager.find(Board.class, boardId);
		User u = entityManager.find(User.class, userId);
		if (u == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("user not found.").build();
		}
		if (u.getRole().equals("Collaborator")) {
			return Response.status(Response.Status.FORBIDDEN).entity("Forbidden").build();
		}
		if (board == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Board not found.").build();
		}
		try {
			entityManager.remove(board);
			entityManager.flush();
			return Response.status(Response.Status.OK).entity("Board deleted successfully.").build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

}
