package com.nnk.springboot.unit.controller;

import com.nnk.springboot.controllers.UserController;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc;

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
    public void userValidateNotSaveErrorTest() throws Exception {
        User user = new User();
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);        when(userService.saveUser(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
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
        user.setId(1);
        when(userService.getUserById(anyInt())).thenReturn(user);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/update"));
    }

    @Test
    @WithMockUser
    public void userShowUpdateErrorFormTest() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.getUserById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/update/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"));
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
        when(userService.saveUser(any())).thenReturn(user);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .flashAttr("user", user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser
    public void userUpdateNoValidDataErrorTest() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.saveUser(any())).thenReturn(user);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
                        .flashAttr("user", user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/update"));
    }

    @Test
    @WithMockUser
    public void userUpdateNotSaveErrorTest() throws Exception {
        User user = new User();
        user.setFullname("fullname");
        user.setUsername("User");
        user.setPassword("password");
        user.setRole("USER");
        user.setId(1);
        when(userService.saveUser(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/user/update/24")
                        .flashAttr("user", user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser
    public void userDeleteTest() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.getUserById(anyInt())).thenReturn(user);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser
    public void userDeleteErrorTest() throws Exception {
        when(userService.getUserById(anyInt())).thenThrow(NullPointerException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/30"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }
}
