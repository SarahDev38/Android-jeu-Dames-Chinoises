package com.sarahdev.chinesecheckers.start;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sarahdev.chinesecheckers.R;

public class DialogFragment_FinishGame {
    public static class GetBackGameDialogFragment extends DialogFragment {
        public static String TAG = "FINISH";
        private StartActivity _startActivity;
        private int gameIcon = R.drawable.game70;

        public GetBackGameDialogFragment(StartActivity startActivity) {
            this._startActivity = startActivity;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = inflater.inflate(R.layout.dialog_reopen_layout, null);
            builder.setView(view);

            ImageView myImage = view.findViewById(R.id.gameImage);
            myImage.setImageResource(gameIcon);

            Button btnGetBack = view.findViewById(R.id.btnOKGetBack);
            btnGetBack.setOnClickListener(v -> {
                _startActivity.onClick_START(v);
                this.dismiss();
            });

            Button btnNewGame = view.findViewById(R.id.btnNewGame);
            btnNewGame.setOnClickListener(v -> {
                _startActivity.initGameParams();
                this.dismiss();
            });

            builder.setCancelable(true);
            return builder.create();
        }

    }
}
