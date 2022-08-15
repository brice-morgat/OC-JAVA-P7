package com.nnk.springboot.integration.controller;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.impl.TradeServiceImpl;
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
public class TradeControllerIT {

    @Autowired
    private TradeServiceImpl tradeService;

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
    public void tradeHomeTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/trade/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/list"));
    }

    @Test
    @WithMockUser
    public void tradeAddFormTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/trade/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/add"));
    }

    @Test
    @WithMockUser
    public void tradeValidateTest() throws Exception {
        Trade trade = new Trade("Name", "");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                        .flashAttr("trade", trade))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser
    public void tradeValidateNotValidDataErrorTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                        .flashAttr("trade", new Trade(null, "")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/add"));
    }

    @Test
    @WithMockUser
    public void tradeShowUpdateFormTest() throws Exception {
        Trade trade = new Trade("Name", "");
        tradeService.saveTrade(trade);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/trade/update/1").flashAttr("trade",trade))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/update"));
    }

    @Test
    @WithMockUser
    public void tradeUpdateTest() throws Exception {
        Trade trade = new Trade("Name", "");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                        .flashAttr("trade", trade).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser
    public void tradeUpdateNoValidDataErrorTest() throws Exception {
        Trade trade = new Trade(null, "");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                        .flashAttr("trade", trade).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/update"));
    }

    @Test
    @WithMockUser
    public void tradeDeleteTest() throws Exception {
        Trade trade = new Trade("Name", "");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/trade/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
    }
}