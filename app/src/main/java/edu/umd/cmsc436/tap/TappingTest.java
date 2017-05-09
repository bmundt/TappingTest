package edu.umd.cmsc436.tap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import edu.umd.cmsc436.sheets.Sheets;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.api.services.sheets.v4.model.Sheet;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_APPENDAGE;
import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_PATIENT_ID;
import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_SCORE;
import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_TRIAL_NUM;
import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_TRIAL_OUT_OF;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getAppendage;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getPatientId;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getResultIntent;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialNum;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialOutOf;
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
    private Button tap;
    private ImageButton questionMark;
    private Sheets sheet;
    private SharedPreferences pref;

    private boolean practiceMode;
    private final String KEY_PRACTICE_MODE = "PRACTICE_MODE";

    private final String MAIN_SHEET_ID = "1YvI3CjS4ZlZQDYi5PaiA7WGGcoCsZfLoSFM0IdvdbDU";
    private final String PRIVATE_SHEET_ID = "1MU87u75_qx35qb6TdtizRBeOH1fkO76ufzR47bfZaRQ";

    private ArrayList<Float> timeBetweenTaps = new ArrayList<Float>();
    private Long timePrevTap;
    private Long currentTimeLeft;
    private boolean timerComplete;

    public static final int LIB_ACCOUNT_NAME_REQUEST_CODE = 1001;
    public static final int LIB_AUTHORIZATION_REQUEST_CODE = 1002;
    public static final int LIB_PERMISSION_REQUEST_CODE = 1003;
    public static final int LIB_PLAY_SERVICES_REQUEST_CODE = 1004;
    public static final int LIB_CONNECTION_REQUEST_CODE = 1005;

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapping_practice);

        numTaps = new float[TIME_LIMIT];
        intent = getIntent();
