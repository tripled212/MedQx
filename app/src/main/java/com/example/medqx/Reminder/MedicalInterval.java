package com.example.medqx.Reminder;

public class MedicalInterval {

    public String id;
    public String interval;

    public MedicalInterval(String interval, String id){
        this.interval = interval;
        this.id = id;
    }

    public String getId(){
        return id;
    }

}
