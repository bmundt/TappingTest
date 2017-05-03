package edu.umd.cmsc436.tap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.api.services.sheets.v4.model.Sheet;

import edu.umd.cmsc436.sheets.Sheets;
import edu.umd.cmsc436.frontendhelper.TrialMode;

import static edu.umd.cmsc436.frontendhelper.TrialMode.KEY_APPENDAGE;

public class Practice extends Activity {

    Button lh_practice;
    Button rh_practice;
    Button lf_practice;
    Button rf_practice;
    final String KEY_PRACTICE_MODE = "PRACTICE_MODE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);

        lh_practice = (Button) findViewById(R.id.LH_practice_btn);
        lh_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAP", KEY_APPENDAGE);
                Intent intent = new Intent(Practice.this, TappingTest.class);
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
                Intent intent = new Intent(Practice.this, TappingTest.class);
                intent.putExtra(KEY_APPENDAGE, Sheets.TestType.RH_TAP);
                intent.putExtra(KEY_PRACTICE_MODE, true);
                startActivity(intent);
                finish();
            }
        });

        lf_practice = (Button) findViewById(R.id.LF_practice_btn);
        lf_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Practice.this, TappingTest.class);
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
                Intent intent = new Intent(Practice.this, TappingTest.class);
                intent.putExtra(KEY_APPENDAGE, Sheets.TestType.RF_TAP);
                intent.putExtra(KEY_PRACTICE_MODE, true);
                startActivity(intent);
                finish();
            }
        });
    }
//
//    public void handPractice(View v) {
//        Intent intent = new Intent(Practice.this, TappingPractice.class);
//        intent.putExtra("Appendage", Sheets.TestType.LH_TAP);
//        startActivity(intent);
//    }
//
//    public void footPractice(View v) {
//        Intent intent = new Intent(Practice.this, TappingPractice.class);
//        intent.putExtra("Appendage", Sheets.TestType.LF_TAP);
//        startActivity(intent);
//    }

//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(Practice.this, TappingTest.class);
//        Sheets.TestType appendage = Sheets.TestType.LH_TAP;
//        Log.d("TAP", "ID of view is: " + String.valueOf(v.getId()));
//        Log.d("TAP", "ID of LH_practice_button: "+ String.valueOf(R.id.LH_practice_btn));
//        switch (v.getId()) {
//            case R.id.LH_practice_btn:
//                Log.d("TAP", "case left hand practice button");
//                appendage = Sheets.TestType.LH_TAP;
//                intent.putExtra(KEY_APPENDAGE, appendage);
//                Log.d("TAP", "set appendage");
//                intent.putExtra(KEY_PRACTICE_MODE, true);
//                startActivity(intent);
//                break;
//            case R.id.RH_practice_btn:
//                appendage = Sheets.TestType.RH_TAP;
//                intent.putExtra(KEY_APPENDAGE, appendage);
//                Log.d("TAP", "set appendage");
//                intent.putExtra(KEY_PRACTICE_MODE, true);
//                startActivity(intent);
//                break;
//            case R.id.LF_practice_btn:
//                appendage =  Sheets.TestType.LF_TAP;
//                intent.putExtra(KEY_APPENDAGE, appendage);
//                Log.d("TAP", "set appendage");
//                intent.putExtra(KEY_PRACTICE_MODE, true);
//                startActivity(intent);
//                break;
//            case R.id.RF_practice_btn:
//                appendage = Sheets.TestType.RF_TAP;
//                intent.putExtra(KEY_APPENDAGE, appendage);
//                Log.d("TAP", "set appendage");
//                intent.putExtra(KEY_PRACTICE_MODE, true);
//                startActivity(intent);
//                break;
//            default:
//                intent.putExtra(KEY_APPENDAGE, appendage);
//                Log.d("TAP", "set appendage");
//                intent.putExtra(KEY_PRACTICE_MODE, true);
//                startActivity(intent);
//        }
//
//    }
}
