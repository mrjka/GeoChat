package resources;

import database.*;

import java.sql.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by paulosk on 05/04/16.
 *
 */

@Path("publish_location")
@Produces(MediaType.APPLICATION_JSON)
public class PublishLocationResource {

    public PublishLocationResource() {}

    @GET
    public boolean publishLocation(@QueryParam("latitude") String latitude,
                                   @QueryParam("longitude") String longitude,
                                   @QueryParam("userID") String dynID) {

        ResourceUtil.validateString("userID", dynID);
        ResourceUtil.validateDouble("latitude", latitude);
        ResourceUtil.validateDouble("longitude", longitude);

        database.AccessMananger dam = new database.AccessMananger();

        try {
            boolean res = dam.publishLocation(dynID, latitude, longitude);
            dam.close();
            return res;

        } catch (SQLException e) {
            System.err.println("SQLException @ PublishLocationResource: " + e.getMessage());
            dam.close();
            return false;
        }
    }
}
