package App;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ejbs.User;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;

@Stateless
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserManagementService {
	@EJB
	@PersistenceContext(unitName = "hello")
	EntityManager entityManager;
	
	@POST
	@Path("/register")
	public Response registerUser(User user) {
		try {
			entityManager.persist(user);
			int result = c.
			retu rn Response.status(Response.Status.OK).entity("{\"Result\": " + result + "}").build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
	}
    
    @POST
    @Path("/login")
    public Response loginUser(User user) {
    	try {
    		user = getUserByEmail(user.getEmail());
            
    		return Response.status(Response.Status.OK).entity("User logged in successfully").build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
    }
    
    @PUT
    @Path("/update/{email}")
    public Response updateProfile(@PathParam("email") String email, User user) {
    	try {
    		user = getUserByEmail(email);
    		if(user.getName() != null && !user.getName().isEmpty()) {
    			user.setName(user.getName());
    		}
    		if(user.getPassword() != null && !user.getPassword().isEmpty()) {
    			user.setName(user.getPassword());
    		}
    		entityManager.merge(user);
    		return Response.status(Response.Status.OK).entity("Profile updated successfully").build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
    }
    
    @GET
    @Path("/{email}")
    public User getUserByEmail(@PathParam("email") String email) {
    	try {
    		return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
    	}catch(Exception e) {
			return  null; 
		}
    }
    
//    @GET
//    @Path("/{email}")
//    public Response getUserByEmail(@PathParam("email") String email) {
//    	try {
//    		return Response.status(Response.Status.OK).entity(entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult()).build();
//    	}catch(Exception e) {
//			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//		}
//    }
   
    
}
