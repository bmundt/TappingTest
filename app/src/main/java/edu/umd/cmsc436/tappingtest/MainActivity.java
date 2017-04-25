package edu.umd.cmsc436.tappingtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.umd.cmsc436.sheets.Sheets;

import static edu.umd.cmsc436.sheets.Sheets.TestType.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void instructions(View v) {
        startActivity(new Intent(MainActivity.this, Instructions.class));
    }
}
