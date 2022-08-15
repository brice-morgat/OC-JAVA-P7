package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.security.SecurityConfig;
import com.nnk.springboot.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Override
    public User saveUser(User user) {
        Objects.requireNonNull(user);
        try {
            user.setPassword(securityConfig.getPasswordEncoder().encode(user.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("LoadByUsername");
        Objects.requireNonNull(username);
        System.out.println("Username not null : " + username);
        User user = userRepository.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("LoadByUsername : " + user.toString());
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), new ArrayList<>());
        System.out.println(userDetails.toString());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), new ArrayList<>());
    }
}
