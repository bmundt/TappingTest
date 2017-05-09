package edu.umd.cmsc436.tap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import edu.umd.cmsc436.sheets.Sheets;

import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_APPENDAGE;

public class HandPractice extends Activity {

    Button lh_practice;
    Button rh_practice;

    final String KEY_PRACTICE_MODE = "PRACTICE_MODE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_practice);

        lh_practice = (Button) findViewById(R.id.LH_practice_btn);
        lh_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAP", KEY_APPENDAGE);
                Intent intent = new Intent(HandPractice.this, TappingTest.class);
                intent.putExtra(KEY_APPENDAGE, Sheets.TestType.LH_TAP);
                intent.putExtra(KEY_PRACTICE_MODE, true);
                startActivity(intent);
                finish();
            }
        });

        rh_practice = (Button) findViewById(R.id.RH_practice_btn);
        rh_practice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HandPractice.this, TappingTest.class);
                intent.putExtra(KEY_APPENDAGE, Sheets.TestType.RH_TAP);
                intent.putExtra(KEY_PRACTICE_MODE, true);
                startActivity(intent);
                finish();
            }
        });


    }
}
