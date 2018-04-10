package com.theunlikelydev.agenda;

import android.app.Activity;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeScreen extends Activity {

    private TextView agendaTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen_act_layout);

        this.agendaTitle = findViewById(R.id.daily_agenda_title);
        setAgendaTitle();

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