//        appendage= getAppendage(intent);

        practiceMode = intent.getBooleanExtra(KEY_PRACTICE_MODE, false);


        pref = getApplicationContext().getSharedPreferences("TRIALS",
                Context.MODE_PRIVATE);

        if (!practiceMode) {
            appendage = getAppendage(intent);
            trialNum = getTrialNum(intent);
            trialOutOf = getTrialOutOf(intent);
            patientId = getPatientId(intent);

            if (trialNum == 1) {
                // clear the other trials from the SharedPreferences
                SharedPreferences.Editor editor = pref.edit();
                for (int i = 1; i <= trialOutOf; i++)
                    editor.remove("TRIAL_" + i);
                editor.commit();
            }
            timePrevTap = -1L;
            timeBetweenTaps = new ArrayList<Float>();
            timerComplete = false;

        } else {
            appendage = (Sheets.TestType) intent.getSerializableExtra(KEY_APPENDAGE);
        }

        if (appendage == Sheets.TestType.LH_TAP)  {
            setContentView(R.layout.left_hand_test);
            TextView handText = (TextView) findViewById(R.id.displayText);
            handText.setText("Left Hand\nTrial " + "\n" + trialNum + " of " + trialOutOf);
        } else if (appendage == Sheets.TestType.RH_TAP) {
            setContentView(R.layout.right_hand_test);
            TextView handText = (TextView) findViewById(R.id.displayText);
            handText.setText("Right Hand\nTrial " + "\n" + trialNum + " of " + trialOutOf);
        } else if (appendage == Sheets.TestType.LF_TAP || appendage == Sheets.TestType.RF_TAP) {
            setContentView(R.layout.foot_test);
            Button handText = (Button) findViewById(R.id.tap);
            if (appendage == Sheets.TestType.LF_TAP) {
                handText.setText("TAP\n\nLeft Foot\nTrial " + "\n" + trialNum + " of " + trialOutOf);
            } else {
                handText.setText("TAP\n\nRight Foot\nTrial " + "\n" + trialNum + " of " + trialOutOf);
            }
        } else {
            if (appendage == null)
                Log.d("TAP", "why is this null!!!");
            Log.d("TAP", "appendage not recognized");
        }

        Log.d("TAP", "about to set onClickListener");
        tap = (Button) findViewById(R.id.tap);
        tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapButton(v);
            }
        });
        Log.d("TAP", "onClickListener set");

        questionMark = (ImageButton) findViewById(R.id.question_mark);
        questionMark.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                questionMark(v);
            }
        });

        timeLeft = (TextView) findViewById(R.id.timeLeft);

        sheet = new Sheets(this, this, getString(R.string.app_name),
                MAIN_SHEET_ID, PRIVATE_SHEET_ID);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(10000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (secondsRemaining != 0) {
            createCountdownTimer(secondsRemaining);
        } else {
            createCountdownTimer(TIME_LIMIT);
        }

        timerStarted = false;
        taps = 0;
    }

    public void tapButton(View v) {
        if (!timerStarted) { // only start timer if not already started
            questionMark.setAlpha(.5f);
            questionMark.setClickable(false);
            //questionMark.setVisibility(View.INVISIBLE);
            timer.start();
            taps++;
            timerStarted = true;
        } else {
            taps++;
        }

        if (!practiceMode) {
            if (!timerComplete) {
                if (timePrevTap == -1) {
                    timeBetweenTaps.add(-1F);
                } else {
                    timeBetweenTaps.add(new Float(timePrevTap - currentTimeLeft));
                }
            }
        }
    }

    public void questionMark(View v) {
        AlertDialog instructions = new AlertDialog.Builder(TappingTest.this).create();
        instructions.setTitle("Instructions");
        instructions.setMessage("Place the phone on a level surface.\n" +
                "Begin Tapping the large button on the screen.\n" +
                "A Timer will start on the first tap, and you will have 10 seconds to tap as many times as possible.\n" +
                "Please use your pointer finger to tap.");
        instructions.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }
                });
        instructions.show();
    }

    public void restart(View v) {
        AlertDialog restart = new AlertDialog.Builder(TappingTest.this).create();
        restart.setTitle("Cancel Test");
        restart.setCancelable(false);
        timer.cancel();
        timerStarted = false;
        timeLeft.setText("Begin Tapping When Ready");
        taps = 0;
        progressBar.setProgress(0);
        // write an incompletet trial to the sheets
        if (!practiceMode)
            sheet.writeTrials(appendage, patientId, changeToFloatArray(timeBetweenTaps));
        restart.setMessage("This test has been canceled. Please retry it again");
        restart.setButton(AlertDialog.BUTTON_NEUTRAL, "Got it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // somehow doing nothing is the right thing
//                        Intent intent = new Intent(TappingTest.this, TappingTest.class);
//                        intent.putExtra(KEY_APPENDAGE, appendage);
//                        intent.putExtra(KEY_TRIAL_NUM, trialNum);
//                        intent.putExtra(KEY_TRIAL_OUT_OF, trialOutOf);
//                        intent.putExtra(KEY_PATIENT_ID, patientId);
//                        startActivity(intent);
//                        Intent resultIntent = new Intent();
//                        resultIntent.putExtra(KEY_SCORE, 0.00F);
//                        setResult(RESULT_OK, resultIntent);
//                        dialog.dismiss();
//                        finish();
                    }
                });
        restart.show();
    }

    private void createCountdownTimer(final long timeRemaining) {
        timer = new CountDownTimer(timeRemaining  * 1000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                secondsRemaining = millisUntilFinished / 1000;
                timeLeft.setText("Seconds remaining: " + secondsRemaining);
                //numTaps[TIME_LIMIT - ((int) secondsRemaining) - 1] = taps;

                Log.d("TAPS", "wrote " + taps + " taps at position" + (TIME_LIMIT - ((int) secondsRemaining) - 1));
                updateProgressBar((int) millisUntilFinished);
                currentTimeLeft = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                timerComplete = true;
                totalTaps = taps;
                // set the values for the different trials
                timeLeft.setText("Total Taps: " + totalTaps);
                // set number of taps to be first column to be written to db
                if (!practiceMode) {
                    timeBetweenTaps.set(0, new Float(totalTaps));
                }
                Log.d("TAPS", "wrote " + taps + " taps at position" + (TIME_LIMIT - 1));
//                intent.putExtra("score", new Float(totalTaps));
                testFinished();
            }
        };
    }

    private void testFinished() {
        if (!practiceMode) {
            Log.d("SHEETS", patientId);
            sheet.writeTrials(appendage, patientId, changeToFloatArray(timeBetweenTaps));
        } else {
            Intent intent = new Intent(TappingTest.this, PracticeResultPage.class);
            intent.putExtra("TAPS", totalTaps);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public int getRequestCode(Sheets.Action action) {
        switch (action) {
            case REQUEST_ACCOUNT_NAME:
                return LIB_ACCOUNT_NAME_REQUEST_CODE;
            case REQUEST_AUTHORIZATION:
                return LIB_AUTHORIZATION_REQUEST_CODE;
            case REQUEST_PERMISSIONS:
                return LIB_PERMISSION_REQUEST_CODE;
            case REQUEST_PLAY_SERVICES:
                return LIB_PLAY_SERVICES_REQUEST_CODE;
            case REQUEST_CONNECTION_RESOLUTION:
                return LIB_CONNECTION_REQUEST_CODE;
            default:
                return -1;
        }
    }

    @Override
    public void notifyFinished(Exception e) {
        if (e != null) {
            Log.d("SHEETS", e.toString());
        } else {
            Log.d("SHEETS", "notifyFinished exception was null");
        }

        Log.d("SHEETS", "NumTaps last 2: " + numTaps[8] + "," + numTaps[9]);
        Log.d("SHEETS", "finished writing trial");
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_SCORE, (float) totalTaps);
        setResult(RESULT_OK, resultIntent);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("TRIAL_" + trialNum, totalTaps);
        editor.commit();
        Intent resultsPageIntent = new Intent(TappingTest.this, TrialResultsPage.class);
        resultsPageIntent.putExtra(KEY_TRIAL_OUT_OF, trialOutOf);
        resultsPageIntent.putExtra(KEY_APPENDAGE, appendage);
        startActivity(resultsPageIntent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        sheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.sheet.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // do the cancel stuff Activity.RESULT_CANCELLED
        Intent resultIntent = new Intent();
        setResult(RESULT_CANCELED, resultIntent);
        finish();
    }

    private void updateProgressBar(int millisUntilFinished){

        progressBar.setProgress(11000-millisUntilFinished);

    }

    private float[] changeToFloatArray(ArrayList<Float> floatArrayList) {
        float[] arr = new float[floatArrayList.size()];
        for (int i = 0; i < floatArrayList.size(); i++) {
            arr[i] = floatArrayList.get(i);
        }
        return arr;
    }
}


