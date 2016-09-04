package resources;

import database.*;

import java.sql.*;
import entities.Register;

import java.util.List;
import java.util.LinkedList;
import java.util.Date;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
/**
 * Created by paulosk on 07/04/16.
 */

@Path("register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource {

    public RegisterResource() {}


    @GET
    public boolean register(@QueryParam("username") String username,
                            @QueryParam("name") String name,
                            @QueryParam("password") String pass,
                            @QueryParam("age") String age,
                            @QueryParam("email") String email,
                            @QueryParam("description") String description) {

        ResourceUtil.validateString("username", username);
        ResourceUtil.validateString("name", name);
        ResourceUtil.validateString("password", pass);
        ResourceUtil.validateInteger("age", age);
        ResourceUtil.validateString("username", username);

        description = ResourceUtil.emptyParam(description) ? "" : description;

        database.AccessMananger dam = new database.AccessMananger();
        try {

            boolean res = dam.registerUser(username, name, pass, age, email, description);
            dam.close();
            return res;

        } catch (SQLException e) {
            System.err.println("SQLException @ RegisterResource: " + e.getMessage());
            dam.close();
            return false;
        }
    }
}
