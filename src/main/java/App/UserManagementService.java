package App;

import java.security.Principal;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

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
    @Path("/updateProfile")
    @RolesAllowed({"TeamLeader", "Collaborator"})
    public Response updateProfile(@Context SecurityContext securityContxt, User updatedUser) {
    	try {
    		Principal principal = securityContxt.getUserPrincipal();
    		String userEmail = principal.getName();
    		User existingUser = entityManager.createQuery("SELECT u FROM User u WHERE u.email", User.class).setParameter("email", userEmail).getSingleResult();
    		if(existingUser == null) {
    			return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
    		}
    		existingUser.setName(updatedUser.getName());
    		existingUser.setEmail(updatedUser.getEmail());
    		existingUser.setPassword(updatedUser.getPassword());
    		entityManager.merge(existingUser);
    		entityManager.flush();
    		return Response.status(Response.Status.OK).entity("Profile updated successfully").build();
    	}catch(Exception e) {
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    	}
    }
    
    private User getUserByEmail(@PathParam("email") String email) {
    	try {
    		return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
    	}catch(Exception e) {
			return  null; 
		}
    }
}
