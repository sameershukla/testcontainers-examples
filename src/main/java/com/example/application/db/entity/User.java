package com.example.application.db.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Table(name="USER")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="created_at")
    private Timestamp created_at;
}
