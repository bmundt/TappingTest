package edu.umd.cmsc436.tap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PracticeResultPage extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_result_page);

        int numTaps = getIntent().getIntExtra("TAPS", -1);

        TextView textView = (TextView) findViewById(R.id.result);

        textView.setText("Number of Taps: " + numTaps);

    }

    @Override
    public void onClick(View view){
        finish();
    }
}
