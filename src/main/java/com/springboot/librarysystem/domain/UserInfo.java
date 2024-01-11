package com.springboot.librarysystem.domain;

import jakarta.persistence.*;

import lombok.*;

import java.util.Collection;


@Entity
@Getter
@Setter
@ToString
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(unique = true, length = 12)
    private String personId;

    @Column(unique = true)
    private String uuid;

    public UserInfo() {
    }

    public UserInfo(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @OneToMany(mappedBy = "borrowedBy")
    private Collection<Book> book;

    public Collection<Book> getBook() {
        return book;
    }

    public void setBook(Collection<Book> book) {
        this.book = book;
    }
}
