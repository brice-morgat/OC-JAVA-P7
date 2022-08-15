package com.nnk.springboot.integration.service;


import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.TradeServiceImpl;
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
public class TradeServiceIT {
    @Autowired
    private TradeServiceImpl tradeService;

    @Autowired
    private Environment env;

    @BeforeEach
    public void init() {
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
    public void findAllTradeTest() {
        //Given
        List<Trade> trades = new ArrayList();
        Trade tradeOne = new Trade("Name", "");
        Trade tradeTwo = new Trade("Name", "");
        trades.add(tradeOne);
        trades.add(tradeTwo);
        //Then
        assertDoesNotThrow(() -> tradeService.getAllTrade());
    }

    @Test
    public void saveTradeTest() {
        //Given
        Trade trade = new Trade("Name", "");
        //Then
        assertEquals(tradeService.saveTrade(trade), trade);
    }

    @Test
    public void saveTradeWithoutAllArgErrorTest() {
        //Given
        Trade trade = new Trade(null, "");
        //Then
        assertThrows(InvalidInputException.class, () -> tradeService.saveTrade(trade));
    }


    @Test
    public void deleteTradeTest() {
        //Then
        assertDoesNotThrow(() -> tradeService.deleteTrade(new Trade("Name", "")));
    }

    @Test
    public void deleteTradeNullErrorTest() {
        //Then
        assertThrows(Exception.class, () -> tradeService.deleteTrade(null));
    }

    @Test
    public void getTradeByIdTest() {
        //Given
        Trade trade = new Trade("Name", "");
        //Then
        assertDoesNotThrow(() -> tradeService.getTradeById(1));
    }
}
