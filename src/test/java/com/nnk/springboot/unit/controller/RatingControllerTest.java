package com.nnk.springboot.unit.controller;

import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.controllers.RatingController;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.RatingServiceImpl;
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
@WebMvcTest(RatingController.class)
public class RatingControllerTest {
    @MockBean
    private RatingServiceImpl ratingService;

    @Autowired
    private MockMvc mockMvc;

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
    public void ratingValidateNotSaveErrorTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        when(ratingService.saveRating(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
                        .flashAttr("rating", rating))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

//    @Test
//    @WithMockUser
//    public void ratingValidateNotValidDataErrorTest() throws Exception {
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
//                        .flashAttr("rating", new Rating()))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("rating/add"));
//    }

    @Test
    @WithMockUser
    public void ratingShowUpdateFormTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        rating.setId(1);
        when(ratingService.getRatingById(anyInt())).thenReturn(rating);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rating/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("rating/update"));
    }

    @Test
    @WithMockUser
    public void ratingShowUpdateErrorFormTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        rating.setId(1);
        when(ratingService.getRatingById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rating/update/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser
    public void ratingUpdateTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        rating.setId(1);
        when(ratingService.saveRating(any())).thenReturn(rating);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
                        .flashAttr("rating", rating).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));
    }

//    @Test
//    @WithMockUser
//    public void ratingUpdateNoValidDataErrorTest() throws Exception {
//        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
//        rating.setId(1);
//        when(ratingService.saveRating(any())).thenReturn(rating);
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
//                        .flashAttr("rating", rating).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("rating/update"));
//    }

    @Test
    @WithMockUser
    public void ratingUpdateNotSaveErrorTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        rating.setId(1);
        when(ratingService.saveRating(any())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1")
                        .flashAttr("rating", rating).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }

    @Test
    @WithMockUser
    public void ratingDeleteTest() throws Exception {
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        rating.setId(1);
        when(ratingService.getRatingById(anyInt())).thenReturn(rating);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rating/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isMovedTemporarily())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser
    public void ratingDeleteErrorTest() throws Exception {
        when(ratingService.getRatingById(anyInt())).thenThrow(InvalidInputException.class);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/rating/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }
}
