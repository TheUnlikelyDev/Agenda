package com.theunlikelydev.agenda;

import android.graphics.Color;

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

    public int getOverflowColorAsInt(){




        int color = 0;
        if(overflowCount < 1){
          color = Color.parseColor("#ffffff");

        }
        else if(overflowCount == 1){
            color = Color.parseColor("#ffff00");
        }
        else if(overflowCount == 2){
             color = Color.parseColor("#ff9933");
        }
        else{
            color = Color.parseColor("#ff0000");
        }

        return  color;

    }
}
