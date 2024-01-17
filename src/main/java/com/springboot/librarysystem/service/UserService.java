package com.springboot.librarysystem.service;

import com.springboot.librarysystem.DTO.BookDTO;
import com.springboot.librarysystem.DTO.UserDTO;
import com.springboot.librarysystem.domain.Book;
import com.springboot.librarysystem.domain.UserInfo;
import com.springboot.librarysystem.repository.UserRepository;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    public static final int CORRECT_LENGTH_PERSON_ID = 12;

    final private UserRepository userRepository;
    private ModelMapper modelMapper;


    public List<UserDTO> findAllUsers() {
        List<UserInfo> users = userRepository.findAll(Sort.by("id"));
        return users.stream().map(userInfo -> convertDomainToDTO(userInfo)).toList();
    }

    UserDTO convertDomainToDTO(UserInfo userInfo) {
        UserDTO userDTO = modelMapper.map(userInfo, UserDTO.class);
        if (userInfo.getBooks() == null) {
            userDTO.setBooks(new HashSet<>());
        } else {
            Set<BookDTO> bookDTOs = userInfo.getBooks().stream()
                    .map(book -> convertBookToDTO(book))
                    .collect(Collectors.toSet());
            userDTO.setBooks(bookDTOs);
        }
        return userDTO;
    }

    private BookDTO convertBookToDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);
    }

    public UserDTO findUserById(Long id) {
        UserInfo existUserInfo = userRepository.findById(id).orElse(null);
        if (existUserInfo != null) {
            return convertDomainToDTO(existUserInfo);
        } else return null;
    }

    public UserDTO saveUser(UserDTO userDTO) {
        UserInfo userInfo = convertDTOToDomain(userDTO);
        validateNewUser(userInfo.getPersonId());

        return convertDomainToDTO(userRepository.save(userInfo));
    }

    private void validateNewUser(String personId) {
        if (personId.length() != CORRECT_LENGTH_PERSON_ID) {
            throw new RuntimeException("PersonId length doesn't match. It needs to have exactly: "
                    + CORRECT_LENGTH_PERSON_ID + " characters");
        }
        if (userRepository.existsByPersonIdIgnoreCase(personId)) {
            throw new RuntimeException("PersonId: " + personId + " already exists");
        }
    }

    UserInfo convertDTOToDomain(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserInfo.class);
    }

}
