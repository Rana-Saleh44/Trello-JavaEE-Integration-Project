package service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.websocket.server.PathParam;
import javax.ws.rs.core.Response;

//import messagingSystem.Client;
import model.User;

@Stateless
public class UserService {
	@PersistenceContext( unitName = "hello")
	EntityManager entityManager;
	@Inject
	//Client messageClient;
	public Response regiserUser(User user) {
		User u = getUserByEmail(user.getEmail());
		if(u != null) {
			//messageClient.sendMessage("User registered: " + user.getName());
			return Response.status(Response.Status.CONFLICT).entity("A user with this email already exists!").build();
		}
		try {
			entityManager.persist(user);
			entityManager.flush();
			return Response.status(Response.Status.OK).entity(user).build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();		}
	}
	public Response loginUser(User loginUser) {
		try {
			User user = getUserByEmail(loginUser.getEmail());
			if(!user.getPassword().equals(loginUser.getPassword())) {
				return Response.status(Response.Status.NOT_FOUND).entity("USer not found!").build();
			}
			//messageClient.sendMessage("User logged in: " + user.getName());
			return Response.status(Response.Status.OK).entity("User logged in successfully").build();
		}catch(Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();		}
	}
	public Response updateProfile(User user) {
		try {
		User updatedUser = entityManager.find(User.class, user.getId());
		if(updatedUser == null) {
			return  Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
		}
		//messageClient.sendMessage("User updated: " + user.getName());
		entityManager.merge(user);
		return Response.status(Response.Status.OK).entity("Profile updated successfully.").build();
	}catch(Exception e) {
		e.printStackTrace();
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}}
    private User getUserByEmail(@PathParam("email") String email) {
    	try {
    		return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
    	}catch(Exception e){
    		return null;
    	}
    }
}
