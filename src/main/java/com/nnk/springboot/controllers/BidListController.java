package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.IBidService;
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
public class BidListController {

    final Logger logger = LoggerFactory.getLogger(BidListController.class);

    @Autowired
    private IBidService bidService;

    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        logger.debug("BidList List page");
        model.addAttribute("bidList", bidService.getAllBidList());
        logger.info("BidList's List getted");
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        logger.debug("Show BidList Form");
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        logger.debug("Add a BidList");
        if (!result.hasErrors()) {
            try {
                bidService.saveBidList(bid);
                logger.info("BidList has been saved");
                return "redirect:/bidList/list";
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
                logger.error("Error : " + e.getMessage());
                return "error";
            }
        }
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.debug("Show BidList's Form");
        try {
            model.addAttribute("bidList", bidService.getBidListById(id));
            logger.info("BidList has been getting");
            return "bidList/update";
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "redirect:/bidList/list";
        }
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        logger.debug("Update BidList");
        if (!result.hasErrors()) {
            try {
                bidService.saveBidList(bidList);
                logger.info("BidList has been updated");
                return "redirect:/bidList/list";
            } catch (Exception e) {
                logger.error("Error : " + e.getMessage());
                model.addAttribute("errorMsg", e.getMessage());
                return "error";
            }
        }
        return "bidList/update";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        logger.debug("Delete BidList by id");
        try {
            BidList bidList = bidService.getBidListById(id);
            bidService.deleteBidList(bidList);
            logger.info("BidList has been deleted");
            return "redirect:/bidList/list";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }
}
