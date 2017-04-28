package edu.umd.cmsc436.tap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.umd.cmsc436.sheets.Sheets;

public class Practice extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);
    }

    protected void handPractice(View v) {
        Intent intent = new Intent(Practice.this, TappingPractice.class);
        intent.putExtra("Appendage", Sheets.TestType.LH_TAP);
        startActivity(intent);
    }

    protected void footPractice(View v) {
        Intent intent = new Intent(Practice.this, TappingPractice.class);
        intent.putExtra("Appendage", Sheets.TestType.LF_TAP);
        startActivity(intent);
    }
}
