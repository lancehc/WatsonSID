package com.watsonsid.common;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.UUID;

/**
 * Created by lance on 11/19/14.
 */
@ParseClassName("Patient")
public class Patient extends ParseObject {
    public String getName() {
        return getString("name");
    }
    public void setName(String name) {
        put("name", name);
    }
    public String getUuidString() {
        return getString("uuid");
    }
    public void setUuidString() {
        put("uuid", UUID.randomUUID().toString());
    }
}
