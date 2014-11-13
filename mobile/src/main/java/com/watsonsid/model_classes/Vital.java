package com.watsonsid.model_classes;

import java.util.Date;
import java.util.Map;

/**
 * Created by Daniel on 11/13/2014.
 */
public abstract class Vital {
    Vital(Patient _patient)
    {
        patient = _patient;
        updateData();
    }
    abstract void drawGraph();
    abstract void updateData();
    private Map<Date, Double> dataPoints;
    Patient patient;
}
