package com.sarahdev.chinesecheckers.start;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sarahdev.chinesecheckers.BaseActivity;
import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.model.DataModel;
import com.sarahdev.chinesecheckers.model.Icon;
import com.sarahdev.chinesecheckers.play.PlayActivity;

import java.util.List;

import static android.widget.Toast.makeText;

public class StartActivity extends BaseActivity implements StartPresenter.ViewActivity {
    private StartPresenter _presenter;
    private String[] _savedNames = {""};
    private ImageView btnAdd;
    private final AutoCompleteTextView[] nameText = new AutoCompleteTextView[6];
    private final ImageView[] btnIconPlayer = new ImageView[6];
    private final ImageView[] btnHidePlayer = new ImageView[6];
    private final int[] id_nameText = new int [] {R.id.name0, R.id.name1, R.id.name2, R.id.name3, R.id.name4, R.id.name5};
    private final int[] id_btnPlayer = new int [] {R.id.btnPlayer0, R.id.btnPlayer1, R.id.btnPlayer2, R.id.btnPlayer3, R.id.btnPlayer4, R.id.btnPlayer5};
    private final int[] id_btnDeletePlayer = new int [] {R.id.btnDeletePlayer0, R.id.btnDeletePlayer1, R.id.btnDeletePlayer2, R.id.btnDeletePlayer3, R.id.btnDeletePlayer4, R.id.btnDeletePlayer5};
    private final int humanFont = R.font.chelsea_market_regular, computerFont = R.font.audiowide_regular;
    private final int[] teamColor = new int [] {R.color.team0, R.color.team1, R.color.team2, R.color.team3, R.color.team4, R.color.team5};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();

        btnAdd = findViewById(R.id.btnAdd);
        _presenter = new StartPresenter(this);
        initWidgets();
        _presenter.displayPlayers();

