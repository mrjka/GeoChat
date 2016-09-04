package resources;

import database.*;

import java.sql.*;

import entities.User;

import java.util.List;
import java.util.LinkedList;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by paulosk on 06/04/16.
 */

@Path("zone-users")
@Produces(MediaType.APPLICATION_JSON)
public class ZoneUsersResource {

    public ZoneUsersResource() {}

    @GET
    public List<User> getUsers(@QueryParam("userID") String dynid) {
        ResourceUtil.validateString("userID", dynid);

        database.AccessMananger dam = new database.AccessMananger();
        try {
            List<User> zoneUsers = dam.getZoneUsers(dynid);
            dam.close();
            return zoneUsers;

        } catch(SQLException e) {
            System.err.println("SQLException @ ZoneUsersResource: " + e.getMessage());
            dam.close();
            return new LinkedList<User>();
        }
    }
}
