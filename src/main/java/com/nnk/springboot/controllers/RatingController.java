package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.IRatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class RatingController {
    final Logger logger = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private IRatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        logger.debug("Rating List page");
        model.addAttribute("rating", ratingService.getAllRating());
        logger.info("rating's List getted");
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        logger.debug("Show Rating Form");
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        logger.debug("Add a Rating");
        if (!result.hasErrors()) {
            try {
                ratingService.saveRating(rating);
                logger.info("Rating has been saved");
                return "redirect:/rating/list";
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
                logger.error("Error : " + e.getMessage());
                return "error";
            }
        }
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.debug("Show Rating's Form");
        try {
            model.addAttribute("rating", ratingService.getRatingById(id));
            logger.info("Rating has been getting");
            return "rating/update";
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "redirect:/rating/list";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        logger.debug("Update Rating");
        if (!result.hasErrors()) {
            try {
                ratingService.saveRating(rating);
                logger.info("Rating has been updated");
                return "redirect:/rating/list";
            } catch (Exception e) {
                logger.error("Error : " + e.getMessage());
                model.addAttribute("errorMsg", e.getMessage());
                return "error";
            }
        }
        return "rating/update";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        logger.debug("Delete rating by id");
        try {
            Rating rating = ratingService.getRatingById(id);
            ratingService.deleteRating(rating);
            logger.info("Rating has been deleted");
            return "redirect:/rating/list";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }
}
