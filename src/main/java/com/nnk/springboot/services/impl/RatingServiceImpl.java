package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.IRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RatingServiceImpl implements IRatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Rating saveRating(Rating rating) {
        Objects.requireNonNull(rating);
        if (rating.getOrderNumber() == null) {
            throw new InvalidInputException("Order number must not be null");
        }
        return ratingRepository.save(rating);
    }

    @Override
    public void deleteRating(Rating rating) {
        Objects.requireNonNull(rating);
        ratingRepository.delete(rating);
    }

    @Override
    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating getRatingById(Integer id) {
        Rating rating = ratingRepository.getById(id);
        if (rating != null) {
            return rating;
        }
        throw new InvalidInputException("Rating not Found");
    }
}
