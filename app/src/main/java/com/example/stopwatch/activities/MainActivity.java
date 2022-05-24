package com.example.stopwatch.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.stopwatch.databinding.ActivityMainBinding;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //  Number of seconds displayed on the stopwatch
    private int seconds = 0;
    //  Is the stopwatch running?
    private boolean isrunning;
    //  Was the stopwatch running?
    private boolean wasrunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState != null) {
            //  Get the previous state of the stopwatch
            seconds = savedInstanceState.getInt("secondsSaved");
            isrunning = savedInstanceState.getBoolean("isrunningSaved");
            wasrunning = savedInstanceState.getBoolean("wasrunningSaved");
        }

        setListeners();
        runTimer();
    }

    //  Save the state of the stopwatch if it's about to be destroyed
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("secondsSaved", seconds);
        savedInstanceState.putBoolean("isrunningSaved", isrunning);
        savedInstanceState.putBoolean("wasrunningSaved", wasrunning);
    }

    //  Setting listeners for our stopwatch buttons
    private void setListeners() {
        binding.startIconHolder.setOnClickListener(v -> {
            //  start the stopwatch
            isrunning = true;
            showToast("Timer started");
        });

        binding.stopIconHolder.setOnClickListener(v -> {
            //  stop the stopwatch
            isrunning = false;
            showToast("Timer stopped");
        });

        binding.resetIconHolder.setOnClickListener(v -> {
            //  Restart the stopwatch
            isrunning = false;
            seconds = 0;
            showToast("Timer reset");
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //  If the activity is paused, stop the stopwatch
    @Override
    protected void onPause() {
        super.onPause();
        wasrunning = isrunning;
        isrunning = false;
    }

    //  If the activity is resumed, start the stopwatch if it was running previously
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (wasrunning) {
            isrunning = true;
        }
    }

    //  This method uses a handler to increment the seconds and updates the textview
    private void runTimer() {
        //  Create a new handler
        final Handler handler = new Handler();

        //  call the Post method by passing in a new runnable
        //  The post() method processes code without a delay so the code in the runnable will run without delay
        handler.post(new Runnable() {
            @Override
            public void run() {

                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                //  Format the seconds into hours, minutes and seconds
                String time = String.format(
                        Locale.getDefault(),
                        "%d:%02d:%02d",
                        hours,
                        minutes,
                        secs
                );

                //  set the timer text
                binding.stopwatchTimer.setText(time);

                //  If running is true, increment the seconds variable
                if (isrunning) {
                    seconds++;
                }

                //  Post the code again with a delay of 1 second
                handler.postDelayed(this, 1000);
            }
        });
    }
}