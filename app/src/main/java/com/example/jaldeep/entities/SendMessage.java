package com.example.jaldeep.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jaldeep on 06/04/16.
 */
public class SendMessage {

    private final ResponseType responseType;
    private String errorMessage;

    public SendMessage() {
        responseType = ResponseType.SUCCESS;
        errorMessage = "";
    }

    public SendMessage(String errMsg) {
        this.responseType = ResponseType.ERROR;
        this.errorMessage = errMsg;
    }

    @JsonProperty
    public ResponseType getResponseType() {
        return responseType;
    }

    @JsonProperty
    public String getErrorMessage() {
        return errorMessage;
    }
}

