package com.nnk.springboot.integration.controller;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.impl.RatingServiceImpl;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.junit.jupiter.api.AfterAll;
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
public class RatingControllerIT {
    @Autowired
    private RatingServiceImpl ratingService;

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
    public void ratingHomeTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rating/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/list"));
    }

    @Test
    @WithMockUser
    public void ratingAddFormTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rating/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/add"));
    }

    @Test
    @WithMockUser
    public void ratingValidateTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
                        .flashAttr("rating", rating))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser
    public void ratingValidateNotValidDataErrorTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
                        .flashAttr("rating", new Rating()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/add"));
    }

    @Test
    @WithMockUser
    public void ratingShowUpdateFormTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        rating.setId(1);
        ratingService.saveRating(rating);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rating/update/1").flashAttr("rating",rating))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/update"));
    }

    @Test
    @WithMockUser
    public void ratingUpdateTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        rating.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
                        .flashAttr("rating", rating).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser
    public void ratingUpdateNoValidDataErrorTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", null);
        rating.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
                        .flashAttr("rating", rating).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/update"));
    }

    @Test
    @WithMockUser
    public void ratingDeleteTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        rating.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rating/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));
    }
}
