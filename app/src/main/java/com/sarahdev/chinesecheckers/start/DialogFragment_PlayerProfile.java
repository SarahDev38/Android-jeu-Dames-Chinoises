package com.sarahdev.chinesecheckers.start;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.model.Icon;

public class DialogFragment_PlayerProfile extends DialogFragment {
    public static String TAG = "PLAYER_PROFILE";
    private final StartActivity activity;
    private final int computerIcon = Icon.COMPUTER.id();
    private int icon, headerIconIndex, color;
    private String name="";
    private boolean isHuman;
    private final int index;
    private final ImageButton[] colorButtons = new ImageButton[6];
    private final ImageButton[] iconButtons = new ImageButton[6];
    private ImageButton imgHuman, imgComputer;

    public DialogFragment_PlayerProfile(StartActivity activity, String name, int index, int color, int icon, boolean isHuman) {
        this.activity = activity;
        if (isHuman)
            this.name = name;
        this.index = index;
        this.color = color;
        this.icon = icon;
        this.isHuman = isHuman;
        this.headerIconIndex = (isHuman) ? icon : computerIcon;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_player_layout, null);

        EditText tvName = view.findViewById(R.id.player_name);
        if (isHuman)
            tvName.setText(name);
        else {
            String androidName = "android";
            tvName.setText(androidName);
        }

        imgComputer = view.findViewById(R.id.player_imgcomputer);
        imgHuman = view.findViewById(R.id.player_imghuman);

        imgComputer.setOnClickListener(v -> {
            headerIconIndex = computerIcon;
            isHuman = false;
            String androidName = "android";
            tvName.setText(androidName);
            colorButtons[color].setImageResource(computerIcon);
            imgComputer.setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.gold));
            imgHuman.setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.fond));
            iconButtons[icon].setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.secondaryColor));
        });
        imgHuman.setOnClickListener(v -> {
            headerIconIndex = icon;
            isHuman = true;
            tvName.setText(name);
            colorButtons[color].setImageResource(Icon.id(icon));
            imgHuman.setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.gold));
            imgComputer.setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.fond));
            iconButtons[icon].setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.gold));
        });

        TableLayout table_icons = view.findViewById(R.id.player_table_icon);
        for (int r = 0; r < 2; r++) {
            TableRow row = (TableRow) table_icons.getChildAt(r);
            if (row instanceof TableRow) {
                for (int c = 0; c < 3; c++) {
                    ImageButton button = (ImageButton) row.getChildAt(c);
                    if (button instanceof ImageButton) {
                        int index = c + (3 * r);
                        iconButtons[index] = button;
                        iconButtons[index].setImageResource(Icon.id(index));
                        iconButtons[index].setOnClickListener(new MyIconClickListener(getContext(), index));
                    }
                }
            }
        }

        imgComputer.setImageResource(computerIcon);
        imgHuman.setImageResource(Icon.id(icon));
        if (isHuman) {
            iconButtons[icon].setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.gold));
            imgHuman.setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.gold));
        } else
            imgComputer.setBackgroundTintList(ContextCompat.getColorStateList(activity.getContext(), R.color.gold));


        TableLayout table_colors = view.findViewById(R.id.player_table_colors);
        for (int r = 0; r < 2; r++) {
            TableRow row = (TableRow) table_colors.getChildAt(r);
            if (row instanceof TableRow) {
                for (int c = 0; c < 3; c++) {
                    ImageButton button = (ImageButton) row.getChildAt(c);
                    if (button instanceof ImageButton) {
                        int index = c + (3 * r);
                        colorButtons[index] = button;
                        colorButtons[index].setOnClickListener(new MyColorClickListener(index));
                    }
                }
            }
        }
        colorButtons[color].setImageResource(Icon.id(headerIconIndex));

        builder.setView(view);

        FloatingActionButton btnOK = view.findViewById(R.id.player_btnOK);
        btnOK.setOnClickListener(v -> {
            activity.savePlayerProfile(index, isHuman, tvName.getText().toString(), color, icon);
            this.dismiss();
        });

        builder.setCancelable(false);
        return builder.create();
    }

    class MyIconClickListener implements View.OnClickListener {
        private final int _iconIndex;
        private final Context _context;
        public MyIconClickListener(Context context, int iconIndex) {
            this._context = context;
            this._iconIndex = iconIndex;
        }
        @Override
        public void onClick(View v) {
            if (isHuman) {
                iconButtons[icon].setBackgroundTintList(ContextCompat.getColorStateList(_context, R.color.secondaryColor));
                icon = _iconIndex;
                headerIconIndex = _iconIndex;
                iconButtons[icon].setBackgroundTintList(ContextCompat.getColorStateList(_context, R.color.gold));
                imgHuman.setImageResource(Icon.id(icon));
                colorButtons[color].setImageResource(Icon.id(icon));
            }
        }
    }

    private class MyColorClickListener implements View.OnClickListener {
        private final int _colorIndex;
        public MyColorClickListener(int colorIndex) {
            this._colorIndex = colorIndex;
        }
        @Override
        public void onClick(View v) {
            colorButtons[DialogFragment_PlayerProfile.this.color].setImageResource(0);
            DialogFragment_PlayerProfile.this.color = _colorIndex;
            colorButtons[DialogFragment_PlayerProfile.this.color].setImageResource(Icon.id(headerIconIndex));
        }
    }
}
