package com.springboot.librarysystem.service;


import com.springboot.librarysystem.DTO.BookDTO;
import com.springboot.librarysystem.domain.Book;
import com.springboot.librarysystem.domain.UserInfo;
import com.springboot.librarysystem.repository.BookRepository;
import com.springboot.librarysystem.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class BookService {

    public static final int CORRECT_LENGTH_BOOK_ID = 12;
    final private BookRepository bookRepository;

    final private UserRepository userRepository;


    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll(Sort.by("id"));
        return books.stream().map(book -> convertDomainToDTO(book, new BookDTO())).toList();
    }

    private BookDTO convertDomainToDTO(Book book, BookDTO bookDTO) {
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setBookId(book.getBookId());
        bookDTO.setUuid(book.getUuid());
        bookDTO.setBorrowed(book.isBorrowed());
        bookDTO.setBorrowedBy(book.getBorrowedBy());
        return bookDTO;
    }

    public BookDTO getBookById(Long id) {
        Book existBook = bookRepository.findById(id).orElse(null);
        if (existBook != null) {
            return convertDomainToDTO(existBook, new BookDTO());
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
        return convertDomainToDTO(newBook, new BookDTO());
    }

    public BookDTO saveBorrowBook(BookDTO bookDTO) {
        Book newBook = convertDTOToDomain(bookDTO);
        bookRepository.save(newBook);
        return convertDomainToDTO(newBook, new BookDTO());
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
        Book bookDomain = new Book();
        bookDomain.setId(bookDTOSec.getId());
        bookDomain.setTitle(bookDTOSec.getTitle());
        bookDomain.setAuthor(bookDTOSec.getAuthor());
        bookDomain.setBookId(bookDTOSec.getBookId());
        bookDomain.setUuid(bookDTOSec.getUuid());
        bookDomain.setBorrowed(bookDTOSec.isBorrowed());
        bookDomain.setBorrowedBy(bookDTOSec.getBorrowedBy());
        return bookDomain;
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
        return saveBorrowBook(convertDomainToDTO(book, new BookDTO()));
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

        return convertDomainToDTO(book, new BookDTO());
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