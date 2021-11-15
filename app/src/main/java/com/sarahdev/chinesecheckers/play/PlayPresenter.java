package com.sarahdev.chinesecheckers.play;

import android.content.Context;
import android.widget.Toast;

import com.sarahdev.chinesecheckers.BasePresenter;
import com.sarahdev.chinesecheckers.model.GameBoard;
import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.model.Type;
import com.sarahdev.chinesecheckers.play.playerController.ComputerController;
import com.sarahdev.chinesecheckers.play.playerController.HumanController;
import com.sarahdev.chinesecheckers.play.playerController.PlayerController;
import com.sarahdev.chinesecheckers.play.playerController.utils.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayPresenter extends BasePresenter {
    private final ViewActivity _viewActivity;
    private PlayerController _playerController;
    protected GameBoard gameBoard;
    private boolean _nextIsPossible = true;
    private boolean _helpRequired = false;
    private boolean _continueGame = false;
    private final List<Player> _winners = new ArrayList<>();
    private boolean _scoreSaved = false;

    public PlayPresenter(ViewActivity viewActivity) {
        this._viewActivity = viewActivity;
        readData();
        setGamePosition();
        initGame(getData().getPlayers());
    }

    public void initGame(List<Player> players) {
        gameBoard = new GameBoard(players);
        if (!getData().isInProgressGame() || getData().getRounds() == 0)
            for (Player player : getData().getPlayers())
                player.initMarblesPlace();
            getData().setInProgressGame(true);
            getData().setOpenGame(true);
    }

    public void startGame() {
        _playerController = getPlayerController();
        while (_playerController == null) {
            setNextPlayer();
        }
        _playerController.displayNewTurn();
        setBtnNextColor();
    }

    public void displayGame(int indexCurrent, boolean isHuman, List<MyPoint> way, int rounds) {
        if (!_helpRequired) {
                _viewActivity.displayGame(indexCurrent, isHuman, getMarblesMap(), way, rounds, getData().getCurrent().getName());
        } else
            findHelp(true);
    }

    public void treatPlayerChange() {
        if (getData().getResult(getData().getCurrentIndex()) == 10) {
            if (!_continueGame) {
                if (!_winners.contains(getData().getCurrent()))
                    _winners.add(getData().getCurrent());
                setGameScore(getData().getCurrentIndex());
                displayWinner();
            } else {
                if (!_winners.contains(getData().getCurrent()))
                    _winners.add(getData().getCurrent());
                if (_winners.size() >= getPlayersCount() - 1) {
                    getData().setInProgressGame(false);
                    getData().setOpenGame(false);
                    displayGameOver();
                } else {
                    setNextPlayer();
                    _helpRequired = false;
                    _playerController.displayNewTurn();
                    setBtnNextColor();
                }
            }
            saveData();
        } else if(_nextIsPossible) {
            saveData();
            setNextPlayer();
            getData().setInProgressGame(true);
            _helpRequired = false;
            _playerController.displayNewTurn();
            setBtnNextColor();
        } else
            _playerController.displayNewTurn();
    }

    protected void setNextPlayer() {
        final int former = getData().getCurrentIndex();
        int next = getNextIndex(getData().getCurrentIndex());
        getData().setCurrentIndex(next);
        if (next < former)
            getData().setRounds(getData().getRounds() + 1);
        _playerController = getPlayerController();
    }

    protected int getNextIndex(int former) {
        int newIndex = (former + 1) % 6;
        while ( (getData().getType(newIndex).equals(Type.NoOne) || getData().getPlayer(newIndex).tempResult() == 10)
                && (_winners.size() < getPlayersCount()) ) {
            newIndex = getNextIndex(newIndex);
        }
        return newIndex;
    }

    public void setBtnNextColor () {
        _viewActivity.setBtnNextColor(getData().getColor(getData().getCurrentIndex()), getNextColor(), getData().isCurrentHuman(), _nextIsPossible, getData().getIcon(getData().getCurrentIndex()));
        _viewActivity.setBtnHelpColor(getData().getColor(getData().getCurrentIndex()), getData().isCurrentHuman(), _helpRequired, getData().isHelpAllowed(), false);
    }

    public int getNextColor() {
        return getData().getColor(getNextIndex(getData().getCurrentIndex()));
    }

    private PlayerController getPlayerController() {
        switch (getData().getType(getData().getCurrentIndex())) {
            case Human:
                return new HumanController(this, getData().getCurrentIndex(), getData().getPlayers(), getData().getRounds());
            case Computer:
                return new ComputerController(this, getData().getCurrentIndex(), getData().getPlayers(), getData().getRounds());
            default :
                return null;
        }
    }

    public void treatSelection(MyPoint point) {
        if (GameBoard._nodes.contains(point))
            _playerController.treatSelection(point);
        else if (getData().isCurrentHuman() && !Position.isOnBorder(point)) {
            _playerController.removeSelected();
            _playerController.displayNewTurn();
        }
    }

    public void clickedHelp() {
        if (getData().isHelpAllowed()) {
            this._helpRequired = !_helpRequired;
            _viewActivity.setBtnHelpColor(getData().getColor(getData().getCurrentIndex()), getData().isCurrentHuman(), _helpRequired, true, true);
        findHelp(_helpRequired);
        } else
            Toast.makeText(_viewActivity.getContext(), "help is disabled", Toast.LENGTH_SHORT).show();
    }

    public void findHelp(Boolean findHelp) {
        _playerController.findHelp(findHelp);
    }

    public void showHelp(int indexCurrent, boolean isHuman, Map<Integer, List<MyPoint>> playersPegs, List<MyPoint> way, int rounds, List<MyPoint> possibles) {
        _viewActivity.displayHelp(indexCurrent,isHuman, playersPegs, way, rounds, possibles, getData().getCurrent().getName());
    }

    public void displayScores() {
        _viewActivity.dialogbox_scores(getData().getScores(), getData().getProfiles());}

    public void displayWinner() {
        _viewActivity.displayWinner(getData().getColor(getData().getCurrentIndex()), getData().getPlayers(), getData().getGameScore());
    }

    public void displayGameOver() {
        for (int i = 0 ; i < 6 ; i++)
            if (!_winners.contains(getData().getPlayer(i)) && !getData().getType(i).equals(Type.NoOne))
                _winners.add(getData().getPlayer(i));
        _viewActivity.displayGameOver(_winners, getData().getGameScore());
    }

    public void back() {
        getData().setOpenGame(false);
        _viewActivity.onBackPressed();
    }

    public void saveScore() {
        if (getData().isInProgressGame()) {
            addScores();
            getData().setInProgressGame(false);
            saveData();
        }
    }

    public void initPlayViewParams() {
       _viewActivity.initPlayview(GameBoard._nodesMap, getAllMarbles(), GameBoard._colors, getData().getDelay());
    }

    protected Map<Integer, List<MyPoint>> getAllMarbles() {
        Map<Integer, List<MyPoint>> marbles = new HashMap<>();
        for (Player player : getData().getPlayers())
            marbles.put(player.getColor(), player.getMarbles());
        return marbles;
    }

    public void setGamePosition() {
        setNames();
        List<Player>[] repartition = setRepartition();
        int count = repartition[0].size();
        setPlayersCount(count);
        switch (count) {
            case 2: getData().setPlayers(allocate(repartition, true, false, false, true, false, false)); break;
            case 3: getData().setPlayers(allocate(repartition, true, false, true, false, true, false)); break;
            case 4: getData().setPlayers(allocate(repartition, false, true, true, false, true, true)); break;
            case 5: getData().setPlayers(allocate(repartition, false, true, true, true, true, true)); break;
            case 6: getData().setPlayers(allocate(repartition, true, true, true, true, true, true)); break;
        }
        setGameNames();
    }

    private List<Player>[] setRepartition() {
        List<Player> playersList = new ArrayList<>();
        List<Player> noPlayersList = new ArrayList<>();
        for (Player player : getData().getPlayers()) {
            if (player.getType().equals(Type.NoOne))
                noPlayersList.add(player);
            else
                playersList.add(player);
        }
        return new List [] {playersList, noPlayersList};
    }

    private List<Player> allocate(List<Player>[] playersRepartition, Boolean... isPlayers) {
        List<Player> newPlayers = new ArrayList<>();
        int iPlayer = 0;
        int iNoPlayer = 0;
        for (Boolean isPlayer : isPlayers) {
            if (isPlayer)
                newPlayers.add(playersRepartition[0].get(iPlayer++));
            else
                newPlayers.add(playersRepartition[1].get(iNoPlayer++));
        }
        return newPlayers;
    }

    public void setNames() {
        for (Player player : getData().getPlayers()) {
            if (!player.getType().equals(Type.Human))
                player.setName("");
            else if (player.getName().equals(""))
                player.setName("joueur inconnu");
        }
    }

    public void setPlayers(List<Player> players) {getData().setPlayers(players);}
    public void setNextIsPossible(Boolean nextIsPossible) {this._nextIsPossible = nextIsPossible;}
    public void setRunningGame(boolean rg) {getData().setInProgressGame(rg);}
    public void setContinueGame(boolean continueGame) {this._continueGame = continueGame;}
    public void setScoreSaved(boolean scoreSaved) {this._scoreSaved = scoreSaved;}
    public boolean isScoreSaved() {return _scoreSaved;}

// ---------------- implementation abstract class

    public Context getContext(){
        return _viewActivity.getContext();
    }

// ----------------------------------- ViewActivity --------------------------

    public interface ViewActivity {
        void initPlayview(Map<Integer, List<MyPoint>> nodesMap, Map<Integer, List<MyPoint>> playersPegs, List<Integer> colors, int delay);
        void displayGame(int indexCurrent, boolean isHuman, Map<Integer, List<MyPoint>> playersPegs, List<MyPoint> way, int rounds, String name);
        void displayWinner(int winnerColor, List<Player> players, Map<Integer, Integer> gameScores);
        void displayGameOver(List<Player> winners, Map<Integer, Integer> gameScores);
        void displayHelp(int indexCurrent, boolean isHuman, Map<Integer, List<MyPoint>> playersPegs, List<MyPoint> way, int rounds, List<MyPoint> possibles, String name);
        void setBtnNextColor(int currentIndex, int nextPlayerIndex, boolean isHuman, boolean nextIsPossible, int iconIndex);
        void setBtnHelpColor(int currentColorIndex, boolean isHuman, boolean helpRequired, boolean helpAllowed, boolean animate);
        Context getContext();
        void onBackPressed();
        void dialogbox_scores(Map<String, Integer> scores, Map<String, Integer> profiles);
    }
}
