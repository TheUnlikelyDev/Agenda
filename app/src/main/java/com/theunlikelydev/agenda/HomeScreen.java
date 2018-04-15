package com.theunlikelydev.agenda;

import android.app.Activity;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeScreen extends Activity {

    private TextView agendaTitle;

    private Task[] tasks;
    private int tasksSize;
    private static final int MAX_TASKS = 1;
    private ListView taskListView;
    private ArrayAdapter<Task> taskAdapter;

    public void doShowCreateTaskDialog(View view) throws ToManyTasksException{
        if(tasksSize < MAX_TASKS) {
            new CreateTaskDialog().show(getFragmentManager(), "create_task");
        }else{
            throw  new ToManyTasksException();
        }
    }

    public void addTask(String taskTitle,String taskBody) {

        tasks[tasksSize] = new Task(taskTitle, taskBody);
        tasksSize++;
        taskAdapter.notifyDataSetChanged();



    }

    public void showCannotCreateNewTaskMessage(){
        Toast.makeText(this,R.string.to_many_tasks_message,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_act_layout);

        this.agendaTitle = findViewById(R.id.daily_agenda_title);
        setAgendaTitle();

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

        this.tasks = new Task[MAX_TASKS];


        taskListView = findViewById(R.id.task_list);
        taskListView.setDividerHeight(0);
        taskListView.setDividerHeight(10);


        this.taskAdapter = new ArrayAdapter<Task>(this,R.layout.task_list_item,tasks);
        taskListView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();



    }

    private void setAgendaTitle(){

        String currentDate = getDateTime();
        String text = getResources().getString(R.string.agenda_title);
        String title =text + " " + currentDate;
        agendaTitle.setText(title);

    }

    private String getDateTime() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);

    }



}
