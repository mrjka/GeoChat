package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by paulosk on 05/04/16.
 */
public class Login {

    private ResponseType responseType;

    private final String dynamicID;
    private String errorMessage;

    public Login(String dynID) {
        this.responseType = ResponseType.SUCCESS;
        this.dynamicID = dynID;
        this.errorMessage = "";
    }

    public Login() {
        responseType = ResponseType.ERROR;
        this.dynamicID = "";
    }

    @JsonProperty
    public String getDynamicID() {
        return dynamicID;
    }

    @JsonProperty
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @JsonProperty
    public ResponseType getResponseType() {
        return responseType;
    }

    public void setErrorMessage(String msg) {
        this.errorMessage = msg;
    }
}