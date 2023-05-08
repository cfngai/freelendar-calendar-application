package com.example.comp4521project.ui.financialhelper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp4521project.DataLoader;
import com.example.comp4521project.MainActivity;
import com.example.comp4521project.R;
import com.example.comp4521project.databinding.FragmentFinancialhelperBinding;
import com.example.comp4521project.ui.IFragment;
import com.example.comp4521project.util.IEvent;

import java.time.LocalDate;
import java.util.List;

public class FinancialHelperFragment extends IFragment {

    private FragmentFinancialhelperBinding binding;

    private Button fromDateField;
    private DatePickerDialog fromDatePickerDialog;
    private Button toDateField;
    private DatePickerDialog toDatePickerDialog;

    private EditText targetField;

    private Button doneButton;

    private TextView resultBox;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFrag = this;

        binding = FragmentFinancialhelperBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textFinancialhelper;
        fromDateField = binding.financialFromPick;
        toDateField = binding.financialToPick;
        initFromDateField();
        initToDateField();

        targetField = binding.financialTarget;

        doneButton = binding.financialDoneButton;
        doneButton.setOnClickListener(doneButtonClick);

        resultBox = binding.financialResultText;

        return root;
    }
    private void initFromDateField() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            LocalDate date = LocalDate.of(i,i1+1,i2).minusDays(30);
            fromDateField.setText(date.toString());
        };
        LocalDate date = LocalDate.now().minusDays(30);
        fromDatePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.BUTTON_POSITIVE,dateSetListener,date.getYear(),date.getMonthValue()-1,date.getDayOfMonth());

        fromDateField.setOnClickListener(openFromDateClick);
        fromDateField.setText(date.toString());
    }

    private View.OnClickListener openFromDateClick = view -> {
        fromDatePickerDialog.show();
    };

    private void initToDateField() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            LocalDate date = LocalDate.of(i,i1+1,i2);
            toDateField.setText(date.toString());
        };
        LocalDate today = LocalDate.now();
        toDatePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.BUTTON_POSITIVE,dateSetListener,today.getYear(),today.getMonthValue()-1,today.getDayOfMonth());

        toDateField.setOnClickListener(openToDateClick);
        toDateField.setText(today.toString());
    }

    private View.OnClickListener openToDateClick = view -> {
        toDatePickerDialog.show();
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private View.OnClickListener doneButtonClick = view -> {
        int numEvent = 0;
        int completed = 0;
        int incomplete = 0;
        int upcoming = 0;
        float confirmedIncome = 0; float confirmedHour = 0;
        float totalIncome = 0; float totalHour = 0;

        String fromDateText = (String) fromDateField.getText();
        int fromYear = Integer.parseInt(fromDateText.split("-")[0]);
        int fromMonth = Integer.parseInt(fromDateText.split("-")[1]);
        int fromDay = Integer.parseInt(fromDateText.split("-")[2]);
        LocalDate fromDate = LocalDate.of(fromYear, fromMonth, fromDay);

        String toDateText = (String) toDateField.getText();
        int toYear = Integer.parseInt(toDateText.split("-")[0]);
        int toMonth = Integer.parseInt(toDateText.split("-")[1]);
        int toDay = Integer.parseInt(toDateText.split("-")[2]);
        LocalDate toDate = LocalDate.of(toYear, toMonth, toDay);

        List<IEvent> resultEvents = MainActivity.dataLoader.getEventsByPeriod(fromDate, toDate);
        for (IEvent event : resultEvents) {
            numEvent++;
            totalIncome += event.getIncome();
            totalHour += 1.0 * (event.getEndTime().toInt() - event.getStartTime().toInt()) / 60;

            confirmedHour += 1.0 * (event.getEndTime().toInt() - event.getStartTime().toInt()) / 60;
            if (event.getStatus() == 1) {
                completed++;
                confirmedIncome += event.getIncome();
                confirmedHour += 1.0 * (event.getEndTime().toInt() - event.getStartTime().toInt()) / 60;
            } else if (event.getStatus() == 0) {
                incomplete++;
            } else {
                upcoming++;
            }
        }

        float confirmedAvgIncomePerE = confirmedIncome/completed;
        float confirmedAvgIncomePerH = confirmedIncome/confirmedHour;
        float totalAvgIncomePerE = totalIncome/numEvent;
        float totalAvgIncomePerH = totalIncome/totalHour;

        int completePercent = (int)(100.0 * completed / numEvent);
        int incompletePercent = (int)(100.0 * incomplete / numEvent);
        int upcomingPercent = (int)(100.0 * upcoming / numEvent);


        String outputText = "Total Events Count: "+numEvent +"\n";
        outputText += "Completed Events: "+ completed +" ("+completePercent+"%)" +"\n";
        outputText += "Incomplete Events: "+ incomplete +" ("+incompletePercent+"%)" +"\n";
        outputText += "Upcoming Events: "+ upcoming +" ("+upcomingPercent+"%)" +"\n\n";

        outputText += "Confirmed Income: $" + confirmedIncome + "\n";
        outputText += "Average Income per Event: $" + confirmedAvgIncomePerE + "\n";
        outputText += "Average Income per Hour: $" + confirmedAvgIncomePerH + "\n\n";

        outputText += "Predicted Income: $" + totalIncome + "\n";
        outputText += "Average Income per Event: $" + totalAvgIncomePerE + "\n";
        outputText += "Average Income per Hour: $" + totalAvgIncomePerH + "\n";
        float target = Float.parseFloat(targetField.getText().toString());
        if (totalIncome > target) outputText += "Target Achieved";
        else outputText += "Target Failed";
        resultBox.setText(outputText);

    };
    @Override
    public void refreshContent() {
        MainActivity.dataLoader.UpdateStatus();
    }
}