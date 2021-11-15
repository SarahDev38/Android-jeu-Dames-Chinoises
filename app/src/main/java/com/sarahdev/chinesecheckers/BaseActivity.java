package com.sarahdev.chinesecheckers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.sarahdev.chinesecheckers.model.DataModel;
import com.sarahdev.chinesecheckers.play.DialogFragment_Scores;
import com.sarahdev.chinesecheckers.start.DialogFragment_Profile;
import com.sarahdev.chinesecheckers.start.DialogFragment_Settings;
import com.sarahdev.chinesecheckers.start.RulesActivity;

import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {

    public void saveProfileName(String name, int iconIndex) { }
    public void saveDelay(int delay) {
        getData().setDelay(delay);
        saveData();
    }

    public void saveHelp(boolean help) {
        getData().setHelpAllowed(help);
        saveData();
    }

    public abstract void deleteScore();

    public abstract void saveData();

    public abstract DataModel getData();

    // ---------------------    MENU   ---------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profil:
                String name = getData().getProfileName();
                dialogbox_profile(name, getData().getProfiles().get(name));
                return true;
            case R.id.menu_rules:
                Intent intent = new Intent(this, RulesActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_scores:
                dialogbox_scores(getData().getScores(), getData().getProfiles());
                return true;
            case R.id.menu_settings:
                dialogbox_settings(getData().getDelay(), getData().isHelpAllowed());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // ---------------------    DIALOG BOX   ---------------------------

    public void dialogbox_profile(String profileName, int iconIndex) {
        DialogFragment profileDialog = new DialogFragment_Profile(this, profileName, iconIndex);
        profileDialog.show(getSupportFragmentManager(), DialogFragment_Profile.TAG);
    }

    public void dialogbox_scores(Map<String, Integer> scores, Map<String, Integer> profiles) {
        DialogFragment scoresDialog = new DialogFragment_Scores(this, scores, profiles);
        scoresDialog.show(getSupportFragmentManager(), DialogFragment_Scores.TAG);
    }

    public void dialogbox_settings(int setting_delay, boolean setting_help) {
        DialogFragment settingsDialog = new DialogFragment_Settings(this, setting_delay, setting_help);
        settingsDialog.show(getSupportFragmentManager(), DialogFragment_Scores.TAG);
    }

    public abstract Context getContext();
}