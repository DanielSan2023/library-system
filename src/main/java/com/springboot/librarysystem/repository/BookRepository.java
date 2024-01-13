package com.springboot.librarysystem.repository;

import com.springboot.librarysystem.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByBookIdIgnoreCase(String bookId);

   }
