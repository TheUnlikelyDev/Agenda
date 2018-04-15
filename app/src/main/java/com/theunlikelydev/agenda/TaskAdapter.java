package com.theunlikelydev.agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsmfo_000 on 16/04/2018.
 */
///
public class TaskAdapter extends BaseAdapter implements ListAdapter {

    private List<Task> list = new ArrayList<Task>();
    private Context context;
    private String dud;

    public TaskAdapter(List<Task> list, Context context){
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

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.task_list_item, null);
        }

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

    public void removeTask(int position){

        list.remove(position);

    }
}
