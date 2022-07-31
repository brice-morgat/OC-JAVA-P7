package com.nnk.springboot.unit.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.impl.RuleNameServiceImpl;
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
public class RuleNameServiceTest {
    @InjectMocks
    private RuleNameServiceImpl ruleNameService;

    @Mock
    private RuleNameRepository ruleNameRepository;

    @Test
    public void findAllRuleNameTest() {
        //Given
        List<RuleName> ruleNames = new ArrayList();
        RuleName ruleNameOne = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        RuleName ruleNameTwo = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleNames.add(ruleNameOne);
        ruleNames.add(ruleNameTwo);
        //When
        when(ruleNameRepository.findAll()).thenReturn(ruleNames);
        //Then
        assertEquals(ruleNameService.getAllRuleName(), ruleNames);
    }

    @Test
    public void saveRuleNameTest() {
        //Given
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        //When
        when(ruleNameRepository.save(any())).thenReturn(ruleName);
        //Then
        assertEquals(ruleNameService.saveRuleName(ruleName), ruleName);
    }

    @Test
    public void saveRuleNameWithoutAllArgErrorTest() {
        //Given
        RuleName ruleName = new RuleName();
        //Then
        assertThrows(InvalidInputException.class, () -> ruleNameService.saveRuleName(ruleName));
    }

    @Test
    public void saveRuleNameWithoutNameErrorTest() {
        //Given
        RuleName ruleName = new RuleName(null, "Desc", "json","Template", "sql","sql");
        //Then
        assertThrows(InvalidInputException.class, () -> ruleNameService.saveRuleName(ruleName));
    }

    @Test
    public void deleteRuleNameTest() {
        //Then
        assertDoesNotThrow(() -> ruleNameService.deleteRuleName(new RuleName()));
    }

    @Test
    public void deleteRuleNameNullErrorTest() {
        //Then
        assertThrows(Exception.class,() -> ruleNameService.deleteRuleName(null));
    }

    @Test
    public void getRuleNameByIdTest() {
        //Given
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        //When
        when(ruleNameRepository.getById(anyInt())).thenReturn(ruleName);
        //Then
        assertEquals(ruleNameService.getRuleNameById(1), ruleName);
    }
}
