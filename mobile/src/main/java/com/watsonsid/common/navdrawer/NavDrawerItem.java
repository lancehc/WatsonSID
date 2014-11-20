package com.watsonsid.common.navdrawer;

/**
 * Created by lance on 11/12/14.
 */
public interface NavDrawerItem {
    public int getId();
    public String getLabel();
    public int getType();
    public boolean isEnabled();
    public boolean updateActionBarTitle();
}
