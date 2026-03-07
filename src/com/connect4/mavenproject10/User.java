package com.connect4.mavenproject10;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String passwordHash;
    private int score;

    public User(String username, String passwordHash, int score) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.score = score;
    }

    public User(String username, String passwordHash) {
        this(username, passwordHash, 0);
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public int getScore() { return score; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setScore(int score) { this.score = score; }
    public void addScore(int amount) { this.score += amount; }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", score=" + score + '}';
    }
}
