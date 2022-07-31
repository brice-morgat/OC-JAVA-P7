package com.nnk.springboot.integration.service;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.RuleNameServiceImpl;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("prod")
public class RuleNameServiceIT {
    @Autowired
    private RuleNameServiceImpl ruleNameService;

    @Autowired
    private Environment env;

    @BeforeEach
    public void init(){
        executeSql("src/main/resources/poseidon-skeleton_test_db.sql");
    }

    private void executeSql(String sqlFilePath) {
        final class SqlExecuter extends SQLExec {
            public SqlExecuter() {
                Project project = new Project();
                project.init();
                setProject(project);
                setTaskType("sql");
                setTaskName("sql");
            }
        }

        SqlExecuter executer = new SqlExecuter();
        executer.setSrc(new File(sqlFilePath));
        executer.setDriver("com.mysql.jdbc.Driver");
        executer.setPassword(env.getProperty("spring.datasource.password"));
        executer.setUserid(env.getProperty("spring.datasource.username"));
        executer.setUrl(env.getProperty("spring.datasource.url"));
        executer.execute();
    }


    @Test
    public void findAllRuleNameTest() {
        //Given
        List<RuleName> ruleNames = new ArrayList();
        RuleName ruleNameOne = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        RuleName ruleNameTwo = new RuleName("Name", "Desc", "json","Template", "sql","sql");
        ruleNames.add(ruleNameOne);
        ruleNames.add(ruleNameTwo);
        //Then
        assertDoesNotThrow(() ->ruleNameService.getAllRuleName());
    }

    @Test
    public void saveRuleNameTest() {
        //Given
        RuleName ruleName = new RuleName("Name", "Desc", "json","Template", "sql","sql");
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
        //Then
        assertDoesNotThrow(() -> ruleNameService.getRuleNameById(1));
    }
}
