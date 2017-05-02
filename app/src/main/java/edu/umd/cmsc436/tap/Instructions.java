package edu.umd.cmsc436.tap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import edu.umd.cmsc436.tap.R;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instr_screen);
    }

    protected void next(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
