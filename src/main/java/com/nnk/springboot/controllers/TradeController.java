package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.ITradeService;
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
public class TradeController {
    // TODO: Inject Trade service
    final Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private ITradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        // TODO: find all Trade, add to model
        logger.debug("Trade List page");
        model.addAttribute("trade", tradeService.getAllTrade());
        logger.info("trade's List getted");
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addRuleForm(Trade bid) {
        logger.debug("Show Trade Form");
        return "trade/add";    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Trade list
        logger.debug("Add a Trade");
        if (!result.hasErrors()) {
            try {
                tradeService.saveTrade(trade);
                logger.info("Trade has been saved");
                return "redirect:/trade/list";
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
                logger.error("Error : " + e.getMessage());
                return "error";
            }
        }
        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Trade by Id and to model then show to the form
        logger.debug("Show Trade's Form");
        try {
            model.addAttribute("trade", tradeService.getTradeById(id));
            logger.info("Trade has been getting");
            return "trade/update";
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "redirect:/trade/list";
        }
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                                 BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Trade and return Trade list
        logger.debug("Update Trade");
        if (!result.hasErrors()) {
            try {
                tradeService.saveTrade(trade);
                logger.info("Trade has been updated");
                return "redirect:/trade/list";
            } catch (Exception e) {
                logger.error("Error : " + e.getMessage());
                model.addAttribute("errorMsg", e.getMessage());
                return "error";
            }
        }
        return "trade/update";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Trade by Id and delete the Trade, return to Rule list
        logger.debug("Delete trade by id");
        try {
            Trade trade = tradeService.getTradeById(id);
            tradeService.deleteTrade(trade);
            logger.info("Trade has been deleted");
            return "redirect:/trade/list";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }
}
