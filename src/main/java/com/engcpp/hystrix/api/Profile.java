package com.engcpp.hystrix.api;

/**
 *
 * @author engcpp
 */
public class Profile {
    private String id;
    private String name;
    private String email;
    private String gender;

    public String getId() {
        return id;
    }

    public Profile withId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Profile withName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Profile withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Profile withGender(String gender) {
        this.gender = gender;
        return this;
    }        
}