package com.example.jasper_report_app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private Long userId;
    
    public User() {}
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}