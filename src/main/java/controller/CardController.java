package controller;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Card;
import model.CardDetails;
import service.CardService;

@PermitAll
@Path("/cards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CardController {
	
	@EJB
	private CardService cardService;
	
	@POST
    @Path("/create/{userid}")
    public Response createCard(@PathParam("userid") Long userId, Card card) {
		
		return cardService.createCard(userId, card);
	}
	
	@PUT
    @Path("/{cardId}/move/{newListId}")
    public Response moveCard(@PathParam("cardId") Long cardId, @PathParam("newListId") Long newListId) {
		
		return cardService.moveCard(cardId, newListId);
	}
	
	@PUT
    @Path("/{cardId}/assign/{userId}")
    public Response assignCard(@PathParam("cardId") Long cardId, @PathParam("userId") Long userId) {
		return cardService.assignCard(cardId, userId);
	}
	
	@PUT
    @Path("/{cardId}/desandcom")
    public Response addDescriptionandComments(@PathParam("cardId") Long cardId, CardDetails cd) {
		return cardService.addDescriptionandComments(cardId, cd);
	}
}
