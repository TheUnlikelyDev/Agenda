package com.theunlikelydev.agenda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TaskViewDialog extends android.app.DialogFragment {

    String[] taskInfo;
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.taskInfo = getArguments().getStringArray("task_info");
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_task_view_dialog,null);

        TextView title = view.findViewById(R.id.title_tv);
        TextView description = view.findViewById(R.id.description_tv);
        TextView taskTitle = view.findViewById(R.id.task_title);



        title.setText(taskInfo[0]);
        description.setText(taskInfo[1]);

        Button okBut = view.findViewById(R.id.ok);
        okBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/carterone.ttf");
        okBut.setTypeface(face);
        taskTitle.setTypeface(face);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return(builder
                .setView(view)
                .create());
    }


}
