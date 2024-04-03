package com.example.LibraryManagementSystem.entity;

import jakarta.persistence.*;


@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String title;

    private String author;

    @ISBN(type = ISBN.Type.ANY)
    private String ISBN;

}
