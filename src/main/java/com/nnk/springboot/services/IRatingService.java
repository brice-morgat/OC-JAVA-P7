package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;

import java.util.List;

public interface IRatingService {

    /**
     * Save a rating.
     *
     * @param rating The rating object to be saved.
     * @return Rating
     */
    public Rating saveRating(Rating rating);

    /**
     * Delete the given rating from the database.
     *
     * @param rating The rating to be deleted.
     */
    public void deleteRating(Rating rating);

    /**
     * Get all the ratings.
     *
     * @return A list of all the ratings in the database.
     */
    public List<Rating> getAllRating();
    
    /**
     * Get a rating by its id.
     *
     * @param id The id of the rating to be retrieved.
     * @return A Rating object.
     */
    public Rating getRatingById(Integer id);
}
