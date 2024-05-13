package com.example.LibraryManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="publication_year")
    private int publicationYear;

    @Column(name="isbn")
    private String ISBN;

    @Column(name="description")
    private String description;

    @Column(name="price")
    private int price;

    @Column(name="availability")
    private boolean availability;

}
