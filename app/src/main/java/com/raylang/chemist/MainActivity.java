package com.raylang.chemist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String ACTIVITY_TAG = "MainActivity";
    public static KryptocyanicAcidMixer acidMixer = new KryptocyanicAcidMixer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        acidMixer.startNewGame();
        refreshView();
    }

    private void refreshView() {
        // compose the strings to be placed in the view
        String outStrLives = getString(R.string.strGameOver);
        String outStrAcid = getString(R.string.strClickStartToBegin);
        if (acidMixer.isGameInProgress()) {
            int lives = acidMixer.getLives();
            int acid = acidMixer.getAcid();

            outStrLives = String.format(
                    getString(R.string.strLivesLeft), lives);
            outStrAcid = String.format(
                    getString(R.string.strLitersOfAcid), acid);
        }

        // fetch the controls that will display the strings
        TextView livesLeftTextView = (TextView) findViewById(R.id.textViewLivesLeft);
        TextView litersAcidTextView = (TextView) findViewById(R.id.textViewLitersAcid);
        // put the strings in them
        livesLeftTextView.setText(outStrLives);
        litersAcidTextView.setText(outStrAcid);
    }

    public void showInstructs(View view) {
        Intent instructIntent = new Intent(this, InstructionActivity.class);
        this.startActivity(instructIntent);
    }

    public void playOnce(View view) {
        Resources res = this.getResources();
        Button playBtn = (Button) findViewById(R.id.btnPlay);

        // if this is a 'new game' button, re-start and return
        if (!acidMixer.isGameInProgress()) {
            playBtn.setText(res.getString(R.string.btnPlayLabel));
            acidMixer.startNewGame();
            refreshView();
            return;
        }

        // a game in progress, get the amount of water from the view
        EditText waterInput = (EditText) findViewById(R.id.editTextWaterInput);
        String waterInStr = waterInput.getText().toString();
        double water;
        try {
            water = Double.parseDouble(waterInStr);
        } catch (NumberFormatException nfe) {
            water = 0;
        }

        // apply the amount of water to the model
        acidMixer.doOneRound(water);

        // show what happened
        if (acidMixer.latestMixWasStable()) {
            Toast.makeText(this, acidMixer.getLatestAttemptDescrip(), Toast.LENGTH_LONG).show();
        } else {
            showFailureDialog();
        }
        waterInput.setText("");
        if (!acidMixer.isGameInProgress()) {
            // change the label on the 'play' button
            playBtn.setText(res.getString(R.string.strNewGame));
        }

        // refresh the view for the next round
        refreshView();
    }

    private void showFailureDialog() {
        Resources res = this.getResources();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(res.getString(R.string.app_name));
        String outStr = acidMixer.getLatestAttemptDescrip();
        alert.setMessage(outStr);
        alert.setNeutralButton(R.string.strDismiss, new DialogClickListener());
        alert.show();
    }

    private class DialogClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.w(ACTIVITY_TAG, "Player loses one life");
        }
    }

    /* mobile device apps don't generally have a 'quit' button
       they just go to the background until the user explicitly
       clears them
     */
    public void quitApp(View view) {
        this.finish();
    }
}
