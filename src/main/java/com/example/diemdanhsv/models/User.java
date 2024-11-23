package com.example.diemdanhsv.models;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String hashedPassword;
    private String role;
    private boolean firstLogin;
    private Date createdAt;
    private Date updatedAt;

    // Constructor đầy đủ
    public User(int id, String username, String hashedPassword, String role, 
                boolean firstLogin, Date createdAt, Date updatedAt) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.firstLogin = firstLogin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor rút gọn cho login
    public User(int id, String username, String hashedPassword, String role, boolean firstLogin) {
        this(id, username, hashedPassword, role, firstLogin, null, null);
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isFirstLogin() { return firstLogin; }
    public void setFirstLogin(boolean firstLogin) { this.firstLogin = firstLogin; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}

