package edu.umd.cmsc436.tap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import edu.umd.cmsc436.sheets.Sheets;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import static edu.umd.cmsc436.frontendhelper.TrialMode.getAppendage;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getPatientId;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getResultIntent;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialNum;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialOutOf;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;


public class TappingTest extends Activity implements Sheets.Host {

    private String patientId;
    private Sheets.TestType appendage;
    private int trialNum;
    private int trialOutOf;
    private final int TIME_LIMIT = 10; // number of seconds

    private Intent intent;
    private TextView timeLeft;
    private CountDownTimer timer;
    private int totalTaps;
    private int taps;
    private boolean timerStarted;
    private long secondsRemaining;
    private float[] numTaps;
    ImageButton questionMark;
    private Sheets sheet;

    private final String MAIN_SHEET_ID = "1YvI3CjS4ZlZQDYi5PaiA7WGGcoCsZfLoSFM0IdvdbDU";
    private final String PRIVATE_SHEET_ID = "1MU87u75_qx35qb6TdtizRBeOH1fkO76ufzR47bfZaRQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping_practice);

        numTaps = new float[TIME_LIMIT];
        intent = getIntent();
        appendage= getAppendage(intent);
        trialNum = getTrialNum(intent);
        trialOutOf = getTrialOutOf(intent);
        patientId = getPatientId(intent);


        if (appendage == Sheets.TestType.LH_TAP)  {
            setContentView(R.layout.left_hand_test);
        } else if (appendage == Sheets.TestType.RH_TAP) {
            setContentView(R.layout.right_hand_test);
        } else if (appendage == Sheets.TestType.LF_TAP || appendage == Sheets.TestType.RF_TAP) {
            setContentView(R.layout.foot_test);
        }

        timeLeft = (TextView) findViewById(R.id.timeLeft);
        questionMark = (ImageButton) findViewById(R.id.question_mark);

        sheet = new Sheets(this, this, getString(R.string.app_name),
                MAIN_SHEET_ID, PRIVATE_SHEET_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (secondsRemaining != 0) {
            createCountdownTimer(secondsRemaining);
//            timer = new CountDownTimer(secondsRemaining  * 1000, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    secondsRemaining = millisUntilFinished / 1000;
//                    numTaps[TIME_LIMIT - (int) secondsRemaining] = taps;
//                    timeLeft.setText("Seconds remaining: " + secondsRemaining);
//                }
//
//                @Override
//                public void onFinish() {
//                    totalTaps = taps;
//                    // set the values for the different trials
//                    timeLeft.setText("Total Taps: " + totalTaps);
//                    intent.putExtra("score", new Float(totalTaps));
//                    finish();
//                }
//            };
        } else {
            createCountdownTimer(TIME_LIMIT);
//            timer = new CountDownTimer(TIME_LIMIT * 1000, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    secondsRemaining = millisUntilFinished / 1000;
//                    numTaps[TIME_LIMIT - (int) secondsRemaining] = taps;
//                    timeLeft.setText("Seconds remaining: " + secondsRemaining);
//                }
//
//                @Override
//                public void onFinish() {
//                    totalTaps = taps;
//                    // set the values for the different trials
//                    timeLeft.setText("Total Taps: " + totalTaps);
//                    intent.putExtra("score", new Float(totalTaps));
//                    finish();
//                }
//            };
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
        AlertDialog instructions = new AlertDialog.Builder(TappingTest.this).create();
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
        AlertDialog restart = new AlertDialog.Builder(TappingTest.this).create();
        restart.setTitle("Restart Test");
        restart.setMessage("Would you like to restart the test?");
        restart.setButton(AlertDialog.BUTTON_NEUTRAL, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        timerStarted = false;
                        Intent intent = new Intent(TappingTest.this, TappingPractice.class);
                        intent.putExtra("appendage", (Sheets.TestType) getIntent().getSerializableExtra("appendage"));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        restart.setButton(AlertDialog.BUTTON_NEUTRAL, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TappingTest.this, TappingPractice.class);
                        intent.putExtra("appendage", (Sheets.TestType) getIntent().getSerializableExtra("appendage"));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        restart.show();
    }

    private void createCountdownTimer(final long timeRemaining) {
        timer = new CountDownTimer(timeRemaining  * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = millisUntilFinished / 1000;
                numTaps[TIME_LIMIT - ((int) secondsRemaining) - 1] = taps;
                Log.d("TAPS", "wrote " + taps + " taps at position" + (TIME_LIMIT - ((int) secondsRemaining) - 1));
                timeLeft.setText("Seconds remaining: " + secondsRemaining);
            }

            @Override
            public void onFinish() {
                totalTaps = taps;
                // set the values for the different trials
                timeLeft.setText("Total Taps: " + totalTaps);
                numTaps[TIME_LIMIT - 1] = totalTaps;
                Log.d("TAPS", "wrote " + taps + " taps at position" + (TIME_LIMIT - 1));
                intent.putExtra("score", new Float(totalTaps));
                testFinished();
            }
        };
    }

    private void testFinished() {
        Log.d("SHEETS", patientId);
        sheet.writeTrials(appendage, patientId, numTaps);
        Log.d("SHEETS", "NumTaps last 2: " + numTaps[8] + "," + numTaps[9]);
        Log.d("SHEETS", "finished writing trial");
//        try {
//            sleep(10000);
//        } catch(Exception e) {
//            Log.d("TAP", "couldn't sleep at end");
//        }
//        try {
//            currentThread().join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            Log.d("SHEETS", "failed waiting for background thread");
//        } finally {
//            finish();
//        }
        Intent resultIntent = getResultIntent(totalTaps);
        setResult(RESULT_OK, resultIntent);
        finish();

    }

    @Override
    public int getRequestCode(Sheets.Action action) {
        switch (action) {
            case REQUEST_PERMISSIONS:
                return 1000;
            case REQUEST_ACCOUNT_NAME:
                return 1001;
            case REQUEST_PLAY_SERVICES:
                return 1002;
            case REQUEST_AUTHORIZATION:
                return 1003;
        }
        return 0;
    }

    @Override
    public void notifyFinished(Exception e) {
        if (e != null) {
            Log.d("SHEETS", e.toString());
        } else {
            Log.d("SHEETS", "notifyFinished exception was null");
        }
    }


    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        sheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
    }
}


