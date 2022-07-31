package com.nnk.springboot.integration.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.CurvePointServiceImpl;
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
public class CurvePointServiceIT {

    @Autowired
    private CurvePointServiceImpl curvePointService;

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
    public void findAllCurvePointTest() {
        //Given
        List<CurvePoint> curvePoints = new ArrayList();
        CurvePoint curvePointOne = new CurvePoint(1, 20d, 1d);
        CurvePoint curvePointTwo = new CurvePoint(2, 10d, 2d);
        curvePoints.add(curvePointOne);
        curvePoints.add(curvePointTwo);
        //Then
        assertDoesNotThrow(() -> curvePointService.getAllCurvePoint());
    }

    @Test
    public void saveCurvePointTest() {
        //Given
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);
        //Then
        assertEquals(curvePointService.saveCurvePoint(curvePoint), curvePoint);
    }

    @Test
    public void saveCurvePointWithoutAllArgErrorTest() {
        //Given
        CurvePoint curvePoint = new CurvePoint();
        //Then
        assertThrows(InvalidInputException.class, () -> curvePointService.saveCurvePoint(curvePoint));
    }

    @Test
    public void saveCurvePointWithoutCurveIdErrorTest() {
        //Given
        CurvePoint curvePoint = new CurvePoint(null, 20d, 1d);
        //Then
        assertThrows(InvalidInputException.class, () -> curvePointService.saveCurvePoint(curvePoint));
    }

    @Test
    public void deleteCurvePointTest() {
        //Then
        assertDoesNotThrow(() -> curvePointService.deleteCurvePoint(new CurvePoint()));
    }

    @Test
    public void deleteCurvePointNullErrorTest() {
        //Then
        assertThrows(Exception.class,() -> curvePointService.deleteCurvePoint(null));
    }

    @Test
    public void getCurvePointByIdTest() {
        //Given
        CurvePoint curvePoint = new CurvePoint(2, 1d, 1d);
        //Then
        assertDoesNotThrow(() -> curvePointService.getCurvePointById(1));
    }
}
