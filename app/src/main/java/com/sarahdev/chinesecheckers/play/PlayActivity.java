package com.sarahdev.chinesecheckers.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.sarahdev.chinesecheckers.R;
import com.sarahdev.chinesecheckers.model.DataModel;
import com.sarahdev.chinesecheckers.model.Icon;
import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.BaseActivity;
import com.sarahdev.chinesecheckers.start.StartActivity;

import java.util.List;
import java.util.Map;

public class PlayActivity extends BaseActivity implements PlayPresenter.ViewActivity, View.OnTouchListener, SurfaceHolder.Callback {
    protected PlayPresenter _presenter;
    private PlayView _playView;
    private RelativeLayout _gameButtons;
    private SurfaceHolder _plateauHolder;
    public ImageView btnNext, btnHelp, btnNoHelp, btnBack, btnExit;
    private final int blackIndex = 0, whiteIndex = 3;
    private final int[] colorsTeams = new int[]{R.color.team0, R.color.team1, R.color.team2, R.color.team3, R.color.team4, R.color.team5};
    private final int computerIcon = R.drawable.persorobot36;
    private final int nextIcon = R.drawable.ic_forward_grey600_24dp, nextIconWhite = R.drawable.ic_forward_white_24dp;
    private final int helpIcon = R.drawable.ic_lightbulb_on_grey600, noHelpIcon = R.drawable.ic_do_not_disturb_grey600_18dp, helpIconWhite = R.drawable.ic_lightbulb_on_white50;
    private final int backIcon = R.drawable.ic_arrow_back_grey600;
    private final int exitIcon = R.drawable.ic_exit_grey600;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.play_layout);
        _presenter = new PlayPresenter(this);
        _presenter.initPlayViewParams();

        FrameLayout layout = findViewById(R.id.play_FrameLayout);
        initBtns();
        layout.addView(_playView);
        layout.addView(_gameButtons);
        setContentView(layout);

        _playView.getHolder().addCallback(this);
        _presenter.startGame();
        _playView.setOnTouchListener(this);
    }


    public void initPlayview(Map<Integer, List<MyPoint>> nodesMap, Map<Integer, List<MyPoint>> playersPegs, List<Integer> colors, int delay) {
        this._playView = new PlayView(this , nodesMap, playersPegs, colors, delay);
    }

    public void initBtns() {
        _gameButtons = new RelativeLayout(this);
        _gameButtons.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        btnNext = new ImageView(this);
        btnHelp = new ImageView(this);
        btnNoHelp = new ImageView(this);
        btnBack = new ImageView(this);
        btnExit = new ImageView(this);
        setBtnsParams(btnNext, 123456, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM, 0, 0, 30, 80);
        setBtnsParams(btnHelp, 123457, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM, 30, 0, 0, 80);
        setBtnsParams(btnNoHelp, 1234575, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM, 95, 0, 0, 55);
        setBtnsParams(btnBack, 123458, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, 30, 110, 0, 0);
        setBtnsParams(btnExit, 123459, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP, 0, 110, 30, 0);

        btnNext.setOnClickListener(v -> _presenter.treatPlayerChange());
        btnHelp.setOnClickListener(v -> _presenter.clickedHelp());
        btnBack.setOnClickListener(v -> _presenter.back());
        btnExit.setOnClickListener(v -> {
            _presenter.saveData();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });
    }

    public void displayWinner(int winnerColor, List<Player> players, Map<Integer, Integer> gameScores) {
        DialogFragment winnerDialog = new DialogFragment_Winner(this, winnerColor, players, gameScores);
        winnerDialog.show(getSupportFragmentManager(), DialogFragment_Winner.TAG);
    }

    @Override
    public void displayGameOver(List<Player> winners, Map<Integer, Integer> gameScores) {
        DialogFragment gameOverDialog = new DialogFragment_GameOver(this, winners, gameScores);
        gameOverDialog.show(getSupportFragmentManager(), DialogFragment_GameOver.TAG);
    }

    @Override
    public void displayHelp(int indexCurrent, boolean isHuman, Map<Integer, List<MyPoint>> playersPegs, List<MyPoint> way, int rounds, List<MyPoint> possibles, String name) {
        _playView.displayGame(indexCurrent, isHuman, playersPegs, way, rounds, name);
        _playView.setHelp(possibles);
        _playView.invalidate();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    public void displayGame(int indexCurrent, boolean isHuman, Map<Integer, List<MyPoint>> playersPegs, List<MyPoint> way, int rounds, String name) {
        _playView.displayGame(indexCurrent, isHuman, playersPegs, way, rounds, name);
        _playView.invalidate();
    }

    private void setBtnsParams(ImageView button, int btnId, int verbRule1, int verbRule2, int left, int top, int right, int bottom ) {
        button.setId(btnId);
        _gameButtons.addView(button);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(verbRule1,RelativeLayout.TRUE);
        params.addRule(verbRule2,RelativeLayout.TRUE);
        params.setMargins(left,top,right,bottom);
        button.setLayoutParams(params);
}

    @Override
    public void setBtnNextColor(int currentColorIndex, int nextPlayerIndex, boolean isHuman, boolean nextIsPossible, int iconIndex) {
        initButton(btnBack, R.color.secondaryColorDark, backIcon);
        initButton(btnExit, R.color.secondaryColorDark, exitIcon);

        if (!isHuman)
            initButton(btnNext, colorsTeams[currentColorIndex], computerIcon);
        else if (!nextIsPossible)
            initButton(btnNext, colorsTeams[currentColorIndex], Icon.miniId(iconIndex));
        else if (currentColorIndex == blackIndex)
            initButton(btnNext, colorsTeams[nextPlayerIndex], nextIconWhite);
        else
            initButton(btnNext, colorsTeams[nextPlayerIndex], nextIcon);
    }

    @Override
    public void setBtnHelpColor(int currentColorIndex, boolean isHuman, boolean helpClicked, boolean helpAllowed, boolean animate) {
        if (!isHuman) {
            btnHelp.setVisibility(View.INVISIBLE);
            btnNoHelp.setVisibility(View.INVISIBLE);
        } else {
            btnHelp.setVisibility(View.VISIBLE);
            if (!helpAllowed) {
                initButton(btnHelp, R.color.secondaryColorDark, helpIcon);
                initButton(btnNoHelp, R.color.secondaryColor, noHelpIcon);
                btnNoHelp.setVisibility(View.VISIBLE);
            } else  {
                btnNoHelp.setVisibility(View.INVISIBLE);
                if (helpClicked) {
                    if (animate)
                        btnHelp.animate().rotation(btnHelp.getRotation()+360).setDuration(1000).start();
                    if (currentColorIndex == whiteIndex)
                        initButton(btnHelp, colorsTeams[currentColorIndex], helpIcon);
                    else
                        initButton(btnHelp, colorsTeams[currentColorIndex], helpIconWhite);
                } else {
                    initButton(btnHelp, R.color.secondaryColor, helpIcon);
                }
            }
        }
    }

    private void initButton(ImageView button, int resourceColorId, int resourceIconId) {
        button.setBackgroundResource(R.drawable.ripple_round);
        RippleDrawable drawable = (RippleDrawable) button.getBackground();
        ColorStateList colorStateList = new ColorStateList(new int[][] { new int[] { } }, new int[]{getResources().getColor(resourceColorId)});
        drawable.setTintList(colorStateList);
        button.setImageResource(resourceIconId);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        _plateauHolder = surfaceHolder;
        Canvas drawingCanvas = _plateauHolder.lockCanvas();
        _playView.setParams(_presenter.getAllMarbles());
        _playView.setWillNotDraw(false);
        _playView.draw(drawingCanvas);
        _plateauHolder.unlockCanvasAndPost(drawingCanvas);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        _plateauHolder =null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        MyPoint touchPoint = _playView.getPointFromScreen((int) event.getX(), (int) event.getY());
        switch (event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                _presenter.treatSelection(touchPoint);
                _playView.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                _presenter.treatSelection(touchPoint);
                _playView.invalidate();
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        _presenter.saveData();
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


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

    @Override
    public void dialogbox_scores(Map<String, Integer> scores, Map<String, Integer> profiles) {
        DialogFragment scoresDialog = new DialogFragment_Scores(this, scores, profiles);
        scoresDialog.show(getSupportFragmentManager(), DialogFragment_Scores.TAG);
    }
}