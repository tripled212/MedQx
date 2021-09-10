package com.example.medqx.Locator;

public class MedModel {
    public String uid, hrsMin, interval, medType, uname, num, currentDate,currentTime;

    public MedModel(){

    }


    public MedModel(String uid,  String hrsMin, String interval, String medType, String uname,
                    String num, String currentDate, String currentTime){
        this.uid =uid;
        this.hrsMin =hrsMin;
        this.interval =interval;
        this.medType =medType;
        this.uname =uname;
        this.num =num;
        this.currentDate=currentDate;
        this.currentTime=currentTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHrsMin() {
        return hrsMin;
    }

    public void setHrsMin(String hrsMin) {
        this.hrsMin = hrsMin;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
