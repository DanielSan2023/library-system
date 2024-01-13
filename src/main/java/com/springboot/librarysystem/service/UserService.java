package com.springboot.librarysystem.service;

import com.springboot.librarysystem.DTO.UserDTO;
import com.springboot.librarysystem.domain.UserInfo;
import com.springboot.librarysystem.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    public static final int CORRECT_LENGTH_PERSON_ID = 12;
    final private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAllUsers() {
        List<UserInfo> users = userRepository.findAll(Sort.by("id"));
        return users.stream().map(userInfo -> convertDomainToDTO(userInfo, new UserDTO())).toList();
    }

    UserDTO convertDomainToDTO(UserInfo userInfo, UserDTO userDTO) {
        userDTO.setId(userInfo.getId());
        userDTO.setName(userInfo.getName());
        userDTO.setSurname(userInfo.getSurname());
        userDTO.setPersonId(userInfo.getPersonId());
        return userDTO;
    }

    public UserDTO findUserById(Long id) {
        UserInfo existUserInfo = userRepository.findById(id).orElse(null);
        if (existUserInfo != null) {
            return convertDomainToDTO(existUserInfo, new UserDTO());
        } else return null;
    }

    public UserDTO saveUser(UserDTO userDTO) {
        UserInfo userInfo = convertDTOToDomain(userDTO);
        validateNewUser(userInfo.getPersonId());
        return convertDomainToDTO(userInfo, new UserDTO());
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
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userDTO.getId());
        userInfo.setName(userDTO.getName());
        userInfo.setSurname(userDTO.getSurname());
        userInfo.setPersonId(userDTO.getPersonId());
        return userInfo;
    }


}