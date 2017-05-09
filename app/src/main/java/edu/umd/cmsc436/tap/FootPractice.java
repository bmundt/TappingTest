package edu.umd.cmsc436.tap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.umd.cmsc436.sheets.Sheets;

import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_APPENDAGE;

public class FootPractice extends AppCompatActivity {

    Button lf_practice;
    Button rf_practice;
    final String KEY_PRACTICE_MODE = "PRACTICE_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_practice);

        lf_practice = (Button) findViewById(R.id.LF_practice_btn);
        lf_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FootPractice.this, TappingTest.class);
                intent.putExtra(KEY_APPENDAGE, Sheets.TestType.LF_TAP);
                intent.putExtra(KEY_PRACTICE_MODE, true);
                startActivity(intent);
                finish();
            }
        });

        rf_practice = (Button) findViewById(R.id.RF_practice_btn);
        rf_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FootPractice.this, TappingTest.class);
                intent.putExtra(KEY_APPENDAGE, Sheets.TestType.RF_TAP);
                intent.putExtra(KEY_PRACTICE_MODE, true);
                startActivity(intent);
                finish();
            }
        });
    }
}
