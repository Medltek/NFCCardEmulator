package com.example.nfccardemulator;


public class User {

    private final String username;
    private final String password;
    private final byte[] testingToken;

    public User(String username, String password, byte[] testingToken) {
        this.username = username;
        this.password = password;
        this.testingToken = testingToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getTestingToken() {
        return testingToken;
    }

}