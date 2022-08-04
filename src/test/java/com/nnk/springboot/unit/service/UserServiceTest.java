package com.nnk.springboot.unit.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void findAllUserTest() {
        //Given
        List<User> users = new ArrayList();
        User userOne = new User();
        User userTwo = new User();
        users.add(userOne);
        users.add(userTwo);
        //When
        when(userRepository.findAll()).thenReturn(users);
        //Then
        assertEquals(userService.getAllUser(), users);
    }

    @Test
    public void saveUserTest() {
        //Given
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        //When
        when(userRepository.save(any())).thenReturn(user);
        //Then
        assertEquals(userService.saveUser(user), user);
    }

    @Test
    public void deleteUserTest() {
        //Then
        assertDoesNotThrow(() -> userService.deleteUser(new User()));
    }

    @Test
    public void deleteUserNullErrorTest() {
        //Then
        assertThrows(Exception.class,() -> userService.deleteUser(null));
    }

    @Test
    public void getUserByIdTest() {
        //Given
        User user = new User();
        //When
        when(userRepository.getById(anyInt())).thenReturn(user);
        //Then
        assertEquals(userService.getUserById(1), user);
    }
}
