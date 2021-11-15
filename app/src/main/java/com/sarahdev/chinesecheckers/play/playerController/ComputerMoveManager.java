package com.sarahdev.chinesecheckers.play.playerController;

import com.sarahdev.chinesecheckers.model.GameBoard;
import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.play.playerController.utils.Distance;
import com.sarahdev.chinesecheckers.play.playerController.utils.Position;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComputerMoveManager extends MoveManager {
    private final Map<MyPoint, MyPoint> _map;
    private final MoveGraph _graph;
    private MyPoint _start;
    private MyPoint _end;

    public ComputerMoveManager(Player player, List<Player> players) {
        super(player, players);
        _map = new HashMap<>();
        _graph = new MoveGraph(_player, _players);
    }

    public List<MyPoint> plotWay() {
        addAndChooseStarts();
        chooseEndPoint();
        if (GameBoard.isFirstRound(_player) && Distance.between(GameBoard.getTop(_color), _end) < 6)
            randomChoose();
        return _graph.getLinkedNodesBetween(_start, _end);
    }

    private void randomChoose() {
        List<Integer> firstsStarts = Arrays.asList(0, 1, 2, 3, 4, 6, 7);
        int randomIndex = firstsStarts.get((int) (Math.random() * firstsStarts.size()));
        _start = _marbles.get(randomIndex);
        Set<MyPoint> visited = collectEnds(_start);
        randomIndex = (int) (Math.random() * visited.size());
        MyPoint[] visitedArray = visited.toArray(new MyPoint[0]);
        _end = visitedArray[randomIndex];
    }

    private void addAndChooseStarts() {
        for (MyPoint start : _marbles) {
            for (MyPoint end : collectEnds(start))
                 if ( !_map.containsKey(end) || checkForChange(start, end, _map.get(end), end) )
                    _map.put(end, start);
        }
    }

    private Set<MyPoint> collectEnds(MyPoint start) {
        Set<MyPoint> visited = _graph.depthFirstTraversal(start, GameBoard.getAllMarbles(_players));
        visited.remove(start);
        visited.addAll(getAdjacentsList(start));
        Set<MyPoint> visitedCopy = new HashSet<>(visited);
        for (MyPoint end : visitedCopy) {
            if (!GameBoard.isParkPossible(end, _color))
                visited.remove(end);
            else if (Position.isBlockingEnd(_player, _players, start, end))
                visited.remove(end);
        }
        return visited;
    }

    private void chooseEndPoint() {
        _end = null;
        for (MyPoint end : _map.keySet()) {
            MyPoint start = _map.get(end);
            if ((_end == null) || checkForChange(start, end, _start, _end) ) {
                _start = start;
                _end = end;
            }
        }
    }

    private boolean checkForChange(MyPoint start, MyPoint end, MyPoint oldStart, MyPoint oldEnd) {
        if (getBenefit(start, end) > 0) {
            if (getBenefit(start, end) > getBenefit(oldStart, oldEnd))
                return true;
            if (getBenefit(start, end) == getBenefit(oldStart, oldEnd)) {
                if (isStartPossibleEndForOther(start, end, _graph) && !isStartPossibleEndForOther(oldStart, oldEnd, _graph))
                    return true;
                if (isPivotMovePossibleAfter(start, end) && !isPivotMovePossibleAfter(oldStart, oldEnd))
                    return true;
                if (isEndAlignedWithOther(start, end) && !isEndAlignedWithOther(oldStart, oldEnd))
                    return true;
                if (GameBoard.isInStartArea(start, _color) && !GameBoard.isInStartArea(oldStart, _color))
                    return true;
                if (GameBoard.isInStartArea(start, _color) && GameBoard.isInStartArea(oldStart, _color) && (distanceToFreeOppositeNode(start) > distanceToFreeOppositeNode(oldStart)))
                    return true;
                if (!GameBoard.isArrived(start, _color) && GameBoard.isArrived(oldStart, _color))
                    return true;
                if (distanceToFreeOppositeNode(start) > distanceToFreeOppositeNode(oldStart))
                    return true;
                if (distanceToFreeOppositeNode(end) < distanceToFreeOppositeNode(oldEnd))
                    return true;
            }
        } else if ((getBenefit(start, end) == 0) && (getBenefit(oldStart, oldEnd) <= 0)) {
            if (getBenefit(start, end) > getBenefit(oldStart, oldEnd))
                return true;
            if (isStartPossibleEndForOther(start, end, _graph) && !isStartPossibleEndForOther(oldStart, oldEnd, _graph))
                return true;
            if (isPivotMovePossibleAfter(start, end) && !isPivotMovePossibleAfter(oldStart, oldEnd))
                return true;
            if (isEndAlignedWithOther(start, end) && !isEndAlignedWithOther(oldStart, oldEnd))
                return true;
            if (GameBoard.isInTipArea(end, _color) && !GameBoard.isInTipArea(oldEnd, _color))
                return true;
        }
         return false;
    }
}
