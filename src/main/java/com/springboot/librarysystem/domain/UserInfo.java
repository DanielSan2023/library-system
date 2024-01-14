package com.springboot.librarysystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence")
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(unique = true, length = 12)
    private String personId;

    @JsonIgnore
    @OneToMany(mappedBy = "borrowedBy",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private Set<Book> books;

    public void addBook(Book book) {
        if (null == books) {
            books = new HashSet<>() {
            };
        }
        books.add(book);
        book.setBorrowedBy(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.setBorrowedBy(null);
    }

    public UserInfo() {
    }

}
