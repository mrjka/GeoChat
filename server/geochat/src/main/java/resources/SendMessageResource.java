package resources;

import database.*;
import entities.SendMessage;

import java.sql.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by paulosk on 06/04/16.
 */

@Path("send-message")
@Produces(MediaType.APPLICATION_JSON)
public class SendMessageResource {


    private Connection con;

    public SendMessageResource() {}


    @GET
    public SendMessage sendMessage(@QueryParam("userID") String dynid,
                                   @QueryParam("recv") String recvUser,
                                   @QueryParam("message") String message) {

        ResourceUtil.validateString("recv", recvUser);
        ResourceUtil.validateString("userID", dynid);
        ResourceUtil.validateString("message", message);

        database.AccessMananger dam = new database.AccessMananger();
        try {
            SendMessage sm = dam.sendMessage(dynid, recvUser, message);

            dam.close();
            return sm;

        } catch(SQLException e) {
            System.err.println("SQLException @ SendMessageResource: " + e.getMessage());
            dam.close();
            return new SendMessage("Server side error! Please try again later.");
        }
    }





}
