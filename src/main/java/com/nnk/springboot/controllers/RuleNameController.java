package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.IRuleNameService;
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
public class RuleNameController {
    // TODO: Inject RuleName service
    final Logger logger = LoggerFactory.getLogger(RuleNameController.class);
    
    @Autowired
    private IRuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        // TODO: find all RuleName, add to model
        logger.debug("RuleName List page");
        model.addAttribute("ruleName", ruleNameService.getAllRuleName());
        logger.info("ruleName's List getted");
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        logger.debug("Show RuleName Form");
        return "ruleName/add";    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return RuleName list
        logger.debug("Add a RuleName");
        if (!result.hasErrors()) {
            try {
                ruleNameService.saveRuleName(ruleName);
                logger.info("RuleName has been saved");
                return "redirect:/ruleName/list";
            } catch (Exception e) {
                model.addAttribute("errorMsg", e.getMessage());
                logger.error("Error : " + e.getMessage());
                return "error";
            }
        }
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get RuleName by Id and to model then show to the form
        logger.debug("Show RuleName's Form");
        try {
            model.addAttribute("ruleName", ruleNameService.getRuleNameById(id));
            logger.info("RuleName has been getting");
            return "ruleName/update";
        } catch (Exception e) {
            logger.error("Error : " + e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "redirect:/ruleName/list";
        }
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update RuleName and return RuleName list
        logger.debug("Update RuleName");
        if (!result.hasErrors()) {
            try {
                ruleNameService.saveRuleName(ruleName);
                logger.info("RuleName has been updated");
                return "redirect:/ruleName/list";
            } catch (Exception e) {
                logger.error("Error : " + e.getMessage());
                model.addAttribute("errorMsg", e.getMessage());
                return "error";
            }
        }
        return "ruleName/update";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        // TODO: Find RuleName by Id and delete the RuleName, return to Rule list
        logger.debug("Delete ruleName by id");
        try {
            RuleName ruleName = ruleNameService.getRuleNameById(id);
            ruleNameService.deleteRuleName(ruleName);
            logger.info("RuleName has been deleted");
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }
}
