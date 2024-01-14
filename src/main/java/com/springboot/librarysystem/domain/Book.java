package com.springboot.librarysystem.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String author;

    @Column(unique = true, length = 12)
    private String bookId;

    @Column
    private String uuid;

    @Column
    private boolean borrowed;

    @ManyToOne
    @JoinColumn(name = "userInfo_id")
    private UserInfo borrowedBy;

    public Book() {
    }

    public Book(String title, String author, String bookId, String uuid, boolean borrowed) {
        this.title = title;
        this.author = author;
        this.bookId = bookId;
        this.uuid = uuid;
        this.borrowed = borrowed;
    }

}
