package com.nnk.springboot.integration.controller;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.impl.UserServiceImpl;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("prod")
public class UserControllerIT {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;


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
    @WithMockUser
    public void userHomeTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/list"));
    }

    @Test
    @WithMockUser
    public void userAddFormTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/add"));
    }

    @Test
    @WithMockUser
    public void userValidateTest() throws Exception {
        User user = new User();
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser
    public void userValidateNotValidDataErrorTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                        .flashAttr("user", new User()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/add"));
    }

    @Test
    @WithMockUser
    public void userShowUpdateFormTest() throws Exception {
        User user = new User();
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);
        userService.saveUser(user);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/update/1").flashAttr("bid",user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/update"));
    }

    @Test
    @WithMockUser
    public void userUpdateTest() throws Exception {
        User user = new User();
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .flashAttr("user", user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser
    public void userUpdateNoValidDataErrorTest() throws Exception {
        User user = new User();
        user.setFullname(null);
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .flashAttr("user", user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/update"));
    }

    @Test
    @WithMockUser
    public void userDeleteTest() throws Exception {
        User user = new User();
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"));
    }
}