        RelativeLayout layout = findViewById(R.id.start_relativelayout);
        layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                _presenter.startGame();
             }
            @Override
            public void onSwipeRight() {
                dialogbox_settings(getData().getDelay(), getData().isHelpAllowed());
            }
            @Override
            public void onSwipeDown() {
                _presenter.displayProfile();
            }
            @Override
            public void onSwipeUp() {
                dialogbox_scores(getData().getScores(), getData().getProfiles());
            }
        });

    }
    public void setSavedNames(List<String> names){
        this._savedNames = new String[names.size()];
        for (int i = 0; i < _savedNames.length; i++)
            _savedNames[i] = names.get(i);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initWidgets() {
        for (int colorIndex = 0 ; colorIndex < 6; colorIndex++) {
            btnIconPlayer[colorIndex] = findViewById(id_btnPlayer[colorIndex]);
            btnIconPlayer[colorIndex].setOnClickListener(new MyTypeListener(colorIndex));
            btnIconPlayer[colorIndex].setOnLongClickListener(new MyOnLongClickListener(colorIndex));

            btnHidePlayer[colorIndex] = findViewById(id_btnDeletePlayer[colorIndex]);
            btnHidePlayer[colorIndex].setOnClickListener(new MyDeleteListener(colorIndex));

            nameText[colorIndex] = findViewById(id_nameText[colorIndex]);
            nameText[colorIndex].setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, _savedNames));
            nameText[colorIndex].setThreshold(1);
            nameText[colorIndex].addTextChangedListener(new MyTextWatcher(colorIndex));
            nameText[colorIndex].setOnItemClickListener(new MyOnItemClickListener(colorIndex));
            nameText[colorIndex].setOnFocusChangeListener((v, hasFocus) -> {
                EditText et = findViewById(v.getId());
                if (hasFocus && et.getHint().equals(getResources().getString(R.string.name)))
                    et.setHint("");
                else if (!hasFocus && et.getText().toString().trim().equals(""))
                    et.setHint(getResources().getString(R.string.name));
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void displayPlayer(int iconIndex, int colorIndex, String name, String playerType_string) {
        btnIconPlayer[colorIndex].setVisibility(View.VISIBLE);
        btnIconPlayer[colorIndex].setBackgroundTintList(ContextCompat.getColorStateList(this, teamColor[colorIndex]));

        nameText[colorIndex].setVisibility(View.VISIBLE);
        btnHidePlayer[colorIndex].setVisibility(View.VISIBLE);
         if (playerType_string.equals("Human")) {
             btnIconPlayer[colorIndex].setImageResource(Icon.id(iconIndex));
             nameText[colorIndex].setText(name);
             nameText[colorIndex].setTypeface(getResources().getFont(humanFont));
             nameText[colorIndex].setFocusableInTouchMode(true);
         } else {
             btnIconPlayer[colorIndex].setImageResource(Icon.COMPUTER.id());
             nameText[colorIndex].setText(getResources().getString(R.string.android_player));
             nameText[colorIndex].setTypeface(getResources().getFont(computerFont));
             nameText[colorIndex].setFocusable(false);
        }
    }

    @Override
    public void hidePlayer(int colorIndex) {
        btnIconPlayer[colorIndex].setVisibility(View.GONE);
        btnHidePlayer[colorIndex].setVisibility(View.GONE);
        nameText[colorIndex].setVisibility(View.GONE);
    }

    @Override
    public void showERRORMSG(Integer errorType){ makeText(this, getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show(); }

    @Override
    public void hideAddButton(boolean hidden) { btnAdd.setVisibility((hidden) ? View.INVISIBLE : View.VISIBLE); }

    @Override
    public void openPlayActivity() {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() { return getApplicationContext(); }

    //  ---------------------    REDIRECT PRESENTER   ---------------------------

    @Override
    public void saveData() {
        _presenter.saveData();
    }
    @Override
    public DataModel getData() { return _presenter.getData(); }
    @Override
    public void deleteScore() {
        _presenter.deleteScore();
    }
    public void initGameParams() {
        _presenter.initGameParams();}
    public void saveProfileName(String name, int iconIndex) {
        _presenter.updateProfile(name, iconIndex);}
    public void savePlayerProfile(int index, boolean isHuman, String name, int color, int icon) { _presenter.updatePlayer(index, isHuman, name, color, icon); }
    public void onClick_RESET(View v) { _presenter.initView(); }
    public void onClick_ADD(View v) { _presenter.addPlayer(); }
    public void onClick_START(View v) { _presenter.startGame(); }

    //  ---------------------    dialog boxes   ---------------------------

    public void dialogbox_getBackGame() {
        DialogFragment getBackGameDialog = new DialogFragment_FinishGame.GetBackGameDialogFragment(this);
        getBackGameDialog.show(getSupportFragmentManager(), DialogFragment_FinishGame.GetBackGameDialogFragment.TAG);
    }

    public void dialogbox_playerProfile(String name, int index, int color, int icon, boolean isHuman) {
        DialogFragment playerDialog = new DialogFragment_PlayerProfile(this, name, index, color, icon, isHuman);
        playerDialog.show(getSupportFragmentManager(), DialogFragment_PlayerProfile.TAG);
    }

    // ---------------------    LISTENER INNER CLASSES   ---------------------------

    private class MyOnLongClickListener implements View.OnLongClickListener {
        private final int color;
        public MyOnLongClickListener(int color) {
            this.color = color;
        }
        @Override
        public boolean onLongClick(View v) {
            _presenter.displayPlayerProfile(color);
            return true;
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private final int color;
        public MyTextWatcher(int color) { this.color = color;}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            nameText[color].setBackgroundColor(0x00000000);
            _presenter.updatePlayerName(color, s.toString());
        }
        @Override
        public void afterTextChanged(Editable s) { }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {
        private final int color;
        public MyOnItemClickListener(int color) { this.color = color; }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            nameText[color].dismissDropDown();
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (view != null) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private class MyTypeListener implements View.OnClickListener {
        private final int color;
        public MyTypeListener(int color) { this.color = color;}
        @Override
        public void onClick(View v) { _presenter.changePlayerTypeByColor(color); }
    }

    private class MyDeleteListener implements View.OnClickListener {
        private final int color;
        public MyDeleteListener (int color) { this.color = color;}
        @Override
        public void onClick(View v) { _presenter.deletePlayer(color); }
    }
}