package entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by paulosk on 06/04/16.
 */
public class RecvMessage {

    private final ResponseType respType;
    private final String errorMessage;

    private final String message;
    private final String sender;
    private final Date sendDate;

    public RecvMessage(String errMsg) {
        respType = ResponseType.ERROR;
        errorMessage = errMsg;

        message = sender = "";
        sendDate = null;
    }

    public RecvMessage(String msg, String sender, Date date) {
        respType = ResponseType.SUCCESS;
        errorMessage = "";

        this.message = msg;
        this.sender = sender;
        this.sendDate = date;
    }

    @JsonProperty
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty
    public ResponseType getResponseType() {
        return respType;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public String getSender() {
        return sender;
    }

    @JsonProperty
    public Date getSendDate() {
        return sendDate;
    }
}
