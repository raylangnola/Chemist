package com.raylang.chemist;

import java.util.Random;

/**
 * Created by raylang on 3/8/18.
 */

public class KryptocyanicAcidMixer {
    private final static String ACTIVITY_TAG = "KryptocyanicAcidMixer";

    private final String WIN_STRING = "Good job! You can breathe now, just don't inhale the fumes!";
    private final String LOSE_FORMAT = "SIZZLE! You have just been desalinated\n" +
            "into a blob of quivering protoplasm!\n\n" +
            "You did not survive the experience.\n\n" +
            "%d units of acid require between %.2f and %.2f units of water, " +
            "but you gave %.2f";

    // game parameters
    private final double MARGIN_OF_ERROR = 0.05;
    private final String UNITS = "Liters";
    private final int MAX_LIVES = 4;
    private final int MAX_ACID = 50;
    private final int PARTS_ACID = 3;
    private final int PARTS_WATER = 7;

    // state variables
    private boolean gameInProgress;
    private int lives;
    private int acid;
    private String latestAttemptDescrip;
    private boolean latestAttemptSucceeded;

    private final Random rnd = new Random();

    public KryptocyanicAcidMixer() {
        startNewGame();
    }

    public void startNewGame() {
        gameInProgress = true;
        lives = MAX_LIVES;
        acid = rnd.nextInt(MAX_ACID) + 1;
        latestAttemptDescrip = "";
        latestAttemptSucceeded = false;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public int getLives() {
        return lives;
    }

    public int getAcid() {
        return acid;
    }

    public String getLatestAttemptDescrip() {
        return latestAttemptDescrip;
    }

    public boolean latestMixWasStable() {
        return latestAttemptSucceeded;
    }

    public void doOneRound(double water_provided) {
        double water_needed = PARTS_WATER * acid / PARTS_ACID;
        double margin = water_needed * MARGIN_OF_ERROR;
        double diff = Math.abs(water_provided - water_needed);
        if (diff < margin) {
            latestAttemptDescrip = WIN_STRING;
            latestAttemptSucceeded = true;
        } else {
            lives--;
            double min = water_needed - margin;
            double max = water_needed + margin;
            String lose_str = String.format(LOSE_FORMAT, acid, min, max, water_provided);
            latestAttemptDescrip = lose_str;
            latestAttemptSucceeded = false;
        }
        if (lives > 0) {
            acid = rnd.nextInt(MAX_ACID) + 1; // set up for next round
        } else {
            gameInProgress = false;
        }
    }

    private String descript_format =
            "The fictitious chemical, kryptocyanic acid, can only be diluted " +
                    "by the ratio of %d parts water to %d parts acid. Any other ratio " +
                    "causes an unstable compound which soon explodes. Given an " +
                    "amount of acid, you must determine how much water to add " +
                    "for dilution. If you're more than %.0f%% off, you lose one of your " +
                    "%d lives. The game continues until you lose all %d lives.\n\n" +
                    "(based on the original game by Wayne Teeter)";

    public String getDescription() {
        String descript = String.format(descript_format,
                PARTS_WATER, PARTS_ACID, MARGIN_OF_ERROR * 100, MAX_LIVES, MAX_LIVES);
        return descript;
    }
}