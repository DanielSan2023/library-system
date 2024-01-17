package com.springboot.librarysystem.controller;

import com.springboot.librarysystem.DTO.BookDTO;
import com.springboot.librarysystem.DTO.UserDTO;
import com.springboot.librarysystem.domain.Book;
import com.springboot.librarysystem.domain.UserInfo;
import com.springboot.librarysystem.repository.BookRepository;
import com.springboot.librarysystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SmokeTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void GIVEN_empty_DB_WHEN_called_all_request_THEN_checked_responses() {
        //GIVEN empty DB
        assertThat(userRepository.findAll()).isEmpty();
        assertThat(bookRepository.findAll()).isEmpty();

        //Create books
        Long bookDTOId1 = createBook("The Prequel", "Harry Potter", "123016579987", "someUuid", false);
        Long bookDTOId2 = createBook("The Prisoner of Azkaban", "Harry Potter", "123456579989", "someUuid", false);

        //WHEN Call  Get request getALlBooks
        Book[] actualBook = restTemplate.getForObject(
                "http://localhost:" + port + "/api/books", Book[].class);

        //THEN Check books
        assertThat(actualBook).hasSize(2);
        assertThat(actualBook[0].getId()).isEqualTo(bookDTOId1);
        assertThat(actualBook[0].getTitle()).isEqualTo("The Prequel");
        assertThat(actualBook[0].getAuthor()).isEqualTo("Harry Potter");
        assertThat(actualBook[0].getBookId()).isEqualTo("123016579987");
        assertThat(actualBook[0].getUuid()).isNotEmpty();
        assertThat(actualBook[0].getBorrowedBy()).isNull();

        assertThat(actualBook[1].getId()).isEqualTo(bookDTOId2);
        assertThat(actualBook[1].getTitle()).isEqualTo("The Prisoner of Azkaban");
        assertThat(actualBook[1].getAuthor()).isEqualTo("Harry Potter");
        assertThat(actualBook[1].getBookId()).isEqualTo("123456579989");
        assertThat(actualBook[1].getUuid()).isNotEmpty();


        //Create users
        Long userDTOId = createUser("Jack", "Sparrow", "321654987123");

        //WHEN Call  Get request getALlUsers
        UserInfo[] actualUser = restTemplate.getForObject(
                "http://localhost:" + port + "/api/users", UserInfo[].class);

        //THEN Check user
        assertThat(actualUser).isNotEmpty().hasSize(1);
        assertThat(actualUser[0].getId()).isEqualTo(userDTOId);
        assertThat(actualUser[0].getName()).isEqualTo("Jack");
        assertThat(actualUser[0].getSurname()).isEqualTo("Sparrow");
        assertThat(actualUser[0].getPersonId()).isEqualTo("321654987123");

        //WHEN Call request where user borrow the book
        ResponseEntity<BookDTO> bookWithUser = restTemplate.exchange(
                "http://localhost:" + port + "/api/{bookId}/borrow/{userId}", HttpMethod.PUT, new HttpEntity<>(actualUser),
                BookDTO.class, bookDTOId1, userDTOId);

        //THEN Check bookWithUser
        assertThat(Objects.requireNonNull(bookWithUser.getBody()).getBorrowedBy()).isNotNull();
        assertThat(bookWithUser.getBody().getTitle()).isEqualTo("The Prequel");
        assertThat(bookWithUser.getBody().getBorrowedBy().getName()).isEqualTo("Jack");


        //WHEN Call request getBookById and getUserById
        BookDTO updatedBook = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/book/{id}", BookDTO.class, bookWithUser.getBody().getId()).getBody();

        UserDTO updatedUser = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/user/{id}", UserDTO.class, actualUser[0].getId()).getBody();

        //THEN Check updatedUser and updatedBook
        assertThat(Objects.requireNonNull(updatedBook).getBorrowedBy()).isNotNull();
        assertThat(updatedBook.getTitle()).isEqualTo("The Prequel");
        assertThat(updatedBook.getBorrowedBy().getName()).isEqualTo("Jack");

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getBooks()).hasSize(1);
        assertThat(updatedUser.getName()).isEqualTo("Jack");
        assertThat(updatedUser.getBooks()).filteredOn("author", "Harry Potter").isNotEmpty();
        assertThat(updatedUser.getBooks()).filteredOn("bookId", "123016579987").isNotEmpty();


        //WHEN Call request for returnBook
        restTemplate.delete("http://localhost:" + port + "/api/user/{id}/return", HttpMethod.DELETE,
                null, Void.class, Objects.requireNonNull(bookDTOId1));

        //WHEN Call request getBookById and getUserById
        BookDTO returnedBook = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/book/{id}", BookDTO.class, updatedBook.getId()).getBody();

        UserDTO userWithNoBook = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/user/{id}", UserDTO.class, actualUser[0].getId()).getBody();

        //THEN check book and user
           //     assertThat(returnedBook.getBorrowedBy()).isNull();

    }

    private Long createUser(String name, String surname, String personId) {
        UserDTO userDTO = new UserDTO(name, surname, personId);

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/user/create", userDTO, UserDTO.class);

        return Objects.requireNonNull(response.getBody()).getId();
    }

    private Long createBook(String title, String author, String bookId, String uuid, boolean borrowed) {
        BookDTO bookDTO = new BookDTO(title, author, bookId, uuid, borrowed);

        ResponseEntity<BookDTO> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/book/create", bookDTO, BookDTO.class);

        return Objects.requireNonNull(response.getBody()).getId();

    }
}