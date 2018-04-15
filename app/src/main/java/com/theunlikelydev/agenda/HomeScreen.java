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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeScreen extends Activity {

    private TextView agendaTitle;

    private List<Task> tasks;
    private static final int MAX_TASKS = 20;
    private ListView taskListView;
    private ArrayAdapter<Task> taskAdapter;

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

        this.tasks = new ArrayList<Task>();


        taskListView = findViewById(R.id.task_list);
        taskListView.setDividerHeight(0);
        taskListView.setDividerHeight(10);


        this.taskAdapter = new ArrayAdapter<Task>(this,R.layout.task_list_item,tasks);
        taskListView.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();



    }


    public void doShowCreateTaskDialog(View view) throws ToManyTasksException{
        if(tasks.size() < MAX_TASKS) {
            new CreateTaskDialog().show(getFragmentManager(), "create_task");
        }else{
            throw  new ToManyTasksException();
        }
    }

    public void addTask(String taskTitle,String taskBody) {

        tasks.add(new Task(taskTitle,taskBody));
        taskAdapter.notifyDataSetChanged();

    }

    public void showCannotCreateNewTaskMessage(){
        Toast.makeText(this,R.string.to_many_tasks_message,Toast.LENGTH_SHORT).show();
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
