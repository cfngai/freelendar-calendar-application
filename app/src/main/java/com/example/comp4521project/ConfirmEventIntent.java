package com.example.comp4521project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comp4521project.util.IEvent;
import com.example.comp4521project.util.Time;

import java.time.LocalDate;
import java.util.List;

public class ConfirmEventIntent extends AppCompatActivity {

    private int eventID;
    private Button confirmButton;
    private Button deleteButton;
    private Button cancelButton;

    private EditText titleField;
    private EditText descriptionField;
    private EditText companyField;
    private Button dateField;
    private Spinner startHour;
    private Spinner startMinute;
    private Spinner endHour;
    private Spinner endMinute;
    private EditText incomeField;
    private EditText remarkField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_event_intent);

        Bundle bundle = getIntent().getExtras();
        eventID = bundle.getInt("id");
        IEvent event = MainActivity.dataLoader.getEventByID(eventID);

        confirmButton = (Button) findViewById(R.id.confirmCompleteButton);
        deleteButton = (Button) findViewById(R.id.confirmDeleteButton);
        cancelButton = (Button) findViewById(R.id.confirmCancelButton);

        confirmButton.setOnClickListener(confirmButtonClick);
        deleteButton.setOnClickListener(deleteButtonClick);
        cancelButton.setOnClickListener(cancelButtonClick);

        titleField = findViewById(R.id.confirmEventTitleField);
        titleField.setText(event.getTitle());
        titleField.setFocusable(false); titleField.setClickable(false);

        descriptionField = findViewById(R.id.confirmEventDescriptionField);
        descriptionField.setText(event.getDescription());
        descriptionField.setFocusable(false); descriptionField.setClickable(false);

        companyField = findViewById(R.id.confirmEvenCompanyField);
        companyField.setText(event.getCompany());
        companyField.setFocusable(false); companyField.setClickable(false);

        dateField = findViewById(R.id.confirmEventDateField);
        dateField.setText(event.getLocalDate().toString());
        dateField.setFocusable(false); dateField.setClickable(false);

        startHour = findViewById(R.id.confirmEventStartHour);
        startMinute = findViewById(R.id.confirmEventStartMinute);

        endHour = findViewById(R.id.confirmEventEndHour);
        endMinute = findViewById(R.id.confirmEventEndMinute);

        String[] _startHour = {event.getStartTime().getHour()+""};
        String[] _endHour = {event.getEndTime().getHour()+""};
        ArrayAdapter<String> startHourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _startHour);
        startHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> endHourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _endHour);
        endHourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startHour.setAdapter(startHourAdapter);
        endHour.setAdapter(endHourAdapter);
        startHour.setFocusable(false); startHour.setClickable(false);
        endHour.setFocusable(false); endHour.setClickable(false);

        String[] _startMinute = {event.getStartTime().getMinute()+""};
        String[] _endMinute = {event.getEndTime().getMinute()+""};
        ArrayAdapter<String> startMinuteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _startMinute);
        startMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> endMinuteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _endMinute);
        endMinuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMinute.setAdapter(startMinuteAdapter);
        endMinute.setAdapter(endMinuteAdapter);
        startMinute.setFocusable(false); startMinute.setClickable(false);
        endMinute.setFocusable(false); endMinute.setClickable(false);

        incomeField = findViewById(R.id.confirmEventIncomeField);
        incomeField.setText(event.getIncome()+"");
        incomeField.setFocusable(false); incomeField.setClickable(false);
        remarkField = findViewById(R.id.confirmEventRemarkField);
        remarkField.setText(event.getRemark());
        remarkField.setFocusable(false); remarkField.setClickable(false);

    }

    private View.OnClickListener confirmButtonClick = view -> {
        MainActivity.dataLoader.updateEventStatus(this,eventID, 1);
        finish();
    };

    private View.OnClickListener deleteButtonClick = view -> {
        MainActivity.dataLoader.removeEvent(this,eventID);
        finish();
    };

    private View.OnClickListener cancelButtonClick = view -> finish();


}