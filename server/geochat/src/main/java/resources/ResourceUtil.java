package resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

/**
 * Created by paulosk on 06/04/16.
 */
public class ResourceUtil {

    protected static boolean emptyParam(String param) {
        return param == null || param.equals("");
    }

    protected static void validateString(String paramName, String value)
            throws WebApplicationException {
        
        if (emptyParam(value)) {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity(paramName + " parameter is mandatory")
                            .build()
            );
        }
    }

    protected static void validateDouble(String paramName, String value) {
        validateString(paramName, value);

        boolean validDouble = false;

        try {
            Double.parseDouble(value);
            validDouble = true;
        } catch (NumberFormatException e) {
            validDouble = false;
        }

        if ( ! validDouble) {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity(paramName + " must be a double value.")
                            .build()
            );
        }
    }

    protected static void validateInteger(String paramName, String value) {
        validateString(paramName, value);

        boolean validInt = false;

        try {
            Integer.parseInt(value);
            validInt = true;
        } catch (NumberFormatException e) {
            validInt = false;
        }

        if ( ! validInt) {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity(paramName + " must be a integer value.")
                            .build()
            );
        }

    }
}
