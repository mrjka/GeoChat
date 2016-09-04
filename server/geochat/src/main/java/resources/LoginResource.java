package resources;

import database.*;
import entities.Login;

import java.sql.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by paulosk on 05/04/16.
 */

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    public LoginResource() {}

    @GET
    public Login login(@QueryParam("username") String user,
                       @QueryParam("password") String pass) {

        ResourceUtil.validateString("username", user);
        ResourceUtil.validateString("password", pass);

        database.AccessMananger dam = new database.AccessMananger();
        try {

            Login login = dam.loginUser(user, pass);
            dam.close();

            return login;

        } catch(SQLException e) {
            System.err.println("SQLException @ LoginResource: " + e.getMessage());
            Login log = new Login();
            log.setErrorMessage("Server side error! Please try again later.");
            dam.close();
            return log;
        }
    }
}
