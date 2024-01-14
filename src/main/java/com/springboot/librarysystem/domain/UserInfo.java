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
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence")
    private Long id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(unique = true, length = 12)
    private String personId;

    @OneToMany(mappedBy = "borrowedBy")
    private Collection<Book> books;

    public UserInfo() {
    }

}
