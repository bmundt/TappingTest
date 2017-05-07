package edu.umd.cmsc436.tap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by brmun on 5/7/2017.
 */

public class ResultsAdapter extends BaseAdapter {

    private int numTrials;
    private Context context;
    private SharedPreferences pref;

    public ResultsAdapter(Context c, int trialOutOf) {
        super();
        context = c;
        numTrials = trialOutOf;
        pref = context.getSharedPreferences("TRIALS", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return numTrials;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
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
