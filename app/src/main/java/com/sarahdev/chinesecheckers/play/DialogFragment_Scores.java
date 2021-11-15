package com.sarahdev.chinesecheckers.play;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.BaseActivity;
import com.sarahdev.chinesecheckers.model.Icon;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class DialogFragment_Scores extends DialogFragment {
    public static String TAG = "Scores";
    private final BaseActivity _activity;
    private Map<String, Integer> _scores;
    private final Map<String, Integer> _profiles;
    private ImageButton btnDelete;
    private final int podiumIcon = R.drawable.podium50;
    private View view;

    public DialogFragment_Scores(BaseActivity activity, Map<String, Integer> scores, Map<String, Integer> profiles) {
        this._activity = activity;
        if ((scores == null) || scores.size() == 0 )
            this._scores = null;
        else
            this._scores = order(scores);
        this._profiles = profiles;
    }

    public Map<String, Integer> getScores() {
        return _scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this._scores = scores;
    }

    private LinkedHashMap<String, Integer> order(Map<String, Integer> gscores) {
        LinkedHashMap<String, Integer> ordscores = new LinkedHashMap<>();
        Map<String, Integer> scorestemp = new HashMap<>(gscores);
        while (scorestemp.size()>0) {
            int maxValue = Collections.max(scorestemp.values());
            String maxkey = getFirstKey(scorestemp, maxValue);
            ordscores.put(maxkey, maxValue);
            scorestemp.remove(maxkey);
        }
        return ordscores;
    }

    private String getFirstKey(Map<String, Integer> gScores, Integer value) {
        for (Map.Entry<String, Integer> entry : gScores.entrySet()) {
            if (entry.getValue().equals(value))
                return entry.getKey();
        }
        return null;
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        this.view = inflater.inflate(R.layout.dialog_scores_layout, null);

        ImageView imgHeader = view.findViewById(R.id.score_image);
        imgHeader.setBackgroundResource(podiumIcon);

        initScoresTv(view);
        builder.setView(view);

        FloatingActionButton btnOK = view.findViewById(R.id.score_btnOK);
        btnOK.setOnClickListener(v -> this.dismiss() );

        btnDelete = view.findViewById(R.id.score_btnDelete);
        if (_scores == null)
            btnDelete.setVisibility(View.INVISIBLE);
        btnDelete.setOnClickListener(v -> {
            MyDialogInterfaceClickListener dialog = new MyDialogInterfaceClickListener(_activity);
            dialog.showAlertDialog();
        });

        builder.setCancelable(false);
        return builder.create();
    }

    public void initScoresTv(View view) {
        LinearLayout layout = view.findViewById(R.id.score_linearLayout);
        layout.removeAllViewsInLayout();
        Typeface typeface = ResourcesCompat.getFont(_activity.getApplicationContext(), R.font.chelsea_market);
        if (_scores != null) {
            for (String playerName : _scores.keySet()) {
                LinearLayout itemLayout = new LinearLayout(_activity.getApplicationContext());
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setPadding(20, 4, 0, 12);

                ImageView img = new ImageView(_activity.getApplicationContext());
                TextView tv = new TextView(_activity.getApplicationContext());
                tv.setTypeface(typeface);
                if (!playerName.equals("android")) {
                    Integer iconIndex = _profiles.get(playerName);
                    if (iconIndex == null)
                        iconIndex =0;
                    img.setImageResource(Icon.miniId(iconIndex));
                } else
                    img.setImageResource(R.drawable.persorobot36);

                if (_scores.get(playerName) > 0) {
                    String score = " " + playerName + " : +" + _scores.get(playerName) + " pts";
                    tv.setText(score);
                } else {
                    String score = " " + playerName + " : " + _scores.get(playerName) + " pts";
                    tv.setText(score);
                }
                itemLayout.addView(img);

                tv.setTextSize(22);
                itemLayout.addView(tv);
                layout.addView(itemLayout);
            }
        } else {
            TextView tv = new TextView(_activity.getApplicationContext());
            String noScore = getResources().getString(R.string.no_recorded_score);
            tv.setText(noScore);
            tv.setTextSize(22);
            tv.setTypeface(typeface);
            tv.setGravity(Gravity.CENTER);
            layout.addView(tv);
        }

        Space space = new Space(_activity.getApplicationContext());
        space.setMinimumHeight(80);
        layout.addView(space);

        layout.setPadding(6, 12, 0, 12);
    }

    private class MyDialogInterfaceClickListener implements AlertDialog.OnClickListener {
        private final BaseActivity activity;

        public MyDialogInterfaceClickListener(BaseActivity activity) {
            this.activity = activity;
        }

        public void showAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("delete");
            builder.setMessage("Are you sure you want to delete scores ?");
            builder.setPositiveButton(android.R.string.yes, this);
            builder.setNegativeButton(android.R.string.cancel, this);
            builder.setCancelable(true);
            builder.create().show();
        }

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
                case BUTTON_POSITIVE:
                    activity.deleteScore();
                    activity.saveData();
                    dialog.dismiss();
                    setScores(null);
                    initScoresTv(view);
                    btnDelete.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }
}


