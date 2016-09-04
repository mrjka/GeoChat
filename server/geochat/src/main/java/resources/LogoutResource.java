package resources;

import java.sql.*;

import database.DatabaseConnection;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
/**
 * Created by paulosk on 08/04/16.
 */

@Path("logout")
@Produces(MediaType.APPLICATION_JSON)
public class LogoutResource {

    public LogoutResource() {}

    @GET
    public boolean logout(@QueryParam("userID") String dynID) {
        ResourceUtil.validateString("userID", dynID);
        database.AccessMananger dam = new database.AccessMananger();

        try {
            if ( ! dam.logoutUser(dynID)) {
                System.err.println("SQL Error @ LogoutResource: " +
                        "Dynamic ID '" + dynID + "' was not found in database.");

                dam.close();
                return false;
            }

            dam.close();
            return true;

        } catch (SQLException e) {
            System.err.println("SQLException @ LogoutResource: " + e.getMessage());
            dam.close();
            return false;
        }
    }
}
