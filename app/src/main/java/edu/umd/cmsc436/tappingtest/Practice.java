package edu.umd.cmsc436.tappingtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.view.View;

import edu.umd.cmsc436.sheets.Sheets;
import edu.umd.cmsc436.sheets.Sheets.TestType;

public class Practice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);
    }

    protected void handPractice(View v) {
        Intent intent = new Intent(Practice.this, TappingTest.class);
        intent.putExtra("Appendage", Sheets.TestType.LH_TAP);
        startActivity(intent);
    }

    protected void footPractice(View v) {
        Intent intent = new Intent(Practice.this, TappingTest.class);
        intent.putExtra("Appendage", Sheets.TestType.LF_TAP);
        startActivity(intent);
    }
}
