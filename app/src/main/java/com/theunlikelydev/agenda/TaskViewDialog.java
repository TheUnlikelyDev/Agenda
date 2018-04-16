package com.theunlikelydev.agenda;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;


public class TaskViewDialog extends android.app.DialogFragment {

    String[] taskInfo;
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments() == null){
            Log.d("s", "onCreateDialog: ");
        }
        this.taskInfo = getArguments().getStringArray("task_info");
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_task_view_dialog,null);

        TextView title = view.findViewById(R.id.title_tv);
        TextView description = view.findViewById(R.id.description_tv);
        title.setText(taskInfo[0]);
        description.setText(taskInfo[1]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return(builder.setTitle(R.string.view_dialog_text)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create());
    }


}
