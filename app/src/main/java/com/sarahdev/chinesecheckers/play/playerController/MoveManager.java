package com.sarahdev.chinesecheckers.play.playerController;

import com.sarahdev.chinesecheckers.model.GameBoard;
import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.play.playerController.utils.Distance;
import com.sarahdev.chinesecheckers.play.playerController.utils.PivotMove;
import com.sarahdev.chinesecheckers.play.playerController.utils.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class MoveManager {
    protected List<Player> _players;
    protected Player _player;
    protected List<MyPoint> _marbles;
    protected final int _color;
    protected final int _oppositeColor;
    protected final List<MyPoint> _oppositeNodes;

    public MoveManager(Player player, List<Player> players) {
        this._players = players;
        this._player = player;
        this._marbles = player.getMarbles();
        this._color = _player.getColor();
        this._oppositeColor = GameBoard.getOppositeColor(_color);
        this._oppositeNodes = GameBoard.getNodesOfColor(_oppositeColor);
    }

    /*
    retourne la liste de tous les trous existants et disponibles
        où on peut se déplacer par saut d'une case
        en partant de start
     */
    protected List<MyPoint> getAdjacentsList(MyPoint start) {
        List<MyPoint> nodes = new ArrayList<>();
        for (int i = -1; i < 2; i++)
            for (int j = -1; j < 2; j++) {
                MyPoint adjacent = new MyPoint(start.x + i, start.y + j);
                if ((j != i) && !GameBoard.getAllMarbles(_players).contains(adjacent) && GameBoard._nodes.contains(adjacent))
                    nodes.add(adjacent);
            }
        return nodes;
    }

    /*
    retourne la liste de tous les trous existants et disponibles
        où on peut se déplacer par saut pivot
        en partant de start, sachant que le tout premier point de départ est root
        (différent de start si plusieurs sauts consécutifs)
     */
    protected List<MyPoint> getEndsListAfterOnePivotMove(MyPoint start, MyPoint root, List<MyPoint> allMarbles) {
        List<MyPoint> endsList = new ArrayList<>();
        for (MyPoint pivot : allMarbles)
            if (PivotMove.isPossibleAround(pivot, root, start, allMarbles))
                endsList.add(PivotMove.getEnd(start, pivot));
        return endsList;
    }

    protected boolean isStartPossibleEndForOther(MyPoint start, MyPoint end, MoveGraph graph) {
        List<MyPoint> newAllMarblesList = new ArrayList<>(GameBoard.getAllMarbles(_players));
        newAllMarblesList.set(newAllMarblesList.indexOf(start), end);
        for (MyPoint marble : _marbles) {
            if (!marble.equals(start) && !GameBoard.isArrived(marble, _color)
                    && graph.depthFirstTraversal(marble, newAllMarblesList).contains(start)
                    && (getBenefit(marble, start) > 0))
                return true;
        }
        return false;
    }

    protected boolean isEndAlignedWithOther (MyPoint start, MyPoint end) {
        for (MyPoint marble : _marbles) {
            if (!marble.equals(start) && !GameBoard.isArrived(marble, _color)
                    && Position.areAligned(marble, end)
                    && (getBenefit(marble, end) > 0))
                return true;
        }
        return false;
    }

    protected boolean isPivotMovePossibleAfter(MyPoint start, MyPoint end) {
        List<MyPoint> newAllMarblesList = new ArrayList<>(GameBoard.getAllMarbles(_players));
        newAllMarblesList.set(newAllMarblesList.indexOf(start), end);
        for (MyPoint pivot : newAllMarblesList) {
            if (PivotMove.isPossibleAround(pivot, end, end, newAllMarblesList)) {
                MyPoint pivotEnd = PivotMove.getEnd(end, pivot);
                if (!GameBoard.isInColoredArea(pivotEnd) || GameBoard.isArrived(pivotEnd, _color))
                    return true;
            }
        }
        return false;
    }

    protected int getBenefit(MyPoint start, MyPoint end) {
        return (distanceToFreeOppositeNode(start) - distanceToFreeOppositeNode(end));
    }

    protected int distanceToFreeOppositeNode(MyPoint node) {
        if (GameBoard.isArrived(node, _color))
            return 0;
        int d = 16;
        for (MyPoint opposite : _oppositeNodes) {
            if (!_marbles.contains(opposite) && !_players.get((_players.indexOf(_player)+3) %6).getMarbles().contains(opposite)) {
                d = Math.min(d, Distance.between(opposite, node));
            }
        }
        return d;
    }
}
