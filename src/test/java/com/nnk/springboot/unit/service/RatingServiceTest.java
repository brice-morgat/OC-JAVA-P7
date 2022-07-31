package com.nnk.springboot.unit.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.impl.RatingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class RatingServiceTest {
    @InjectMocks
    private RatingServiceImpl ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Test
    public void findAllRatingTest() {
        //Given
        List<Rating> ratingList = new ArrayList();
        Rating ratingOne = new Rating("Moody", "Sand", "Fitch", 1);
        Rating ratingTwo = new Rating("Moody", "Sand", "Fitch", 1);
        ratingList.add(ratingOne);
        ratingList.add(ratingTwo);
        //When
        when(ratingRepository.findAll()).thenReturn(ratingList);
        //Then
        assertEquals(ratingService.getAllRating(), ratingList);
    }

    @Test
    public void saveRatingTest() {
        //Given
        Rating rating = new Rating("Moody", "Sand", "Fitch", 1);

        //When
        when(ratingRepository.save(any())).thenReturn(rating);
        //Then
        assertEquals(ratingService.saveRating(rating), rating);
    }

    @Test
    public void deleteRatingTest() {
        //Then
        assertDoesNotThrow(() -> ratingService.deleteRating(new Rating()));
    }

    @Test
    public void deleteRatingNullErrorTest() {
        //Then
        assertThrows(Exception.class,() ->  ratingService.deleteRating(null));
    }

    @Test
    public void getRatingByIdTest() {
        //Given
        Rating rating = new Rating("Moody", "Sand", "Fitch", 1);
        //When
        when(ratingRepository.getById(anyInt())).thenReturn(rating);
        //Then
        assertEquals(ratingService.getRatingById(1), rating);
    }
}
