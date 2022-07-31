package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;

import java.util.List;

public interface IRuleNameService {

    /**
     * Save a rule.
     *
     * @param rule The rule object to be saved.
     * @return RuleName
     */
    public RuleName saveRuleName(RuleName rule);

    /**
     * Delete the given rule from the database.
     *
     * @param rule The rule to be deleted.
     */
    public void deleteRuleName(RuleName rule);

    /**
     * Get all the rules.
     *
     * @return A list of all the rules in the database.
     */
    public List<RuleName> getAllRuleName();

    /**
     * Get a rule by its id.
     *
     * @param id The id of the rule to be retrieved.
     * @return A RuleName object.
     */
    public RuleName getRuleNameById(Integer id);
}
