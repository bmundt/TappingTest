package edu.umd.cmsc436.tap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.umd.cmsc436.sheets.Sheets;

import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_APPENDAGE;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getAppendage;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialOutOf;

public class TrialResultsPage extends AppCompatActivity {

    private int numTrials;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_results_page);

        numTrials = getTrialOutOf(getIntent());
        Log.d("RESULTS", String.valueOf(numTrials));

        pref = getApplicationContext().getSharedPreferences("TRIALS", Context.MODE_PRIVATE);

        TextView titleView = (TextView) findViewById(R.id.resultsTitle);

        Intent intent = getIntent();
        Sheets.TestType appendage = (Sheets.TestType) intent.getSerializableExtra(KEY_APPENDAGE);
        String title = "";
        switch(appendage) {
            case LH_TAP: title = "Left Hand";
                break;
            case RH_TAP: title = "Right Hand";
                break;
            case LF_TAP: title = "Left Foot";
                break;
            case RF_TAP: title = "Right Foot";
                break;
        }
        titleView.setText(title);

//        ScrollView resultsView = (ScrollView) findViewById(R.id.resultsView);
        GridView gridview = (GridView) findViewById(R.id.resultsView);
//        gridview.setNumColumns(numTrials);
        gridview.setAdapter(new ResultsAdapter(this, numTrials));

//        for (int i = 0; i < numTrials; i++) {
//
//            resultsView.addView(new ResultView(this, (i + 1)));
//        }

        Button nextTrial_btn = (Button) findViewById(R.id.nextTrial);
        nextTrial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private View getView(int position, View convertView) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            convertView = inflater.inflate(R.layout.result_layout, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView score = (TextView) convertView.findViewById(R.id.score);
        title.setText("Trial " + (position + 1));
        Log.d("POSITION", String.valueOf(position));
        int taps = pref.getInt("TRIAL_" + (position + 1), 0);
        if (taps != 0)
            score.setText(taps + " Taps");
        else
            score.setText("-");

        return convertView;
//        TextView dummy = new TextView(context);
//        dummy.setText(position);
//        return dummy;
    }

}
