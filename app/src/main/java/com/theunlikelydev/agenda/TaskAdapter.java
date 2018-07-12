package com.theunlikelydev.agenda;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;


public class TaskAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<Task> list = new ArrayList<Task>();
    private Context context;


    public TaskAdapter(ArrayList<Task> list, Context context){
        this.context= context;
        this.list = list;
    }

    public void add(Task task){
        list.add(task);

    }


    @Override
    public int getCount() {
        return list.size();
    }

    public ArrayList<Task> getList(){
        return this.list;
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int id) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.task_list_item, null);
        }

        CardView card = view.findViewById(R.id.card_view);
        TextView title = view.findViewById(R.id.task_cell_title);


        setTextFont(title);



        title.setText(list.get(position).getTitle());

        int procrastColor = list.get(position).getOverflowColorAsInt();
        title.setBackgroundColor(procrastColor);
        card.setCardBackgroundColor(procrastColor);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HomeScreen homeScreen =(HomeScreen) context;
                TaskViewDialog viewDialog = new TaskViewDialog();
                Bundle args = new Bundle();
                String[] info = new String[]{list.get(position).getTitle(),
                        list.get(position).getDescription(),
                Integer.toString(list.get(position).getOverflowCount())};
                args.putStringArray("task_info",info);
                viewDialog.setArguments(args);
                viewDialog.show(homeScreen.getFragmentManager(), "view task");

            }
        });

        Button taskCompleted = view.findViewById(R.id.task_complete_button);
        taskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeTask(position);
                notifyDataSetChanged();
            }
        });




        return view;

    }

    private void setTextFont(TextView title){

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/carterone.ttf");
        title.setTypeface(face);

    }

    public void overflowAllTasks(){


        for(Task task : list)
        {
            task.incrementOverflow();
        }
    }

    public void removeTask(int position){

        list.remove(position);

    }
}
