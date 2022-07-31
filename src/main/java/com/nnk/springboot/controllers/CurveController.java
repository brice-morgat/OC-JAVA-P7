package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.ICurvePointService;
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
public class CurveController {
    final Logger logger = LoggerFactory.getLogger(CurveController.class);

    @Autowired
    private ICurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        logger.debug("CurvePoint List page");
        model.addAttribute("curvePoint", curvePointService.getAllCurvePoint());
        logger.info("CurvePoint's List getted");
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        logger.debug("Show CurvePoint Form");
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        logger.debug("Add a CurvePoint");
        if (!result.hasErrors()) {
            try {
                curvePointService.saveCurvePoint(curvePoint);
                logger.info("CurvePoint has been saved");
                return "redirect:/curvePoint/list";
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
                logger.error("Error : " + e.getMessage());
                return "error";
            }
        }
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.debug("Show CurvePoint's Form");
        try {
            model.addAttribute("curvePoint", curvePointService.getCurvePointById(id));
            logger.info("CurvePoint has been getting");
            return "curvePoint/update";
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "redirect:/curvePoint/list";
        }
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        logger.debug("Update CurvePoint");
        if (!result.hasErrors()) {
            try {
                curvePointService.saveCurvePoint(curvePoint);
                logger.info("CurvePoint has been updated");
                return "redirect:/curvePoint/list";
            } catch (Exception e) {
                logger.error("Error : " + e.getMessage());
                model.addAttribute("errorMsg", e.getMessage());
                return "error";
            }
        }
        return "curvePoint/update";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        logger.debug("Delete curvePoint by id");
        try {
            CurvePoint curvePoint = curvePointService.getCurvePointById(id);
            curvePointService.deleteCurvePoint(curvePoint);
            logger.info("CurvePoint has been deleted");
            return "redirect:/curvePoint/list";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }
}
