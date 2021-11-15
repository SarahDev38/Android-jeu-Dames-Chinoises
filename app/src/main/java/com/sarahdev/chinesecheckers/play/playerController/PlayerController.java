package com.sarahdev.chinesecheckers.play.playerController;

import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.play.PlayPresenter;

import java.util.List;

public abstract class PlayerController {
    protected PlayPresenter _presenter;
    protected int _playerIndex;
    protected List<Player> _players;
    protected int _rounds;

    public PlayerController(PlayPresenter presenter, int playerIndex, List<Player> players, int rounds) {
        this._presenter = presenter;
        this._playerIndex = playerIndex;
        this._players = players;
        this._rounds = rounds;
    }

    public abstract void displayNewTurn();

    public abstract void treatSelection(MyPoint clicked);

    public abstract void findHelp(Boolean findHelp);

    public abstract void removeSelected();
}
