package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.ICurvePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CurvePointServiceImpl implements ICurvePointService {

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Override
    public CurvePoint saveCurvePoint(CurvePoint curvePoint) {
        Objects.requireNonNull(curvePoint);
        if (curvePoint.getCurveId() == null) {
            throw new InvalidInputException("Curve Id must not be null");
        }
        return curvePointRepository.save(curvePoint);
    }

    @Override
    public void deleteCurvePoint(CurvePoint curvePoint) {
        Objects.requireNonNull(curvePoint);
        curvePointRepository.delete(curvePoint);
    }

    @Override
    public List<CurvePoint> getAllCurvePoint() {
        return curvePointRepository.findAll();
    }

    @Override
    public CurvePoint getCurvePointById(Integer id) {
        return curvePointRepository.getById(id);
    }
}
