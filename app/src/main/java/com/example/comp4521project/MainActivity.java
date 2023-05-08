package com.example.comp4521project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.comp4521project.util.IEvent;
import com.example.comp4521project.util.Time;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.comp4521project.databinding.ActivityMainBinding;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static DataLoader dataLoader;

    private ImageButton addEventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weekly, R.id.navigation_monthly, R.id.navigation_financialhelper)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        dataLoader = new DataLoader(this);

        addEventButton = (ImageButton) findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(addEventButtonClick);
    }

    private View.OnClickListener addEventButtonClick = view -> {
        Intent addIntent = new Intent(MainActivity.this, AddEventIntent.class);
        startActivity(addIntent);
    };

}