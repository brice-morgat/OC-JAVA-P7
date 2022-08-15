package com.nnk.springboot.unit.controller;

import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.TradeServiceImpl;
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
@WebMvcTest(TradeController.class)
public class TradeControllerTest {
    @MockBean
    private TradeServiceImpl tradeService;

    @Autowired
    private MockMvc mockMvc;

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
    public void tradeValidateNotSaveErrorTest() throws Exception {
        Trade trade = new Trade("Name", "");
        when(tradeService.saveTrade(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
                        .flashAttr("trade", trade))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
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
        when(tradeService.getTradeById(anyInt())).thenReturn(trade);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/trade/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/update"));
    }

    @Test
    @WithMockUser
    public void tradeShowUpdateErrorFormTest() throws Exception {
        Trade trade = new Trade("Name", "");
        when(tradeService.getTradeById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/trade/update/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser
    public void tradeUpdateTest() throws Exception {
        Trade trade = new Trade("Name", "");
        when(tradeService.saveTrade(any())).thenReturn(trade);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                        .flashAttr("trade", trade).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser
    public void tradeUpdateNoValidDataErrorTest() throws Exception {
        Trade trade = new Trade(null, "");
        when(tradeService.saveTrade(any())).thenReturn(trade);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                        .flashAttr("trade", trade).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("trade/update"));
    }

    @Test
    @WithMockUser
    public void tradeUpdateNotSaveErrorTest() throws Exception {
        Trade trade = new Trade("Name", "");
        when(tradeService.saveTrade(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1")
                        .flashAttr("trade", trade).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser
    public void tradeDeleteTest() throws Exception {
        Trade trade = new Trade("Name", "");
        when(tradeService.getTradeById(anyInt())).thenReturn(trade);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/trade/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser
    public void tradeDeleteErrorTest() throws Exception {
        when(tradeService.getTradeById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/trade/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }
}