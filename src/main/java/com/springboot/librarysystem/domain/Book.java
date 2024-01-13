package com.springboot.librarysystem.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

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

}
