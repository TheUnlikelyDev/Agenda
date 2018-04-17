package com.theunlikelydev.agenda;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by wsmfo_000 on 17/04/2018.
 */

public class StorageManager {


    public final String filesDir;

    public StorageManager(String filesDir){
        this.filesDir = filesDir;
    }

    public void writeDateToFile(String currentDate) throws IOException {

        FileOutputStream fileOut = new FileOutputStream(filesDir +"/date");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(currentDate);
        out.close();
        fileOut.close();


    }

    public void writeTaskListToFile(TaskAdapter taskAdapter) throws IOException{

        FileOutputStream fileOut = new FileOutputStream(filesDir +"/tasks_list");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(taskAdapter.getList());
        out.close();
        fileOut.close();


    }

    public String loadPreviousDate() throws IOException,ClassNotFoundException{

        File dateFile = new File(filesDir + "/date");

        if(dateFile.exists()) {

            FileInputStream fileIn = new FileInputStream(dateFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            String previousDate =(String) in.readObject();

            in.close();
            fileIn.close();
            return previousDate;
        }else{
            return "";
        }
    }

    public ArrayList<Task> loadTaskListFromFile() throws IOException,ClassNotFoundException{
        File listFile = new File(filesDir + "/tasks_list");

        if(listFile.exists()){

            FileInputStream fileIn = new FileInputStream(listFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<Task> list = (ArrayList<Task>) in.readObject();

            in.close();
            fileIn.close();

            return list;
        }else{
            return new ArrayList<Task>();
        }


    }


}
