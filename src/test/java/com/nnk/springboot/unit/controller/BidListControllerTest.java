package com.nnk.springboot.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.BidServiceImpl;
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
@WebMvcTest(BidListController.class)
public class BidListControllerTest {
    @MockBean
    private BidServiceImpl bidService;

    @Autowired
    private MockMvc mockMvc;

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
    public void bidListValidateNotSaveErrorTest() throws Exception {
        BidList bidList = new BidList("account", "type", 1d);
        when(bidService.saveBidList(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate")
                        .flashAttr("bidList", bidList))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
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
        when(bidService.getBidListById(anyInt())).thenReturn(bidList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/bidList/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/update"));
    }

    @Test
    @WithMockUser
    public void bidListShowUpdateErrorFormTest() throws Exception {
        BidList bidList = new BidList("a", "b", 1d);
        bidList.setBidListId(1);
        when(bidService.getBidListById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/bidList/update/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    public void bidListUpdateTest() throws Exception {
        BidList bidList = new BidList("a", "b", 1d);
        bidList.setBidListId(1);
        when(bidService.saveBidList(any())).thenReturn(bidList);
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
        when(bidService.saveBidList(any())).thenReturn(bidList);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1")
                        .flashAttr("bidList", bidList).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bidList/update"));
    }

    @Test
    @WithMockUser
    public void bidListUpdateNotSaveErrorTest() throws Exception {
        BidList bidList = new BidList("a", "b", 1d);
        bidList.setBidListId(1);
        when(bidService.saveBidList(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1")
                        .flashAttr("bidList", bidList).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser
    public void bidListDeleteTest() throws Exception {
        BidList bidList = new BidList("a", "b", 1d);
        bidList.setBidListId(1);
        when(bidService.getBidListById(anyInt())).thenReturn(bidList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/bidList/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    public void bidListDeleteErrorTest() throws Exception {
        when(bidService.getBidListById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/bidList/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
