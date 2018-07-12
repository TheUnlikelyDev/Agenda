package com.theunlikelydev.agenda;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
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
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeScreen extends Activity {

    String TAG = this.getClass().getSimpleName();

    private static final int MAX_TASKS = 20;
    private ListView taskListView;
    private TaskAdapter taskAdapter;
    private String filesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_act_layout);

        createActionBar();

        filesDir = getFilesDir().toString();

        buildCreateTaskButton();

        ArrayList<Task> list = loadTaskListArray();
        buildTaskListView(list);
        doOverflowCheck();

        Button taskCreateButton = findViewById(R.id.create_task_button);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/carterone.ttf");
        taskCreateButton.setTypeface(face);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void createActionBar(){

        ActionBar bar = getActionBar();
        String agendaTitleText = getResources().getString(R.string.agenda_title);
        bar.setTitle("   " + agendaTitleText + " " +getDateTime());
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#004488")));

        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        TextView textView = (TextView) findViewById(titleId);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/carterone.ttf");
        textView.setTypeface(face);
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
