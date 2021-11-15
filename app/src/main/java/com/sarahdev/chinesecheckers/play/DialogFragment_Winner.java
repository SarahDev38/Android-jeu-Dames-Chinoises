package com.sarahdev.chinesecheckers.play;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.model.Icon;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.model.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogFragment_Winner extends DialogFragment {
    public static String TAG = "Results";
    private final PlayActivity _playActivity;
    private final int _winnerColor;
    private final Map<Integer, Integer> _gameScores;
    private final List<Player> _players;
    private final int blackIndex =0;
    private final int championCupIcon = R.drawable.gagnant100, podiumIcon = R.drawable.podium50;
    private final int[] colorsTeams = new int [] {R.color.team0, R.color.team1, R.color.team2, R.color.team3, R.color.team4, R.color.team5};

    public DialogFragment_Winner(PlayActivity playActivity, int winnerColor, List<Player> players, Map<Integer, Integer> gameScores) {
        this._winnerColor = winnerColor;
        this._playActivity = playActivity;
        this._gameScores = gameScores;
        this._players = setInOrder(players, gameScores);
    }

    private List<Player> setInOrder(List<Player> players, Map<Integer, Integer> gScores) {
        List<Player> playerstemp = new ArrayList<>();
        Map<Integer, Integer> scores = new HashMap<>(gScores);
        while (scores.size()>0) {
            int maxValue = Collections.max(scores.values());
            int maxkey = getFirstKey(scores, maxValue);
            playerstemp.add(getPlayerOfColor(players, maxkey));
            scores.remove(maxkey);
        }
        return playerstemp;
    }

    private Integer getFirstKey(Map<Integer, Integer> gScores, Integer value) {
        for (Map.Entry<Integer, Integer> entry : gScores.entrySet()) {
            if (entry.getValue().equals(value))
                return entry.getKey();
        }
        return null;
    }

    private Player getPlayerOfColor(List<Player> players, int color) {
        for (Player p : players) {
            if (p.getColor() == color)
                return p;
        }
        return null;
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_winner_layout, null);

        LinearLayout layoutW = view.findViewById(R.id.winnerLayout);
        layoutW.setBackgroundColor(ContextCompat.getColor(getContext(), colorsTeams[_winnerColor]));

        ImageView imgChampion = view.findViewById(R.id.winnerImage);
        imgChampion.setBackgroundResource(championCupIcon);
        TextView header = view.findViewById(R.id.textViewheader);
        if (_winnerColor == blackIndex) {
            header.setTextColor(Color.WHITE);
        }

        LinearLayout layout = view.findViewById(R.id.linearLayoutWinner);
        for (Player player : _players) {
            if (!player.getType().equals(Type.NoOne)) {
                LinearLayout itemLayout = new LinearLayout(_playActivity.getApplicationContext());
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(20, 4, 0, 12);

                ImageView img = new ImageView(_playActivity.getApplicationContext());
                TextView tv = new TextView(_playActivity.getApplicationContext());

                tv.setPadding(6, 14, 0, 0);
                img.setBackgroundResource(R.drawable.ripple_round);
                RippleDrawable drawable = (RippleDrawable) img.getBackground();
                ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{}}, new int[]{getResources().getColor(colorsTeams[player.getColor()])});
                drawable.setTintList(colorStateList);

                if (player.getType().equals(Type.Human)) {
                    img.setImageResource(Icon.miniId(player.getIcon()));
                    String score = " " + player.getName() + " : " + _gameScores.get(player.getColor()) + " pts";
                    tv.setText(score);
                } else {
                    img.setImageResource(R.drawable.persorobot36);
                    String score = " android" + " : " + _gameScores.get(player.getColor()) + " pts";
                    tv.setText(score);
                }
                itemLayout.addView(img);

                tv.setTextSize(22);
                itemLayout.addView(tv);
                layout.addView(itemLayout);
            }
        }

        layout.setPadding(0, 12, 0, 12);
        builder.setView(view);

        FloatingActionButton btnSave = view.findViewById(R.id.btnSave);

        if (_playActivity._presenter.isScoreSaved())
            btnSave.setImageResource(podiumIcon);
        btnSave.setOnClickListener(v -> {
            if (_playActivity._presenter.isScoreSaved())
                _playActivity._presenter.displayScores();
            else {
                _playActivity._presenter.saveScore();
                Toast.makeText(_playActivity.getApplicationContext(), "scores saved", Toast.LENGTH_SHORT).show();
            }
            _playActivity._presenter.setScoreSaved(true);
            btnSave.setImageResource(podiumIcon);
        });


        Button btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> {
            _playActivity._presenter.setContinueGame(true);
            _playActivity._presenter.treatPlayerChange();
            this.dismiss();
        });
        if (_gameScores.size() < 3)
            btnContinue.setVisibility(View.GONE);

        Button btnAgain = view.findViewById(R.id.btnOKAgain);
        btnAgain.setOnClickListener(v -> {
            _playActivity._presenter.setRunningGame(false);
            _playActivity._presenter.back();
            this.dismiss();
        });

        Button btnExit = view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> {
            _playActivity._presenter.setRunningGame(false);
            _playActivity.moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });
/*
        TextView title = new TextView(getContext());
        String strTitle = (winnerName.equals(""))? (getResources().getString(R.string.well_done)) : (winnerName + " " + getResources().getString(R.string.a_gagne));
        title.setText(strTitle);
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        builder.setCustomTitle(title);*/
        builder.setCancelable(false);
        return builder.create();
    }
}
