package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.IRuleNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RuleNameServiceImpl implements IRuleNameService {
    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Override
    public RuleName saveRuleName(RuleName ruleName) {
        Objects.requireNonNull(ruleName);
        if (ruleName.getName() == null) {
            throw new InvalidInputException("Rule name must not be null");
        }
        return ruleNameRepository.save(ruleName);
    }

    @Override
    public void deleteRuleName(RuleName ruleName) {
        Objects.requireNonNull(ruleName);
        ruleNameRepository.delete(ruleName);
    }

    @Override
    public List<RuleName> getAllRuleName() {
        return ruleNameRepository.findAll();
    }

    @Override
    public RuleName getRuleNameById(Integer id) {
        RuleName ruleName = ruleNameRepository.getById(id);
        if (ruleName != null) {
            return ruleName;
        }
        throw new InvalidInputException("RuleName not Found");
    }
}
