package com.sarahdev.chinesecheckers.play.playerController;

import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.play.PlayPresenter;

import java.util.ArrayList;
import java.util.List;

public class HumanController extends PlayerController {
    private final HumanMoveManager _humanMoveManager;
    private List<MyPoint> _way = new ArrayList<>();

    public HumanController(PlayPresenter presenter, int playerIndex, List<Player> players, int rounds) {
        super(presenter, playerIndex, players, rounds);
        _humanMoveManager = new HumanMoveManager(players.get(playerIndex), players);
    }

    @Override
    public void displayNewTurn() {
        _humanMoveManager.initSelected();
        _presenter.displayGame(_playerIndex, true, _way, _rounds);
        _presenter.setNextIsPossible(false);
    }

    @Override
    public void treatSelection(MyPoint clicked) {
        if (_players.get(_playerIndex).getMarbles().contains(clicked)) {
            _way = _humanMoveManager.changeSelected(clicked);
        } else
            _way = _humanMoveManager.moveSelected(clicked);
        _presenter.displayGame(_playerIndex, true, _way, _rounds);
        _presenter.setNextIsPossible(_humanMoveManager.isPossibleEndOfTurn());
        _presenter.setBtnNextColor();
    }

    @Override
    public void findHelp(Boolean findHelp) {
        if (!findHelp)
            _presenter.showHelp(_playerIndex,true, _presenter.getMarblesMap(), _way, _rounds, null);
        else {
            if ((_way != null) && (_way.size() != 0))
                _presenter.showHelp(_playerIndex, true, _presenter.getMarblesMap(), _way, _rounds, _humanMoveManager.findPossiblesForSelected());
            else
                _presenter.showHelp(_playerIndex, true, _presenter.getMarblesMap(), _way, _rounds, _humanMoveManager.findPossibles());
        }
    }

    @Override
    public void removeSelected() {
        _humanMoveManager.initSelected();
        _way.clear();
    }
}
