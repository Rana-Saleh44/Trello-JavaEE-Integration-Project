package controller;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.User;
import service.UserService;
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class UserController {
	@EJB
	private UserService userService;
	
	@POST
	@Path("/register")
	public Response registerUser(User user) {
		return userService.regiserUser(user);
	}
	@POST
	@Path("/login")
	public Response loginUser(User loginUser) {
		return userService.loginUser(loginUser);
	}
	@PUT
	@Path("/update")
	public Response updateProfile(User user) {
		return userService.updateProfile(user);
	}
	

}
