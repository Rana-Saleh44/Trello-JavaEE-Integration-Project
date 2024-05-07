package App;

import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.stream.events.Comment;

import ejbs.Card;
import ejbs.CardDetails;
import ejbs.ListEntity;
import ejbs.User;


@Stateless
@PermitAll
@Path("/cards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CardManagementService {
	@EJB 
	MessagingService messagingService;
	

    @PersistenceContext(unitName = "hello")
    EntityManager entityManager;
    
    public void updateDescription(Card card, String newDescription) {
        card.setDescription(newDescription);
        // Trigger notification event
        messagingService.sendNotification(card, card.getCollaborator(), card.getCommenters());
        entityManager.merge(card);
    }
    
    public void addComment(Card card, Set<ejbs.Comment> newComment) {
        card.setComment(newComment);
    	 //card.getComment().add(newComment);
        entityManager.persist(newComment);
        // Trigger notification event
        messagingService.sendNotification(card, card.getCollaborator(), card.getCommenters());
    }

    @POST
    @Path("/create/{userid}")
    public Response createCard(@PathParam("userid") Long userId, Card card) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        try {
            entityManager.persist(card);
            entityManager.flush();
            return Response.status(Response.Status.OK).entity(card).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{cardId}/move/{newListId}")
    public Response moveCard(@PathParam("cardId") Long cardId, @PathParam("newListId") Long newListId) {
        Card card = entityManager.find(Card.class, cardId);
        ListEntity newList = entityManager.find(ListEntity.class, newListId);
        if (card == null || newList == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card or list not found.").build();
        }
        card.setList(newList);
        try {
            entityManager.merge(card);
            entityManager.flush();
            return Response.status(Response.Status.OK).entity("Card moved successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{cardId}/assign/{userId}")
    public Response assignCard(@PathParam("cardId") Long cardId, @PathParam("userId") Long userId) {
        Card card = entityManager.find(Card.class, cardId);
        User newcollaborator = entityManager.find(User.class, userId);
        if (card == null || newcollaborator == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card or user not found.").build();
        }
        card.setCollaborator(newcollaborator);
        try {
            entityManager.merge(card);
            entityManager.flush();
            return Response.status(Response.Status.OK).entity("Card assigned successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{cardId}/desandcom")
    /*public Response addDescriptionandComments(@PathParam("cardId") Long cardId, CardDetails cd) {
        Card card = entityManager.find(Card.class, cardId);
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card not found.").build();
        }
        card.setDescription(cd.getDescription());
        card.setComment((Set<ejbs.Comment>) cd.getComment());
        try {
            entityManager.merge(card);
            entityManager.flush();
            return Response.status(Response.Status.OK).entity("Description added successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }*/
    public Response addDescriptionandComments(Long cardId, CardDetails cd) {
        Card card = entityManager.find(Card.class, cardId);
        if (card == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Card not found.").build();
        }
        card.setDescription(cd.getDescription());
        Set<ejbs.Comment> comments = cd.getComments(); // Assuming you have a getComments() method in CardDetails
        card.setComment(comments);
        try {
            entityManager.merge(card);
            entityManager.flush();
            return Response.status(Response.Status.OK).entity("Description and comments added successfully.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }


    
}