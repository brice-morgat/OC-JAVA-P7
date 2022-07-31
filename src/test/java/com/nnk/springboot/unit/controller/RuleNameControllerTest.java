package com.nnk.springboot.unit.controller;

import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.RuleNameServiceImpl;
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
@WebMvcTest(RuleNameController.class)
public class RuleNameControllerTest {
    @MockBean
    private RuleNameServiceImpl ruleNameService;

    @Autowired
    private MockMvc mockMvc;

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
    public void ruleNameValidateNotSaveErrorTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        when(ruleNameService.saveRuleName(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
                        .flashAttr("ruleName", ruleName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
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
        when(ruleNameService.getRuleNameById(anyInt())).thenReturn(ruleName);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/update"));
    }

    @Test
    @WithMockUser
    public void ruleNameShowUpdateErrorFormTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleName.setId(1);
        when(ruleNameService.getRuleNameById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/update/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser
    public void ruleNameUpdateTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleName.setId(1);
        when(ruleNameService.saveRuleName(any())).thenReturn(ruleName);
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
        when(ruleNameService.saveRuleName(any())).thenReturn(ruleName);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1")
                        .flashAttr("ruleName", ruleName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("ruleName/update"));
    }

    @Test
    @WithMockUser
    public void ruleNameUpdateNotSaveErrorTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleName.setId(1);
        when(ruleNameService.saveRuleName(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1")
                        .flashAttr("ruleName", ruleName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser
    public void ruleNameDeleteTest() throws Exception {
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleName.setId(1);
        when(ruleNameService.getRuleNameById(anyInt())).thenReturn(ruleName);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser
    public void ruleNameDeleteErrorTest() throws Exception {
        when(ruleNameService.getRuleNameById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }
}
