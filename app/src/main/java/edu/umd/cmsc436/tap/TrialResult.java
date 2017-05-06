package edu.umd.cmsc436.tap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TrialResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_result);

        int numTaps = getIntent().getIntExtra("TAPS", -1);
        boolean finished = getIntent().getBooleanExtra("FINISH", false);

        TextView textView = (TextView) findViewById(R.id.score);

        textView.setText(String.valueOf(numTaps));

        if(finished){
            textView = (TextView) findViewById(R.id.next_trial);
            textView.setText("Return to Menu");
        }


    }

    public void onClick(View view){
        finish();
    }
}
