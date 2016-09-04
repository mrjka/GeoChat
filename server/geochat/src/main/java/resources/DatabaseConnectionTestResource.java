package resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.Result;

/**
 * Created by paulosk on 05/04/16.
 */

@Path("db_test")
@Produces(MediaType.APPLICATION_JSON)
public class DatabaseConnectionTestResource {

    private static class Response {
        private final String msg;

        public Response(String msg) {
            this.msg = msg;
        }

        @JsonProperty
        public String getMessage() {
            return msg;
        }
    }

    @GET
    public Response testConnection() {
        Connection con = DatabaseConnection.connect();
        Response response;

        try {
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT count(*) FROM users");

            StringBuilder respMessage = new StringBuilder();
            while (res.next()) {
                respMessage.append("Count: " + res.getInt(1));
            }

            response = new Response(respMessage.toString());
        } catch(SQLException e) {
            response = new Response(e.getMessage());
        }

        return response;

    }
}
