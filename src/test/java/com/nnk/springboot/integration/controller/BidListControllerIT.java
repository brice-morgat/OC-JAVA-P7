package com.nnk.springboot.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.BidServiceImpl;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("prod")
public class BidListControllerIT {
    @Autowired
    private BidServiceImpl bidService;

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
    public void bidListHomeTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/bidList/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/list"));
    }

    @Test
    @WithMockUser
    public void bidListAddFormTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/bidList/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/add"));
    }

    @Test
    @WithMockUser
    public void bidListValidateTest() throws Exception {
        BidList bidList = new BidList("account", "type", 1d);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                        .flashAttr("bidList", bidList))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    public void bidListValidateNotValidDataErrorTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                        .flashAttr("bidList", new BidList()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/add"));
    }

    @Test
    @WithMockUser
    public void bidListShowUpdateFormTest() throws Exception {
        BidList bidList = new BidList("a", "b", 1d);
        bidList.setBidListId(1);
        bidService.saveBidList(bidList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/bidList/update/1").flashAttr("bid",bidList))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/update"));
    }

    @Test
    @WithMockUser
    public void bidListUpdateTest() throws Exception {
        BidList bidList = new BidList("a", "b", 1d);
        bidList.setBidListId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1")
                        .flashAttr("bidList", bidList).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    public void bidListUpdateNoValidDataErrorTest() throws Exception {
        BidList bidList = new BidList("a", null, 1d);
        bidList.setBidListId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1")
                        .flashAttr("bidList", bidList).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/update"));
    }

    @Test
    @WithMockUser
    public void bidListDeleteTest() throws Exception {
        BidList bidList = new BidList("a", "b", 1d);
        bidList.setBidListId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/bidList/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
    }
}