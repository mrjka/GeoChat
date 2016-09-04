package com.example.jaldeep.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Jaldeep on 08/04/16.
 */
public class User {

    private  ResponseType responseType;
    private String errorMessage;

    private String name;
    private int age;
    private String description;
    private String email;

    public User(String name, int age, String email, String description)  {
        this.responseType = ResponseType.SUCCESS;
        this.name = name;
        this.age = age;
        this.email = email;
        this.description = description;
        this.errorMessage = "";
    }

    public User(String errMsg) {
        this.responseType = ResponseType.ERROR;
        this.errorMessage = errMsg;

        this.age = 0;
        this.name = this.description = this.email = "";
    }

    public User() {}

    @JsonProperty
    public String getName() {
        return name;
    }

    /**
     * Returns age of the user
     */
    @JsonProperty
    public int getAge() {
        return age;
    }

    /**
     * Returns the description of the user
     */
    @JsonProperty
    public String getDescription()  {
        return description;
    }

    @JsonProperty
    public String getEmail() { return email; }
}
