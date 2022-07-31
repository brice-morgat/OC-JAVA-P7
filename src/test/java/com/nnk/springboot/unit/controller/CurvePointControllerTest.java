package com.nnk.springboot.unit.controller;
import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.CurvePointServiceImpl;
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
@WebMvcTest(CurveController.class)
public class CurvePointControllerTest {
    @MockBean
    private CurvePointServiceImpl curvePointService;

    @Autowired
    private MockMvc mockMvc;

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
    public void curvePointValidateNotSaveErrorTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);
        when(curvePointService.saveCurvePoint(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
                        .flashAttr("curvePoint", curvePoint))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
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
        when(curvePointService.getCurvePointById(anyInt())).thenReturn(curvePoint);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/update"));
    }

    @Test
    @WithMockUser
    public void curvePointShowUpdateErrorFormTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);
        curvePoint.setId(1);
        when(curvePointService.getCurvePointById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/update/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser
    public void curvePointUpdateTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);
        curvePoint.setId(1);
        when(curvePointService.saveCurvePoint(any())).thenReturn(curvePoint);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
                        .flashAttr("curvePoint", curvePoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser
    public void curvePointUpdateNoValidDataErrorTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(null, 10d, 1d);
        curvePoint.setId(1);
        when(curvePointService.saveCurvePoint(any())).thenReturn(curvePoint);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
                        .flashAttr("curvePoint", curvePoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("curvePoint/update"));
    }

    @Test
    @WithMockUser
    public void curvePointUpdateNotSaveErrorTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 10d, 1d);
        curvePoint.setId(1);
        when(curvePointService.saveCurvePoint(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
                        .flashAttr("curvePoint", curvePoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser
    public void curvePointDeleteTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint(1, 10d, 1d);
        curvePoint.setId(1);
        when(curvePointService.getCurvePointById(anyInt())).thenReturn(curvePoint);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser
    public void curvePointDeleteErrorTest() throws Exception {
        when(curvePointService.getCurvePointById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }
}

