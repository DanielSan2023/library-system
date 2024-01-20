package com.springboot.librarysystem.controller;

import com.springboot.librarysystem.DTO.BookDTO;
import com.springboot.librarysystem.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class BookController {

    final private BookService bookService;

    @Secured({"ROLE_EDITOR","ROLE_USER"})
    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getAllBookSortedById() {
        // bookService.saveBook(new BookDTO("Prsten", "Harry Potter", "123456579987", "someUuid", false));

        List<BookDTO> allBooks = bookService.getAllBooks();
        if (CollectionUtils.isEmpty(allBooks)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(allBooks, HttpStatus.OK);
        }
    }

    @Secured({"ROLE_EDITOR","ROLE_USER"})
    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        BookDTO existBookDTO = bookService.getBookById(id);
        if (existBookDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(existBookDTO, HttpStatus.OK);
        }
    }

    @Secured("ROLE_EDITOR")
    @PostMapping("/book/create")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO createdBookDTO = bookService.saveBook(bookDTO);
        return new ResponseEntity<>(createdBookDTO, HttpStatus.CREATED);
    }

    @Secured({"ROLE_EDITOR"})
    @PutMapping("book/{id}")
    public ResponseEntity<HttpStatus> updateBookById(@PathVariable Long id, @RequestBody BookDTO updateBook) {
        if (bookService.getBookById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            bookService.updateBookById(id, updateBook);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Secured({"ROLE_EDITOR"})
    @DeleteMapping("/book/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured({"ROLE_USER","ROLE_EDITOR"})
    @PostMapping("/{bookId}/borrow/{userId}")
    public ResponseEntity<BookDTO> borrowBook(@PathVariable Long bookId, @PathVariable Long userId) {
        BookDTO borrowedBook = bookService.borrowBook(bookId, userId);
        if (borrowedBook == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(borrowedBook);
        }
    }

    @Secured({"ROLE_EDITOR"})
    @PostMapping("/book/{bookId}/return")
    public ResponseEntity<BookDTO> returnBook(@PathVariable Long bookId) {
        BookDTO returnedBook = bookService.returnBook(bookId);
        if (returnedBook == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(returnedBook);
        }
    }

}
