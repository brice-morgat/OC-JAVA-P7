package com.nnk.springboot.integration.controller;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.impl.CurvePointServiceImpl;
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
public class CurvePointControllerIT {
    @Autowired
    private CurvePointServiceImpl curvePointService;

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
    public void curvePointHomeTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/list"));
    }

    @Test
    @WithMockUser
    public void curvePointAddFormTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser
    public void curvePointValidateTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                        .flashAttr("curvePoint", curvePoint))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser
    public void curvePointValidateNotValidDataErrorTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                        .flashAttr("curvePoint", new CurvePoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser
    public void curvePointShowUpdateFormTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);
        curvePoint.setId(1);
        curvePointService.saveCurvePoint(curvePoint);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/update/1").flashAttr("curvePoint",curvePoint))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/update"));
    }

    @Test
    @WithMockUser
    public void curvePointUpdateTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);
        curvePoint.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
                        .flashAttr("curvePoint", curvePoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser
    public void curvePointUpdateNoValidDataErrorTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(null, 20d, 1d);
        curvePoint.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
                        .flashAttr("curvePoint", curvePoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/update"));
    }

    @Test
    @WithMockUser
    public void curvePointDeleteTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);
        curvePoint.setId(1);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));
    }
}
