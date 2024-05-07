package App;
import messagingSystem.Client;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
	Client messageClient;
	@POST
	@Path("/register")
	public Response registerUser(User user) {
		User u = getUserByEmail(user.getEmail());
		if (u != null) {     // email must be unique-> returns 409
			messageClient.sendMessage("User registered: " + user.getName());
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
    		messageClient.sendMessage("User logged in: " + user.getName());
    		return Response.status(Response.Status.OK).entity("User logged in successfully").build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
    }
    
  
    @PUT
    @Path("/update")
    public Response updateProfile(User user) {
    	try {
    		User updatedUser = entityManager.find(User.class, user.getId());
    		if (updatedUser == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
            }
    		messageClient.sendMessage("User updated: " + user.getName());
    		entityManager.merge(user);
    		return Response.status(Response.Status.OK).entity("Profile updated successfully").build();
    	}catch(Exception e) {
    		e.printStackTrace();
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
