package com.springboot.librarysystem.controller;

import com.springboot.librarysystem.DTO.BookDTO;
import com.springboot.librarysystem.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class BookController {

    final private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAllBook() {
       // bookService.saveBook(new BookDTO("Prsten", "Harry Potter", "123456579987", "someUuid", false));

        List<BookDTO> allBooks = bookService.getAllBooks();
        if (CollectionUtils.isEmpty(allBooks)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(allBooks, HttpStatus.OK);
        }
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO existBookDTO = bookService.getBookById(id);
        if (existBookDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(existBookDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/book/create")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO createdBookDTO = bookService.saveBook(bookDTO);
        return new ResponseEntity<>(createdBookDTO, HttpStatus.CREATED);
    }

    @PutMapping("book/{id}")
    public ResponseEntity<HttpStatus> updateBookById(@PathVariable Long id, @RequestBody BookDTO updateBook) {
        if (bookService.getBookById(id) != null) {
            bookService.updateBookById(id, updateBook);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{bookId}/borrow/{userId}")
    public ResponseEntity<BookDTO> borrowBook(@PathVariable Long bookId, @PathVariable Long userId) {
        BookDTO borrowedBook = bookService.borrowBook(bookId, userId);
        if (borrowedBook != null) {
            return ResponseEntity.ok(borrowedBook);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/book/{bookId}/return")
    public ResponseEntity<BookDTO> returnBook(@PathVariable Long bookId) {
        BookDTO returnedBook = bookService.returnBook(bookId);
        if (returnedBook != null) {
            return ResponseEntity.ok(returnedBook);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}