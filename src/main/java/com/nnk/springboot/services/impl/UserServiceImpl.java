package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        Objects.requireNonNull(user);
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception e) {
            throw new InvalidInputException("");
        }

    }

    @Override
    public void deleteUser(User user) {
        Objects.requireNonNull(user);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        User user = userRepository.getById(id);
        if (user != null) {
            return user;
        }
        throw new InvalidInputException("Invalid user Id:" + id);
    }
}
