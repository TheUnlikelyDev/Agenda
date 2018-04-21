package com.theunlikelydev.agenda;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;



public class CreateTaskDialog extends android.app.DialogFragment implements DialogInterface.OnClickListener {


    View dialogView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_create_task_dialog,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        return(builder.setTitle(R.string.create_task_title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel,null)
                .create());

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        EditText title = dialogView.findViewById(R.id.task_title_edt);
        EditText description = dialogView.findViewById(R.id.task_body_edt);

        String parsedTitle = parseTitle(title.getText().toString());
        String parsedDescription =parseDescription(description.getText().toString());

        HomeScreen homeScreen =(HomeScreen) getActivity();
        homeScreen.addTask(parsedTitle,parsedDescription);
    }

    private String parseTitle(String title){

        return firstLetterUpcase(title);

    }
    private String parseDescription(String description){

        return firstLetterUpcase(description);

    }

    private String firstLetterUpcase(String convert){
        char[] arr = convert.toCharArray();
        if(convert.length() <=0){
            return "";
        }
        char cap = Character.toUpperCase(arr[0]);

        StringBuilder newString = new StringBuilder(convert).replace(0,1,Character.toString(cap));
        return newString.toString();

    }
}
