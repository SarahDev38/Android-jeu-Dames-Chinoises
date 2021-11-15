package com.sarahdev.chinesecheckers.play.playerController;

import com.sarahdev.chinesecheckers.model.GameBoard;
import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.play.playerController.utils.Distance;
import com.sarahdev.chinesecheckers.play.playerController.utils.ListUtils;
import com.sarahdev.chinesecheckers.play.playerController.utils.PivotMove;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HumanMoveManager extends MoveManager {
    private enum State {
        NewTurn, SelectMarble, PivotMove, NodeMove
    }
    private State _state;
    private List<MyPoint> _way;

    public HumanMoveManager(Player player, List<Player> players) {
        super(player, players);
        _state = State.NewTurn;
        _way = new ArrayList<>();
    }

    protected List<MyPoint> changeSelected(MyPoint clicked) {
        MyPoint last = null;
        if (_way.size() > 0) {
            last = _way.get(_way.size() - 1);
            if (!clicked.equals(last))
                _player.move(last, _way.get(0));
        }
        if (!clicked.equals(last)) {
            _way.clear();
            _way.add(clicked);
            _state = State.SelectMarble;
        }
        return _way;
    }

    protected List<MyPoint> moveSelected(MyPoint clicked) {
        if (_state == State.SelectMarble || _state == State.PivotMove) {
            if (PivotMove.isPossible(_way.get(0), _way.get(_way.size() - 1), clicked, GameBoard.getAllMarbles(_players))) {
                _player.move(_way.get(_way.size() - 1), clicked);
                _way.add(clicked);
                _state = State.PivotMove;
            } else if (_state == State.SelectMarble
                    && (Distance.between(_way.get(_way.size() - 1), clicked) == 1 && !GameBoard.getAllMarbles(_players).contains(clicked)) ) {
                _player.move(_way.get(_way.size() - 1), clicked);
                _way.add(clicked);
                _state = State.NodeMove;
            }
        }
        if (_state == State.NodeMove || _state == State.PivotMove) {
            if (_way.size() > 0 && _way.get(0).equals(clicked)) {
                _player.move(_way.get(_way.size() - 1), clicked);
                _way.clear();
                _way.add(clicked);
                _state = State.SelectMarble;
            }
        }
        _way = ListUtils.tidy(_way);
        return _way;
    }

    protected boolean isPossibleEndOfTurn() {
        if (_way != null && _way.size() > 0)
            return ( !(_way.get(_way.size() - 1).equals(_way.get(0)))
                    && GameBoard.isParkPossible(_way.get(_way.size() - 1), _color));
        return false;
    }

    protected List<MyPoint> findPossiblesForSelected() {
        if ((_way != null) && (_way.size() != 0)) {
            MyPoint selected = _way.get(0);
            List<MyPoint> formerWay = new ArrayList<>(_way);
            State formerState = _state;
            initSelected();

            MoveGraph graph = new MoveGraph(_player, _players);
            Set<MyPoint> possibles = graph.depthFirstTraversal(selected, GameBoard.getAllMarbles(_players));
            possibles.remove(selected);
            List<MyPoint> possiblesCopy = new ArrayList<>(possibles);
            for (MyPoint point : possiblesCopy) {
                if (!GameBoard.isParkPossible(point, _color))
                    possibles.remove(point);
            }
            _way = formerWay;
            _state = formerState;
            if (_way.size() > 0)
                _player.move(_way.get(0), _way.get(_way.size() - 1));
            return new ArrayList<>(possibles);
        }
        return null;
    }

    protected List<MyPoint> findPossibles() {
        initSelected();
        MoveGraph graph = new MoveGraph(_player, _players);
        Set<MyPoint> possibles = new HashSet<>();
        Set<MyPoint> tempPossibles;
        for (MyPoint marble : _marbles) {
            if (!GameBoard.isArrived(marble, _color)) {
                tempPossibles = graph.depthFirstTraversal(marble, GameBoard.getAllMarbles(_players));
                tempPossibles.remove(marble);
                possibles.addAll(tempPossibles);
             }
        }
        tempPossibles = new HashSet<>(possibles);
        for (MyPoint point : tempPossibles) {
            if (!GameBoard.isParkPossible(point, _color))
                possibles.remove(point);
        }
        return new ArrayList<>(possibles);
    }

    protected void initSelected() {
        if (_way.size() > 0)
            _player.move(_way.get(_way.size() - 1), _way.get(0));
        _way.clear();
        _state = State.NewTurn;
    }
}