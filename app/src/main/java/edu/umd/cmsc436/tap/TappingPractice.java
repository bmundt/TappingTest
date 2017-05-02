package edu.umd.cmsc436.tap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ProgressBar;

import edu.umd.cmsc436.sheets.Sheets;

public class TappingPractice extends AppCompatActivity {


    private Intent intent;
    private Sheets.TestType TYPE;
    private TextView timeLeft;
    private CountDownTimer timer;
    private int totalTaps;
    private int taps;
    private boolean timerStarted;
    private long secondsRemaining;
    ImageButton questionMark;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping_practice);

        intent = getIntent();
        TYPE = (Sheets.TestType) intent.getSerializableExtra("Appendage");

        if (TYPE == Sheets.TestType.LH_TAP)  {
            setContentView(R.layout.left_hand_test);
        } else if (TYPE == Sheets.TestType.RH_TAP) {
            setContentView(R.layout.right_hand_test);
        } else if (TYPE == Sheets.TestType.LF_TAP || TYPE == Sheets.TestType.RF_TAP) {
            setContentView(R.layout.foot_test);
        }

        timeLeft = (TextView) findViewById(R.id.timeLeft);
        questionMark = (ImageButton) findViewById(R.id.question_mark);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (secondsRemaining != 0) {
            timer = new CountDownTimer(secondsRemaining  * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    secondsRemaining = millisUntilFinished / 1000;
                    timeLeft.setText("Seconds remaining: " + secondsRemaining);
                    updateProgressBar((int) millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    totalTaps = taps;
                    // set the values for the different trials
                    timeLeft.setText("Total Taps: " + totalTaps);
                    intent.putExtra("score", new Float(totalTaps));
                    Intent intent = new Intent(TappingPractice.this, PracticeResultPage.class);
                    intent.putExtra("Taps", totalTaps);
                    startActivity(intent);
                    finish();
                }
            };
        } else {
            timer = new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    secondsRemaining = millisUntilFinished / 1000;
                    timeLeft.setText("Seconds remaining: " + secondsRemaining);
                    updateProgressBar((int) millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    totalTaps = taps;
                    // set the values for the different trials
                    timeLeft.setText("Total Taps: " + totalTaps);
                    intent.putExtra("score", new Float(totalTaps));
                    Intent intent = new Intent(TappingPractice.this, PracticeResultPage.class);
                    intent.putExtra("Taps", totalTaps);
                    startActivity(intent);
                    finish();
                }
            };
        }

        timerStarted = false;
        taps = 0;
    }

    protected void tapButton(View v) {
        if (!timerStarted) { // only start timer if not already started
            questionMark.setVisibility(View.INVISIBLE);
            timer.start();
            taps++;
            timerStarted = true;
        } else {
            taps++;
        }
    }

    protected void questionMark(View v) {
        AlertDialog instructions = new AlertDialog.Builder(TappingPractice.this).create();
        instructions.setTitle("Instructions");
        instructions.setMessage("Place the phone on a level surface\n" +
        "Begin Tapping the button in the middle of the screen when ready\n" +
        "A Timer will start on the first tap, and you will have 10 seconds to tap as many times as possible");
        instructions.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }
                });
        instructions.show();
    }

    protected void restart(View v) {
        AlertDialog restart = new AlertDialog.Builder(TappingPractice.this).create();
        restart.setTitle("Restart Test");
        restart.setMessage("Would you like to restart the test?");
        restart.setButton(AlertDialog.BUTTON_NEUTRAL, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        timerStarted = false;
                        Intent intent = new Intent(TappingPractice.this, TappingPractice.class);
                        intent.putExtra("appendage", (Sheets.TestType) getIntent().getSerializableExtra("appendage"));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        restart.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TappingPractice.this, TappingPractice.class);
                        intent.putExtra("appendage", (Sheets.TestType) getIntent().getSerializableExtra("appendage"));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        restart.show();
    }

    private void updateProgressBar(int millisUntilFinished){

        progressBar.setProgress(11000-millisUntilFinished);

    }


    @Override
    public void onBackPressed() {
    }
}
