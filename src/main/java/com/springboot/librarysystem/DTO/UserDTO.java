package com.springboot.librarysystem.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String surname;

    @NotNull
    @Size(max = 12)
    private String personId;

    private Set<BookDTO> books;

    public Set<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(Set<BookDTO> books) {
        this.books = books;
    }

    public UserDTO(String name, String surname, String personId) {
        this.name = name;
        this.surname = surname;
        this.personId = personId;
    }
}
