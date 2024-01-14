package com.springboot.librarysystem.controller;

import com.springboot.librarysystem.DTO.BookDTO;
import com.springboot.librarysystem.domain.Book;
import com.springboot.librarysystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;
//
//    @BeforeEach
//    void setUp() {
//        bookRepository.deleteAll();
//    }

//    @Test
//    void GIVEN_empty_DB_WHEN_get_users_THEN_nothing_is_returned() {
//        Book[] book = restTemplate.getForObject(
//                "http://localhost:" + port + "/api/v1/books", Book[].class);
//
//        assertThat(book).isNullOrEmpty();
//    }

    @Test
    void GIVEN_saved_correct_book_dto_in_db_WHEN_get_book_endpoint_THEN_saved_book_is_returned_and_checked() {
        //GIVEN
        BookDTO bookDTO =  new BookDTO("Prsten", "Harry Potter", "123456579987", "someUuid", false);

        ResponseEntity<BookDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/book/create", bookDTO, BookDTO.class);
        Long bookDTOId = Objects.requireNonNull(response.getBody()).getId();

        //WHEN
        Book[] actual = restTemplate.getForObject(
                "http://localhost:" + port + "/api/books", Book[].class);

        //THEN
        assertThat(actual).isNotEmpty().hasSize(1);
        assertThat(actual[0].getId()).isEqualTo(bookDTOId);
        assertThat(actual[0].getTitle()).isEqualTo("Prsten");
        assertThat(actual[0].getAuthor()).isEqualTo("Harry Potter");
        assertThat(actual[0].getBookId()).isEqualTo("123456579987");
        assertThat(actual[0].getUuid()).isNotEmpty();
    }


    @Test
    void getBookById() {
    }

    @Test
    void createBook() {
    }

    @Test
    void updateBookById() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void borrowBook() {
    }

    @Test
    void returnBook() {
    }
}