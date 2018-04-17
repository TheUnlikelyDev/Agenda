package com.theunlikelydev.agenda;

import android.app.Activity;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeScreen extends Activity {

    String TAG = this.getClass().getSimpleName();

    private TextView agendaTitle;


    private static final int MAX_TASKS = 20;
    private ListView taskListView;
    private TaskAdapter taskAdapter;

    private String filesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_act_layout);


        filesDir = getFilesDir().toString();
        Log.d(TAG, "onCreate: " + filesDir);



        this.agendaTitle = findViewById(R.id.daily_agenda_title);


        Button createTask = findViewById(R.id.create_task_button);
        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    doShowCreateTaskDialog(view);


                } catch (ToManyTasksException exception) {
                    showCannotCreateNewTaskMessage();
                }
            }
        });

        ArrayList<Task> list = new ArrayList<Task>();

        try {
           list  = loadTaskListFromFile();
       }catch(IOException e){
              e.printStackTrace();
            Log.d(TAG, "onCreate: io");
        }
        catch(ClassNotFoundException e){
            Log.d(TAG, "onCreate: cnf");
        }




        taskListView = findViewById(R.id.task_list);
        taskListView.setDividerHeight(0);
        taskListView.setDividerHeight(10);

        if(list == null){
            Log.d(TAG, "onCreate: null");
        }

        this.taskAdapter = new TaskAdapter(list,this);
        taskListView.setAdapter(taskAdapter);
       // taskAdapter.notifyDataSetChanged();

        doOverflowCheck();
        this.agendaTitle.setText(getDateTime());


    }


    public void doOverflowCheck(){
        String previousDate = "";
        try {
           previousDate = loadPreviousDate();
        }catch(Exception e){}

        if(!previousDate.equals(getDateTime()))
        {
            taskAdapter.overflowAllTasks();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            writeTaskListToFile();
            Log.d(TAG, "onDestroy: wrtten");
        }catch(IOException e){}

        try {
            writeDateToFile();
            Log.d(TAG, "onDestroy: wrtten2");
        }catch(IOException e){
            Log.d(TAG, "onDestroy: wrt task failed2");
        }



    }


    private void writeDateToFile() throws IOException{

        FileOutputStream fileOut = new FileOutputStream(filesDir +"/date");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(getDateTime());
        out.close();
        fileOut.close();


    }

    private String loadPreviousDate() throws IOException,ClassNotFoundException{

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

    private ArrayList<Task> loadTaskListFromFile() throws IOException,ClassNotFoundException{
        File listFile = new File(filesDir + "/tasks_list");

        if(listFile.exists()){
            Log.d(TAG, "loadTaskListFromFile: in listFIleexists");
            FileInputStream fileIn = new FileInputStream(listFile);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            ArrayList<Task> list = (ArrayList<Task>) in.readObject();
            if(list == null) {
                Log.d(TAG, "loadTaskListFromFile: null");
            }
            in.close();
            fileIn.close();

            return list;
        }else{
            return new ArrayList<Task>();
        }


    }

    private void writeTaskListToFile() throws IOException{

        FileOutputStream fileOut = new FileOutputStream(filesDir +"/tasks_list");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(taskAdapter.getList());
        out.close();
        fileOut.close();
        Log.d(TAG, "writeTaskListToFile: end");

    }

    public void doShowCreateTaskDialog(View view) throws ToManyTasksException{
        if(taskAdapter.getCount() < MAX_TASKS) {
            new CreateTaskDialog().show(getFragmentManager(), "create_task");
        }else{
            throw  new ToManyTasksException();
        }
    }

    public void addTask(String taskTitle,String taskBody) {

        taskAdapter.add(new Task(taskTitle,taskBody));
        taskAdapter.notifyDataSetChanged();

    }

    public void showCannotCreateNewTaskMessage(){
        Toast.makeText(this,R.string.to_many_tasks_message,Toast.LENGTH_SHORT).show();
    }



    private String getDateTime() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);

    }



}
