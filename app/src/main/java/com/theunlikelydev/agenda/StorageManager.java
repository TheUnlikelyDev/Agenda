package com.theunlikelydev.agenda;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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


}
