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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarahdev.chinesecheckers.BaseActivity;
import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.model.Icon;

public class DialogFragment_Profile extends DialogFragment {
    public static String TAG = "MAIN_PROFILE";
    private final BaseActivity _activity;
    private final String _name;
    private int _icon;
    private final ImageButton[] iconButtons = new ImageButton[6];

    public DialogFragment_Profile(BaseActivity activity, String name, int iconIndex) {
        this._activity = activity;
        this._name = name;
        this._icon = iconIndex;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.dialog_profile_layout, null);

        TableLayout table_icons = view.findViewById(R.id.profile_table_icon);
        for (int r = 0; r < 2; r++) {
            TableRow row = (TableRow) table_icons.getChildAt(r);
            if (row instanceof TableRow) {
                for (int c = 0; c < 3; c++) {
                    ImageButton button = (ImageButton) row.getChildAt(c);
                    if (button instanceof ImageButton) {
                        int index = c + (3 * r);
                        iconButtons[index] = button;
                        iconButtons[index].setImageResource(Icon.id(index));
                        iconButtons[index].setOnClickListener(new DialogFragment_Profile.MyIconClickListener(getContext(), index));
                    }
                }
            }
        }

        iconButtons[_icon].setBackgroundTintList(ContextCompat.getColorStateList(_activity.getContext(), R.color.gold));

        EditText tvName = view.findViewById(R.id.profile_name);
        tvName.setText(_name);

        builder.setView(view);

        FloatingActionButton btnOK = view.findViewById(R.id.profile_btnOK);
        btnOK.setOnClickListener(v -> {
            _activity.saveProfileName(tvName.getText().toString(), _icon);
            this.dismiss();
        });

        builder.setCancelable(false);
        return builder.create();
    }

    private class MyIconClickListener implements View.OnClickListener {
        private final int _icon;
        private final Context _context;
        public MyIconClickListener(Context context, int icon) {
            this._context = context;
            this._icon = icon;
        }
        @Override
        public void onClick(View v) {
                iconButtons[DialogFragment_Profile.this._icon].setBackgroundTintList(ContextCompat.getColorStateList(_context, R.color.secondaryColor));
                DialogFragment_Profile.this._icon = _icon;
                iconButtons[DialogFragment_Profile.this._icon].setBackgroundTintList(ContextCompat.getColorStateList(_context, R.color.gold));
        }
    }
}