package com.connect4.mavenproject10;

import java.io.*;
import java.util.*;
import java.util.Base64;

public class UserManager {
    private ArrayList<User> users = new ArrayList<>();
    private final String filePath = "users.dat";

    public UserManager() { loadUsers(); }

    public void loadUsers() {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            users = (ArrayList<User>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(String username, String password) {
        String hash = encrypt(password);
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPasswordHash().equals(hash)) {
                return u;
            }
        }
        return null;
    }

    public User signup(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return null;
        }
        String hash = encrypt(password);
        User newUser = new User(username, hash);
        users.add(newUser);
        saveUsers();
        return newUser;
    }

    public void updateScore(User user, int score) {
        user.addScore(score);
        saveUsers();
    }

    public String encrypt(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public String decrypt(String text) {
        return new String(Base64.getDecoder().decode(text));
    }
}
