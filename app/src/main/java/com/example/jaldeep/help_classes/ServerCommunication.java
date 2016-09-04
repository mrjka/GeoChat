package com.example.jaldeep.help_classes;

import android.util.Log;

import com.example.jaldeep.entities.Login;
import com.example.jaldeep.entities.ReceiveMessage;
import com.example.jaldeep.entities.SendMessage;
import com.example.jaldeep.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created by Jaldeep on 06/04/16.
 */
public class ServerCommunication {
    //Ip-address and post to connect to server
    private final String ipAddress;
    private final int port;
    private final String defaultURL;

    public ServerCommunication()    {
        ipAddress = "130.229.155.161";
        port = 8080;
        defaultURL = "http://" + this.ipAddress + ":" + this.port + "/";
    }


    /**
     * Login using the username and password provided by user
     */
    public Login login(String username, String password) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(defaultURL + "login?username=" + username + "&password=" + password));
            StatusLine statusLine = httpResponse.getStatusLine();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            InputStream in = null;
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                // receive response as inputStream
                in = httpResponse.getEntity().getContent();
            } else {
                //Closes the connection.
                httpResponse.getEntity().getContent().close();
            }
            //Use the JSON tool to read input and create a Login instance
            ObjectMapper mapper = new ObjectMapper();
            Login login = mapper.readValue(in, Login.class);

            //Close all the connections
            in.close();

            //Return the login object
            return login;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Logs the user out from the server
     */
    public boolean logout(String userID) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(defaultURL + "logout?userID=" + userID));
            StatusLine statusLine = httpResponse.getStatusLine();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            InputStream in = null;
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                // receive response as inputStream
                in = httpResponse.getEntity().getContent();
            } else {
                //Closes the connection.
                httpResponse.getEntity().getContent().close();
            }

            //Use the JSON tool to read input and create a Login instance
            ObjectMapper mapper = new ObjectMapper();
            boolean response = mapper.readValue(in, boolean.class);

            //Close all the connections
            in.close();

            //Return the login object
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Send message from user1 to user2
     */
    public SendMessage sendMessage(String userID, String userReceiveName, String message) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(defaultURL + "send-message?userID=" + userID + "&recv=" + URLEncoder.encode(userReceiveName, "UTF-8") + "&message=" + URLEncoder.encode(message, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            InputStream in = null;
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                // receive response as inputStream
                in = httpResponse.getEntity().getContent();
            } else {
                //Closes the connection.
                httpResponse.getEntity().getContent().close();
            }

            //Use the JSON tool to read the boolean value from server
            ObjectMapper mapper = new ObjectMapper();
            SendMessage sendMessage = mapper.readValue(in, SendMessage.class);


            //Close all the connections
            in.close();

            //Return boolean
            return sendMessage;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Receive message from user
     * <p/>
     * TODO
     */
    public ReceiveMessage[] receiveMessage(String userID) {
        try {
            Log.e("Debug", "Trying to receive message at URL " + defaultURL + "receive-messages?userID=" + userID);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(defaultURL + "receive-messages?userID=" + userID));
            StatusLine statusLine = httpResponse.getStatusLine();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            InputStream in = null;
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                // receive response as inputStream
                in = httpResponse.getEntity().getContent();
            } else {
                //Closes the connection.
                httpResponse.getEntity().getContent().close();
            }

            //Use the JSON tool to read the boolean value from server
            ObjectMapper mapper = new ObjectMapper();
            ReceiveMessage[] response = mapper.readValue(in, ReceiveMessage[].class);

            //Close all the connections
            in.close();

            //Return boolean
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Send current location of user to server
     */
    public boolean publishLocation(String latitude, String longitude, String userID) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(defaultURL + "publish_location?latitude=" + latitude + "&longitude=" + longitude + "&userID=" + userID));
            StatusLine statusLine = httpResponse.getStatusLine();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            InputStream in = null;
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                // receive response as inputStream
                in = httpResponse.getEntity().getContent();
            } else {
                //Closes the connection.
                httpResponse.getEntity().getContent().close();
            }

            //Use the JSON tool to read the boolean value from server
            ObjectMapper mapper = new ObjectMapper();
            boolean response = mapper.readValue(in, boolean.class);

            //Close connections
            in.close();

            //Return boolean
            return response;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Register a new user
     */
    public boolean registerUser(String username, String password, String email, String name, String age, String description) {
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(defaultURL + "register?username=" + username + "&password=" + password + "&email=" + email + "&name=" + URLEncoder.encode(name, "UTF-8") + "&age=" + age + "&description=" + URLEncoder.encode(description, "UTF-8")));
            StatusLine statusLine = httpResponse.getStatusLine();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            InputStream in = null;
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                // receive response as inputStream
                in = httpResponse.getEntity().getContent();
            } else {
                //Closes the connection.
                httpResponse.getEntity().getContent().close();
            }


            //InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());

            //Use the JSON tool to read input and create a Login instance
            ObjectMapper mapper = new ObjectMapper();
            boolean response = mapper.readValue(in, boolean.class);


            //Close all the connections
            //urlConnection.disconnect();

            //Return the login object
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns a list with all the users within the userIDs zone
     * @param userID
     * @return
     */
    public User[] listAllUsers(String userID) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(defaultURL + "zone-users?userID=" + userID));
            StatusLine statusLine = httpResponse.getStatusLine();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            InputStream in = null;
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                // receive response as inputStream
                in = httpResponse.getEntity().getContent();
            } else {
                //Closes the connection.
                httpResponse.getEntity().getContent().close();
            }

            //Use the JSON tool to read input and create a Login instance
            ObjectMapper mapper = new ObjectMapper();
            User[] users = mapper.readValue(in, User[].class);


            return users;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
