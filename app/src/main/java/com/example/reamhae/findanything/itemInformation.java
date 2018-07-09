package com.example.reamhae.findanything;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.net.HttpURLConnection;
import java.util.Date;

/**
 * Created by reamhae on 2017-05-06.
 */

public class itemInformation {
    private String ID;
    private String take_place;
    private String name;
    private String date;
    private String position;
    private Drawable icon;
    private HttpURLConnection exitCode;

    itemInformation(){
        ID="";
        take_place="내용 없음";
        name="내용 없음";
        date="내용 없음";
        position="내용 없음";
        icon= null;
        exitCode=null;
    }



    public String getID() {
        return ID;
    }

    public String getTake_place() {
        return take_place;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getPosition() {
        return position;
    }

    public Drawable getIcon() {
        return icon;
    }

    public HttpURLConnection getExitCode() {
        return exitCode;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTake_place(String take_place) {
        this.take_place = take_place;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setExitCode(HttpURLConnection exitCode) {
        this.exitCode = exitCode;
    }
}
