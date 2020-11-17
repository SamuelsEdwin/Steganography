//This program is a modification of the codelab friedly chat tutorial
package com.google.firebase.codelab.littleAlcove;

public class User {
    private String name;
    private String fullName;
    public User() {
        name=null;
        fullName=null;
    }
    public User(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
