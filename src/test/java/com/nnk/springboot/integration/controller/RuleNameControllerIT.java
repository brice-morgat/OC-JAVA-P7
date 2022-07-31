package com.nnk.springboot.integration.controller;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.impl.RuleNameServiceImpl;
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
public class RuleNameControllerIT {

    @Autowired
    private RuleNameServiceImpl ruleNameService;

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
    public void ruleNameHomeTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/list"));
    }

    @Test
    @WithMockUser
    public void ruleNameAddFormTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/add"));
    }

    @Test
    @WithMockUser
    public void ruleNameValidateTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
                        .flashAttr("ruleName", ruleName))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser
    public void ruleNameValidateNotValidDataErrorTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
                        .flashAttr("ruleName", new RuleName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/add"));
    }

    @Test
    @WithMockUser
    public void ruleNameShowUpdateFormTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleName.setId(1);
        ruleNameService.saveRuleName(ruleName);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/update/1").flashAttr("bid",ruleName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/update"));
    }

    @Test
    @WithMockUser
    public void ruleNameUpdateTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleName.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1")
                        .flashAttr("ruleName", ruleName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser
    public void ruleNameUpdateNoValidDataErrorTest() throws Exception {
        RuleName ruleName = new RuleName(null, "Desc", "json","Template", "sql","sql");
        ruleName.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1")
                        .flashAttr("ruleName", ruleName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/update"));
    }

    @Test
    @WithMockUser
    public void ruleNameDeleteTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleName.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ruleName/list"));
    }
}
