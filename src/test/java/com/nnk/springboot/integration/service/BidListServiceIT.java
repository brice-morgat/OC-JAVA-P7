package com.nnk.springboot.integration.service;


import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.BidServiceImpl;
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
public class BidListServiceIT {

    @Autowired
    private BidServiceImpl bidService;

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
    public void findAllBidListTest() {
        //Given
        List<BidList> bidLists = new ArrayList();
        BidList bidListOne = new BidList("Account1", "Type1", 1d);
        BidList bidListTwo = new BidList("Account2", "Type2", 2d);
        bidLists.add(bidListOne);
        bidLists.add(bidListTwo);
        //Then
        assertDoesNotThrow(() ->bidService.getAllBidList());
    }

    @Test
    public void saveBidListTest() {
        //Given
        BidList bidList = new BidList("Account1", "Type1", 1d);
        //Then
        assertEquals(bidService.saveBidList(bidList), bidList);
    }

    @Test
    public void saveBidListWithoutAllArgErrorTest() {
        //Given
        BidList bidList = new BidList();
        //Then
        assertThrows(InvalidInputException.class, () -> bidService.saveBidList(bidList));
    }

    @Test
    public void saveBidListWithoutAccountErrorTest() {
        //Given
        BidList bidList = new BidList(null, "Type1", 1d);
        //Then
        assertThrows(InvalidInputException.class, () -> bidService.saveBidList(bidList));
    }

    @Test
    public void saveBidListWithoutTypeErrorTest() {
        //Given
        BidList bidList = new BidList("Account", null, 1d);
        //Then
        assertThrows(InvalidInputException.class, () -> bidService.saveBidList(bidList));
    }

    @Test
    public void deleteBidListTest() {
        //Then
        assertDoesNotThrow(() -> bidService.deleteBidList(new BidList()));
    }

    @Test
    public void deleteBidListNullErrorTest() {
        //Then
        assertThrows(Exception.class,() -> bidService.deleteBidList(null));
    }

    @Test
    public void getBidListByIdTest() {
        //Given
        BidList bidList = new BidList("account", "type", 1d);
        //Then
       assertDoesNotThrow(() -> bidService.getBidListById(1));
    }
}
