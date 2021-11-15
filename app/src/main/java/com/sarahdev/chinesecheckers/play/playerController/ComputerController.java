package com.sarahdev.chinesecheckers.play.playerController;

import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.play.PlayPresenter;

import java.util.List;

public class ComputerController extends PlayerController {
    private final ComputerMoveManager _computerMoveManager;

    public ComputerController(PlayPresenter presenter, int playerIndex, List<Player> players, int rounds) {
        super(presenter, playerIndex, players, rounds);
        _computerMoveManager = new ComputerMoveManager(players.get(playerIndex), players);
    }

    @Override
    public void displayNewTurn() {
        List<MyPoint> way = _computerMoveManager.plotWay();
        _presenter.displayGame(_playerIndex, false, way, _rounds);
        _players.get(_playerIndex).move(way.get(0), way.get(way.size() - 1));
        _presenter.setPlayers(_players);
        _presenter.setNextIsPossible(true);
    }

    @Override
    public void treatSelection(MyPoint clicked) {
    }

    @Override
    public void findHelp(Boolean findHelp) {
    }

    @Override
    public void removeSelected() {
    }
}
