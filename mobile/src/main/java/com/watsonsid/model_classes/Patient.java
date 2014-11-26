package com.watsonsid.model_classes;

/**
 * Created by Daniel on 11/13/2014.
 */
public class Patient {
    public Patient(String _name, String _id, String _status)
    {
        name = _name;
        id = _id;
        status = _status;
    }
    public String name;
    public String id;
    public String status;
}
