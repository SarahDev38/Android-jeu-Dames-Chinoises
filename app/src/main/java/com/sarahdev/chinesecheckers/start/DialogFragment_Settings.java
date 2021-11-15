package com.sarahdev.chinesecheckers.start;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.model.DataModel;
import com.sarahdev.chinesecheckers.BaseActivity;

public class DialogFragment_Settings extends DialogFragment {
    private final BaseActivity _activity;
    private int _delay;
    private boolean _help;
    private final int DELAYS_AMOUNT = 4;
    private TextView tvDelayChoice;
    private final int[] delayText = new int [] {R.string.veryFastSpeed, R.string.fastSpeed, R.string.normalSpeed, R.string.slowSpeed, R.string.verySlowSpeed};

    public DialogFragment_Settings(BaseActivity activity, int delay, boolean help) {
        this._activity = activity;
        this._delay = delay;
        this._help = help;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.settings_layout, null);

        tvDelayChoice = view.findViewById(R.id.textViewSpeedRep);
        int d = Math.round((float)(_delay *DELAYS_AMOUNT/DataModel.MAX_DELAY));
        tvDelayChoice.setText(getResources().getString(delayText[d]));

        SeekBar seekBar = view.findViewById(R.id.seekBarDelay);
        seekBar.setMin(0);
        seekBar.setMax(4);
        seekBar.setProgress(d);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                _delay = progressValue*(DataModel.MAX_DELAY/DELAYS_AMOUNT) + 5;
                int d = Math.round((float)(_delay *DELAYS_AMOUNT/DataModel.MAX_DELAY));
                tvDelayChoice.setText(getResources().getString(delayText[d]));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        CheckBox cbHelp = view.findViewById(R.id.checkBox);
        cbHelp.setChecked(_help);
        cbHelp.setOnCheckedChangeListener((buttonView, isChecked) -> _help = isChecked);

        ImageButton btnOK = view.findViewById(R.id.okProfile);
        btnOK.setOnClickListener(v -> {
            _activity.saveDelay(_delay);
            _activity.saveHelp(_help);
            this.dismiss();
        });


        builder.setView(view);
        builder.setCancelable(false);
        return builder.create();
    }

}