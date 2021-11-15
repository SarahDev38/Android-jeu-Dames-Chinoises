package com.sarahdev.chinesecheckers.play;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
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
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.model.Icon;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.model.Type;

import java.util.List;
import java.util.Map;

public class DialogFragment_GameOver extends DialogFragment {
    public static String TAG = "GameOver";
    private final PlayActivity _playActivity;
    private final List<Player> _winners;
    private final Map<Integer, Integer> _gameScores;
    private final int podiumIcon = R.drawable.podium50, winnerIcon = R.drawable.winner100;
    private final int[] colorsTeams = new int[]{R.color.team0, R.color.team1, R.color.team2, R.color.team3, R.color.team4, R.color.team5};

    public DialogFragment_GameOver(PlayActivity playActivity, List<Player> winners, Map<Integer, Integer> gameScores) {
        this._winners = winners;
        this._playActivity = playActivity;
        this._gameScores = gameScores;
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_gameover_layout, null);

        ImageView imgHeader = view.findViewById(R.id.winnerImage3);
        imgHeader.setBackgroundResource(winnerIcon);

        LinearLayout layout = view.findViewById(R.id.linearLayoutWinners);
        for (Player player : _winners) {
            LinearLayout itemLayout = new LinearLayout(_playActivity.getApplicationContext());
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(20, 4, 0, 12);

            ImageView img = new ImageView(_playActivity.getApplicationContext());
            TextView tv = new TextView(_playActivity.getApplicationContext());

            img.setBackgroundResource(R.drawable.ripple_round);
            RippleDrawable drawable = (RippleDrawable) img.getBackground();
            ColorStateList colorStateList = new ColorStateList(new int[][] { new int[] { } }, new int[]{getResources().getColor(colorsTeams[player.getColor()])});
            drawable.setTintList(colorStateList);

            if (player.getType().equals(Type.Human)) {
                img.setImageResource(Icon.miniId(player.getIcon()));
                String score = " " + player.getName() + " : " + _gameScores.get(player.getColor()) + " pts";
                tv.setText(score);
            } else if (player.getType().equals(Type.Computer)){
                img.setImageResource(R.drawable.persorobot36);
                String score = " android" + " : " + _gameScores.get(player.getColor()) + " pts";
                tv.setText(score);
            }
            itemLayout.addView(img);

            tv.setTextSize(22);
            itemLayout.addView(tv);
            layout.addView(itemLayout);
        }

        layout.setPadding(0, 12, 0, 12);
        builder.setView(view);

        FloatingActionButton btnSave = view.findViewById(R.id.btnScores2);
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

        Button btnAgain = view.findViewById(R.id.btnPlayAgain2);
        btnAgain.setOnClickListener(v -> {
            _playActivity._presenter.setRunningGame(false);
            _playActivity._presenter.back();
            this.dismiss();
        });

        Button btnExit = view.findViewById(R.id.btnExit2);
        btnExit.setOnClickListener(v -> {
            _playActivity._presenter.setRunningGame(false);
            _playActivity.moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });

        builder.setCancelable(false);
        return builder.create();
    }
}
