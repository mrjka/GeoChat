package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by paulosk on 07/04/16.
 */
public class Register {

    private final ResponseType respType;
    private final String errorMessage;

    public Register(String errMsg) {
        respType = ResponseType.ERROR;
        errorMessage = errMsg;
    }

    public Register() {
        respType = ResponseType.SUCCESS;
        errorMessage = "";
    }

    @JsonProperty
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty
    public ResponseType getResponseType() {
        return respType;
    }
}
