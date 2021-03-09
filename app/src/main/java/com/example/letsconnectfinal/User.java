package com.example.letsconnectfinal;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String type;
    public String email;
    public String fname;
    public String lname;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String type, String email, String fname, String lname) {
        this.type = type;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
    }
}
