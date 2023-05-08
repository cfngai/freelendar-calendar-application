package com.example.comp4521project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.comp4521project.util.IEvent;
import com.example.comp4521project.util.Time;

import java.time.LocalDate;
import java.util.List;


public class AddEventIntent extends AppCompatActivity {

    private Button confirmButton;
    private Button cancelButton;

    private EditText titleField;
    private EditText descriptionField;
    private EditText companyField;
    private Button dateField;
    private DatePickerDialog datePickerDialog;
    private Spinner startHour;
    private Spinner startMinute;
    private Spinner endHour;
    private Spinner endMinute;
    private EditText incomeField;
    private EditText remarkField;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_intent);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        confirmButton.setOnClickListener(confirmButtonClick);
        cancelButton.setOnClickListener(cancelButtonClick);

        titleField = findViewById(R.id.addEventTitleField);
        descriptionField = findViewById(R.id.addEventDescriptionField);
        companyField = findViewById(R.id.addEvenCompanyField);

        dateField = findViewById(R.id.addEventDateField);
        initDateField();

        startHour = findViewById(R.id.addEventStartHour);
        startMinute = findViewById(R.id.addEventStartMinute);

        endHour = findViewById(R.id.addEventEndHour);
        endMinute = findViewById(R.id.addEventEndMinute);

        String[] hours = { "00","01","02","03","04","05","06","07","08","09","10","11",
                "12","13","14","15","16","17","18","19","20","21","22","23"};

        ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, hours);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startHour.setAdapter(hourAdapter);
        endHour.setAdapter(hourAdapter);

        String[] minute = { "00","15","30","45"};
        ArrayAdapter<String> minuteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, minute);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startMinute.setAdapter(minuteAdapter);
        endMinute.setAdapter(minuteAdapter);

        incomeField = findViewById(R.id.addEventIncomeField);
        remarkField = findViewById(R.id.addEventRemarkField);

        message = findViewById(R.id.addEventMessageBox);
        message.setText("");
    }

    private View.OnClickListener confirmButtonClick = view -> {
        Time startTime = new Time(Integer.parseInt(startHour.getSelectedItem().toString()),Integer.parseInt(startMinute.getSelectedItem().toString()));
        Time endTime = new Time(Integer.parseInt(endHour.getSelectedItem().toString()),Integer.parseInt(endMinute.getSelectedItem().toString()));
        if (endTime.isEarlierThan(startTime)|| endTime.isEqualTo(startTime)) {
            message.setText("Start Time must be earlier than End Time");
            return;
        }
        String dateText = (String) dateField.getText();
        int year = Integer.parseInt(dateText.split("-")[0]);
        int month = Integer.parseInt(dateText.split("-")[1]);
        int day = Integer.parseInt(dateText.split("-")[2]);
        LocalDate date = LocalDate.of(year, month, day);

        List<IEvent> events = MainActivity.dataLoader.getEventsByDate(date);
        if (events != null) {
            for (IEvent e : events) {
                if (startTime.isEarlierThan(e.getStartTime()) && endTime.isLaterThan(e.getStartTime())) {
                    message.setText("Event overlap with existing event!");
                    return;
                }
                if (startTime.isEqualTo(e.getStartTime())) {
                    message.setText("Event overlap with existing event!");
                    return;
                }
                if (startTime.isLaterThan(e.getStartTime()) && startTime.isEarlierThan(e.getEndTime())) {
                    message.setText("Event overlap with existing event!");
                    return;
                }
            }
        }

        IEvent event = new IEvent(-1, titleField.getText().toString(), descriptionField.getText().toString(),
                companyField.getText().toString(),date,startTime,endTime,Float.parseFloat(incomeField.getText().toString()),
                remarkField.getText().toString(), -1);
        boolean addEvent = MainActivity.dataLoader.addEvent(this,event);
        if (!addEvent) {
            message.setText("Unexpected Error occurs");
        } else {
            finish();
        }
    };

    private View.OnClickListener cancelButtonClick = view -> finish();

    private void initDateField() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            LocalDate date = LocalDate.of(i,i1+1,i2);
            dateField.setText(date.toString());
        };
        LocalDate today = LocalDate.now();
        datePickerDialog = new DatePickerDialog(this, AlertDialog.BUTTON_POSITIVE,dateSetListener,today.getYear(),today.getMonthValue()-1,today.getDayOfMonth());

        dateField.setOnClickListener(openDateClick);
        dateField.setText(today.toString());
    }

    private View.OnClickListener openDateClick = view -> {
        datePickerDialog.show();
    };
}