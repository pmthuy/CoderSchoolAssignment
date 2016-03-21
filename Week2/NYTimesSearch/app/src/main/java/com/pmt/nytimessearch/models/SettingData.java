package com.pmt.nytimessearch.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by thuypm on 20/03/2016.
 */
public class SettingData implements Serializable {

    public Calendar beginDate;
    public Calendar endDate;
    public String sortType = "";
    public ArrayList<String> newsDeskValues = new ArrayList<>();

    public SettingData(){

    }

}
