package com.springboot.librarysystem.DTO;

import com.springboot.librarysystem.domain.UserInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BookDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    @Size(max = 255)
    private String author;

    @NotNull
    @Size(max = 12)
    private String bookId;

    @NotNull
    @Size(max = 255)
    private String uuid;

    @NotNull
    private boolean borrowed;

    @NotNull
    private UserInfo borrowedBy;

}
