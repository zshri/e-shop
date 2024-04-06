package com.example.eshop.service;

import com.example.eshop.model.Role;
import com.example.eshop.model.User;
import com.example.eshop.model.exception.UserAlreadyExistsException;
import com.example.eshop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith({MockitoExtension.class})
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    String username = "user1";
    String email = "mail@example.com";
    String password = "123456";
    String encodedPassword = "encoded123456";

    UserServiceTest() {
    }

    @Test
    public void testRegistrationUser_Success() throws Exception {

            Mockito.when(userRepository.findByEmail(email)).thenReturn( null);
            Mockito.when(bCryptPasswordEncoder.encode(password)).thenReturn(encodedPassword);

            UserService userService = new UserService(userRepository, bCryptPasswordEncoder);

            userService.registrationUser(User.builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .build());

            Mockito.verify(userRepository).save(User.builder()
                    .username(username)
                    .email(email)
                    .password(encodedPassword)
                    .roles(Collections.singleton(Role.ROLE_USER))
                    .build());

    }

    @Test
    public void testRegistrationUser_UserAlreadyExists() throws Exception {
        User existingUser = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
        Mockito.when(userRepository.findByEmail(email)).thenReturn(existingUser);
        UserService userService = new UserService(userRepository, bCryptPasswordEncoder);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registrationUser(existingUser);
        });

        Mockito.verify(userRepository, Mockito.never()).save((User)Mockito.any());
    }

    @Test
    public void testRegistrationUser_RepositoryThrowsException() {
        User newUser = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        Mockito.doThrow(new RuntimeException("Database error")).when(userRepository).save(newUser);

        assertThrows(RuntimeException.class, () -> userService.registrationUser(newUser));

        Mockito.verify(userRepository).save(newUser);
    }

}
