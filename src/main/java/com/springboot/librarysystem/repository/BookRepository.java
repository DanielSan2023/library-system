package com.springboot.librarysystem.repository;

import com.springboot.librarysystem.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existBookIdIgnoreCase(String bookId);
}
