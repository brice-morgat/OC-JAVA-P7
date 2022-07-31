package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;

import java.util.List;

public interface ICurvePointService {
    /**
     * Save a CurvePoint object to the database.
     *
     * @param curvePoint The curve point to save.
     * @return The CurvePoint object that was saved.
     */
    public CurvePoint saveCurvePoint(CurvePoint curvePoint);

    /**
     * Deletes a curve point
     *
     * @param curvePoint The curve point to delete.
     */
    public void deleteCurvePoint(CurvePoint curvePoint);

    /**
     * > Get all the curve points in the curve
     *
     * @return A list of all the curve points in the database.
     */
    public List<CurvePoint> getAllCurvePoint();

    /**
     * > Get a curve point by its id
     *
     * @param id The id of the curve point you want to retrieve.
     * @return A CurvePoint object.
     */
    public CurvePoint getCurvePointById(Integer id);
}
