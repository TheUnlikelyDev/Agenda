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

        this.agendaTitle = findViewById(R.id.daily_agenda_title);

        buildCreateTaskButton();


        ArrayList<Task> list = loadTaskListArray();
        buildTaskListView(list);
        doOverflowCheck();
        this.agendaTitle.setText(getDateTime());


    }

    private void buildCreateTaskButton(){

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

    }

    private ArrayList<Task> loadTaskListArray(){
        ArrayList<Task> list = null;
        try {
            list  = new StorageManager(getFilesDir().toString()).loadTaskListFromFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        catch(ClassNotFoundException e){

        }

        return list;

    }

    private void buildTaskListView(ArrayList<Task> list){
        taskListView = findViewById(R.id.task_list);
        taskListView.setDividerHeight(0);
        taskListView.setDividerHeight(10);
        this.taskAdapter = new TaskAdapter(list,this);
        taskListView.setAdapter(taskAdapter);

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

        }catch(IOException e){}

        try {
            manager.writeDateToFile(getDateTime().toString());

        }catch(IOException e){

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

    private void showCannotCreateNewTaskMessage(){
        Toast.makeText(this,R.string.to_many_tasks_message,Toast.LENGTH_SHORT).show();
    }



    private String getDateTime() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);

    }



}
