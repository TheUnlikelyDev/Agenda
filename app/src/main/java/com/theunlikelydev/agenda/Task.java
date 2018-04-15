package com.theunlikelydev.agenda;

/**
 * Created by wsm18 on 15/04/2018.
 */

public class Task {

    private String title;
    private String description;
    public Task(String title,String description){
        this.title = title;
        this.description = description;
    }

    public String toString(){
        return title;
    }
}
