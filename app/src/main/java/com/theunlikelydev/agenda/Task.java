package com.theunlikelydev.agenda;

import java.io.Serializable;

/**
 * Created by wsm18 on 15/04/2018.
 */

public class Task implements Serializable {

    private String title;
    private String description;
    private int overflowCount;

    public Task(String title,String description){
        this.title = title;
        this.description = description;
        this.overflowCount =0;
    }
    public String getTitle(){
        return title;
    }

    public void incrementOverflow(){
        overflowCount++;
    }
    public int getOverflowCount(){
        return this.overflowCount;
    }

    public String getDescription(){
        return description;
    }

    public String toString(){

        return "title: " + title + ",description: " + description + ",overflow: " + overflowCount;
    }
}
