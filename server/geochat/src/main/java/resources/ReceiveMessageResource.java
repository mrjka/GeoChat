package resources;

import database.*;

import java.sql.*;
import entities.RecvMessage;

import java.util.List;
import java.util.LinkedList;
import java.util.Date;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by paulosk on 06/04/16.
 */

@Path("receive-messages")
@Produces(MediaType.APPLICATION_JSON)
public class ReceiveMessageResource {

    public ReceiveMessageResource() {

    }


    @GET
    public List<RecvMessage> getMessages(@QueryParam("userID") String dynid) {

        ResourceUtil.validateString("userID", dynid);

        database.AccessMananger dam = new database.AccessMananger();

        try {

            List<RecvMessage> msgs = dam.receiveMessages(dynid);
            dam.close();
            return msgs;

        } catch(SQLException e) {
            System.err.println("SQLException @ ReceiveMessageResource: " + e.getMessage());
            dam.close();
            return new LinkedList<RecvMessage>();
        }
    }
}
