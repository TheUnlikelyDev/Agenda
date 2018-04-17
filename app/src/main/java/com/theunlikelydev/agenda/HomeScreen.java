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
           list  = new StorageManager(getFilesDir().toString()).loadTaskListFromFile();
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
           previousDate = new StorageManager(getFilesDir().toString()).loadPreviousDate();
        }catch(Exception e){}

        if(!previousDate.equals(getDateTime()))
        {
            taskAdapter.overflowAllTasks();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        StorageManager manager = new StorageManager(getFilesDir().toString());
        try {
            manager.writeTaskListToFile(taskAdapter);
            Log.d(TAG, "onDestroy: wrtten");
        }catch(IOException e){}

        try {
            manager.writeDateToFile(getDateTime().toString());
            Log.d(TAG, "onDestroy: wrtten2");
        }catch(IOException e){
            Log.d(TAG, "onDestroy: wrt task failed2");
        }



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
