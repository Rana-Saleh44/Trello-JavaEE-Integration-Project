package App;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ejbs.Messaging;
import ejbs.User;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

@Stateless
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserManagementService {
	
	@PersistenceContext(unitName = "hello")
	EntityManager entityManager;
	
	@POST
	@Path("/register")
	public Response registerUser(User user) {
		User u = getUserByEmail(user.getEmail());
		if (u != null) {     // email must be unique-> returns 409
			return Response.status(Response.Status.CONFLICT).entity("A user with this email already exists!").build();
		}
		try {
			entityManager.persist(user);
			entityManager.flush();
			return Response.status(Response.Status.OK).entity(user).build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		
	}
    
    @POST
    @Path("/login")
    public Response loginUser(User loginUser) {
    	try {
    		User user = getUserByEmail(loginUser.getEmail());
    		if(!user.getPassword().equals(loginUser.getPassword())){ // return 404;
    			return Response.status(Response.Status.NOT_FOUND).entity("User not found!").build();
    		}
    		return Response.status(Response.Status.OK).entity("User logged in successfully").build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
    }
    
  
    @PUT
    @Path("/update/{email}")
    public Response updateProfile(@PathParam("email") String email,@NotNull User updatedUser) {
    	try {
    		User user = getUserByEmail(email);
    		if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
            }
    		if(updatedUser.getEmail() != null) {
    			user.setEmail(updatedUser.getEmail());
    		}
    		if(updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
    			user.setName(updatedUser.getName());
    		}
    		if(user.getPassword() != null && !user.getPassword().isEmpty()) {
    			user.setPassword(updatedUser.getPassword());
    		}
    		entityManager.merge(user);
    		return Response.status(Response.Status.OK).entity("Profile updated successfully").build();
    	}catch(Exception e) {
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    	}
    }
    
    @GET
    @Path("/{userId}/notifications")
    /*public Response getUserNotifications(@PathParam("userId") Long userId) {
        // Retrieve notifications for the user
        List<Messaging> notifications = MessagingService.getDescription(userId);
        return Response.status(Response.Status.OK).entity(notifications).build();
    }*/
    public Response getUserNotifications(@PathParam("userId") Long userId) {
        // Assuming you have a method in MessagingService to get notifications by user ID
        //List<Messaging> notifications = MessagingService.getNotificationsByUserId(userId);
    	
        //return Response.status(Response.Status.OK).entity(notifications).build();
    	 try {
    	        // Retrieve notifications for the user
    	        List<Messaging> notifications = getNotificationsByUserId(userId);
    	        return Response.status(Response.Status.OK).entity(notifications).build();
    	    } catch (Exception e) {
    	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving notifications.").build();
    	    }
    }

    
    public List<Messaging> getNotificationsByUserId(Long userId) {
        // Retrieve notifications for the user
        //TypedQuery<Messaging> query = entityManager.createQuery("SELECT m FROM Messaging m WHERE m.recipient.id = :userId", Messaging.class)
                //.setParameter("userId", userId);
        //return query.getResultList();
    	 return entityManager.createQuery("SELECT m FROM Messaging m WHERE m.recipient.id = :userId", Messaging.class)
    	            .setParameter("userId", userId)
    	            .getResultList();
    }
    

	private User getUserByEmail(@PathParam("email") String email) {
    	try {
    		return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
    	}catch(Exception e) {
			return  null; 
		}
    }
}
