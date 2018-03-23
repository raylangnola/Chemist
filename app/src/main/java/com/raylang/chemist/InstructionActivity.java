package com.raylang.chemist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by raylang on 3/8/18.
 */

public class InstructionActivity extends AppCompatActivity {
    private final static String ACTIVITY_TAG = "MainActivity";

    KryptocyanicAcidMixer acidMixer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        acidMixer = MainActivity.acidMixer;
        setContentView(R.layout.activity_instruct);
        refreshView();
    }

    private void refreshView() {
        StringBuilder instructs = new StringBuilder(acidMixer.getDescription());
        instructs.append(getString(R.string.strTapToReturn));

        Button goBackBtn = (Button) findViewById(R.id.btnGoBack);
        goBackBtn.setText(instructs.toString());
        /*
        EditText instructView = (EditText) findViewById(R.id.instructsView);
        Log.w(ACTIVITY_TAG,"got handle to text view component");
        String instructs = acidMixer.getDescription();
        Log.w(ACTIVITY_TAG,instructs);
        instructView.setText(instructs);
        Log.w(ACTIVITY_TAG,"set the text on the field");
        */
    }

    public void goBack(View view) {
        this.finish();
    }
}
