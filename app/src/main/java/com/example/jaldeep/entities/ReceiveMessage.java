package com.example.jaldeep.entities;

/**
 * Created by Jaldeep on 06/04/16.
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by paulosk on 06/04/16.
 */
public class ReceiveMessage {

    private ResponseType responseType;
    private String errorMessage;

    private String message;
    private String sender;
    private Date sendDate;

    public ReceiveMessage() {
    }

    public ReceiveMessage(String errMsg) {
        responseType = ResponseType.ERROR;
        errorMessage = errMsg;

        message = sender = "";
        sendDate = null;
    }

    public ReceiveMessage(String msg, String sender, Date date) {
        responseType = ResponseType.SUCCESS;
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
        return responseType;
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
