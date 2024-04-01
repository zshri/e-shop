package com.example.eshop.service;

import com.example.eshop.model.Role;
import com.example.eshop.model.User;
import com.example.eshop.model.exception.UserAlreadyExistsException;
import com.example.eshop.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public User registrationUser(User newUser) throws Exception {
        if (userRepository.findByEmail(newUser.getEmail()) != null) {
            throw new UserAlreadyExistsException("user is exist");
        } else {
            newUser.setPassword(this.bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setRoles(Collections.singleton(Role.ROLE_USER));
            return userRepository.save(newUser);
        }
    }
}