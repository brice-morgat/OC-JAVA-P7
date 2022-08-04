package com.nnk.springboot.integration.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.UserServiceImpl;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("prod")
public class UserServiceIT {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private Environment env;

    @BeforeEach
    public void init(){
        executeSql("src/main/resources/poseidon-skeleton_test_db.sql");
    }

    private void executeSql(String sqlFilePath) {
        final class SqlExecuter extends SQLExec {
            public SqlExecuter() {
                Project project = new Project();
                project.init();
                setProject(project);
                setTaskType("sql");
                setTaskName("sql");
            }
        }

        SqlExecuter executer = new SqlExecuter();
        executer.setSrc(new File(sqlFilePath));
        executer.setDriver("com.mysql.jdbc.Driver");
        executer.setPassword(env.getProperty("spring.datasource.password"));
        executer.setUserid(env.getProperty("spring.datasource.username"));
        executer.setUrl(env.getProperty("spring.datasource.url"));
        executer.execute();
    }


    @Test
    public void findAllUserTest() {
        //Given
        List<User> users = new ArrayList();
        User user = new User();
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);
        User userTwo = new User();
        userTwo.setFullname("fullname");
        userTwo.setUsername("User");
        userTwo.setPassword("password");
        userTwo.setRole("USER");
        userTwo.setId(1);
        users.add(user);
        users.add(userTwo);
        //Then
        assertDoesNotThrow(() ->userService.getAllUser());
    }

    @Test
    public void saveUserTest() {
        //Given
        User user = new User();
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);        //Then
        assertEquals(userService.saveUser(user).getUsername(), user.getUsername());
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
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);        //Then
        assertDoesNotThrow(() -> userService.getUserById(1));
    }
}
