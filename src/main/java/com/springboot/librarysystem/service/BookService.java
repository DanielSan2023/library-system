package com.springboot.librarysystem.service;


import com.springboot.librarysystem.DTO.BookDTO;
import com.springboot.librarysystem.domain.Book;
import com.springboot.librarysystem.domain.UserInfo;
import com.springboot.librarysystem.repository.BookRepository;
import com.springboot.librarysystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BookService {


    public static final int CORRECT_LENGTH_BOOK_ID = 12;

    final private BookRepository bookRepository;
    final private UserRepository userRepository;
    private ModelMapper modelMapper;


    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll(Sort.by("id"));
        return books.stream().map(book -> convertToDto(book)).toList();
    }

    private BookDTO convertToDto(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public BookDTO getBookById(Long id) {
        Book existBook = bookRepository.findById(id).orElse(null);
        if (existBook != null) {
            return convertToDto(existBook);
        } else {
            return null;
        }
    }

    public BookDTO saveBook(BookDTO bookDTO) {
        Book newBook = convertDTOToDomain(bookDTO);
        validateNewBook(newBook.getBookId());
        UUID uuid = UUID.randomUUID();
        newBook.setUuid(String.valueOf(uuid));
        bookRepository.save(newBook);
        return convertToDto(newBook);
    }

    public BookDTO saveBorrowBook(BookDTO bookDTO) {
        Book newBook = convertDTOToDomain(bookDTO);
        bookRepository.save(newBook);
        return convertToDto(newBook);
    }

    private void validateNewBook(String bookId) {
        if (bookId.length() != CORRECT_LENGTH_BOOK_ID) {
            throw new RuntimeException("BookId length doesn't match. It needs to have exactly: "
                    + CORRECT_LENGTH_BOOK_ID + " characters");
        }
        if (bookRepository.existsByBookIdIgnoreCase(bookId)) {
            throw new RuntimeException("BookId: " + bookId + " already exists");
        }
    }

    private Book convertDTOToDomain(BookDTO bookDTOSec) {
        return modelMapper.map(bookDTOSec, Book.class);
    }


    public void updateBookById(Long id, BookDTO updateBook) {
        Book bookDomain = convertDTOToDomain(updateBook);
        bookRepository.findById(id).ifPresent(existBook -> {
            existBook.setTitle(bookDomain.getTitle());
            existBook.setAuthor(bookDomain.getAuthor());
            bookRepository.save(existBook);
        });
    }

    public BookDTO borrowBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        UserInfo userInfo = userRepository.findById(userId).orElse(null);

        validateBookForBorrow(bookId, book, userInfo);

        book.setBorrowedBy(userInfo);
        book.setBorrowed(true);
        return saveBorrowBook(convertToDto(book));
    }

    private static void validateBookForBorrow(Long bookId, Book book, UserInfo userInfo) {
        if (book == null) {
            throw new RuntimeException("bookId " + bookId + " not found!");
        }
        if (book.isBorrowed()) {
            throw new RuntimeException("Book is not available for borrow!");
        }
        if (userInfo == null) {
            throw new RuntimeException("User does not exist in DB.");
        }
    }

    public BookDTO returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        validateBookForReturn(bookId, book);
        book.setBorrowedBy(null);
        book.setBorrowed(false);
        bookRepository.save(book);
        return convertToDto(book);
    }

    private static void validateBookForReturn(Long bookId, Book book) {
        if (book == null) {
            throw new RuntimeException("bookId " + bookId + " not found!");
        }
        if (!book.isBorrowed()) {
            throw new RuntimeException("Book " + book.getTitle() + "was not borrow.");
        }
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

}