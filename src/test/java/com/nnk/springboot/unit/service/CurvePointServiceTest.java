package com.nnk.springboot.unit.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.impl.CurvePointServiceImpl;
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
public class CurvePointServiceTest {
    @InjectMocks
    private CurvePointServiceImpl curvePointService;

    @Mock
    private CurvePointRepository curvePointRepository;

    @Test
    public void findAllCurvePointTest() {
        //Given
        List<CurvePoint> curvePointList = new ArrayList();
        CurvePoint curvePointOne = new CurvePoint(1, 20d, 1d);
        CurvePoint curvePointTwo = new CurvePoint(2, 20d, 2d);
        curvePointList.add(curvePointOne);
        curvePointList.add(curvePointTwo);
        //When
        when(curvePointRepository.findAll()).thenReturn(curvePointList);
        //Then
        assertEquals(curvePointService.getAllCurvePoint(), curvePointList);
    }

    @Test
    public void saveCurvePointTest() {
        //Given
        CurvePoint curvePoint = new CurvePoint(1, 20d, 1d);

        //When
        when(curvePointRepository.save(any())).thenReturn(curvePoint);
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
        assertThrows(Exception.class,() ->  curvePointService.deleteCurvePoint(null));
    }

    @Test
    public void getCurvePointByIdTest() {
        //Given
        CurvePoint curvePoint = new CurvePoint(1, 10d, 1d);
        //When
        when(curvePointRepository.getById(anyInt())).thenReturn(curvePoint);
        //Then
        assertEquals(curvePointService.getCurvePointById(1), curvePoint);
    }
}
