package com.nnk.springboot.integration.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.services.impl.RatingServiceImpl;
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
public class RatingServiceIT {

    @Autowired
    private RatingServiceImpl ratingService;

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
    public void findAllRatingTest() {
        //Given
        List<Rating> ratings = new ArrayList();
        Rating ratingOne =new Rating("Moody", "SandPRating", "FitchRating", 1);
        Rating ratingTwo = new Rating("Moody", "SandPRating", "FitchRating", 1);
        ratings.add(ratingOne);
        ratings.add(ratingTwo);
        //Then
        assertDoesNotThrow(() -> ratingService.getAllRating());
    }

    @Test
    public void saveRatingTest() {
        //Given
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        //Then
        assertEquals(ratingService.saveRating(rating), rating);
    }

//    @Test
//    public void saveRatingWithoutAllArgErrorTest() {
//        //Given
//        Rating rating = new Rating();
//        //Then
//        assertThrows(InvalidInputException.class, () -> ratingService.saveRating(rating));
//    }
//
//    @Test
//    public void saveRatingWithoutArgsErrorTest() {
//        //Given
//        Rating rating = new Rating(null, 20d, 1d);
//        //Then
//        assertThrows(InvalidInputException.class, () -> ratingService.saveRating(rating));
//    }

    @Test
    public void deleteRatingTest() {
        //Then
        assertDoesNotThrow(() -> ratingService.deleteRating(new Rating()));
    }

    @Test
    public void deleteRatingNullErrorTest() {
        //Then
        assertThrows(Exception.class,() -> ratingService.deleteRating(null));
    }

    @Test
    public void getRatingByIdTest() {
        //Given
        Rating rating = new Rating("Moody", "SandPRating", "FitchRating", 1);
        //Then
        assertDoesNotThrow(() -> ratingService.getRatingById(1));
    }
}
