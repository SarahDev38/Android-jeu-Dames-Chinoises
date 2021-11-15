package com.sarahdev.chinesecheckers.play.playerController;

import com.sarahdev.chinesecheckers.model.GameBoard;
import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.play.playerController.utils.Distance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class MoveGraph extends MoveManager {
    private Map<MyPoint, List<MyPoint>> _map;

    public MoveGraph(Player player, List<Player> players) {
        super(player, players);
    }

    /*
    récupère tous les points d'arrivée possibles après des déplacements pivot
    pour un point de départ donné (start)
    start est inclus dans visited
     */
    protected Set<MyPoint> depthFirstTraversal(MyPoint start, List<MyPoint> allMarbles) {
        _map = new HashMap<>();
        _map.put(start, new ArrayList<>());
        Set<MyPoint> visited = new HashSet<>();
        Stack<MyPoint> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            MyPoint node = stack.pop();
            addLandingNodesToMap(node, start, allMarbles);
            for (MyPoint adjNode : _map.get(node)) {
                if (!visited.contains(adjNode) && !stack.contains(adjNode))
                    stack.push(adjNode);
            }
            visited.add(node);
        }
        return visited;
    }

    protected List<MyPoint> getLinkedNodesBetween(MyPoint start, MyPoint end) {
        _map = new HashMap<>();
        _map.put(start, new ArrayList<>());
        List<MyPoint> way = new ArrayList<>();
        if (Distance.between(start, end) != 1) {
            Set<MyPoint> visited = new HashSet<>();
            Stack<MyPoint> stack = new Stack<>();
            stack.push(start);
            way.add(start);
            while (!stack.isEmpty()) {
                MyPoint node = stack.pop();
                addLandingNodesToMap(node, start, GameBoard.getAllMarbles(_players));
                for (MyPoint next : _map.get(node)) {
                    if (!visited.contains(next) && !stack.contains(next))
                        stack.push(next);
                }
                if (!visited.contains(node)) {
                    visited.add(node);
                    if (!way.contains(node))
                        way.add(node);
                    if (node.equals(end)) {
                        sortOutWay(way);
                        return way;
                    }
                }
            }
        } else {
            way.add(start);
            way.add(end);
        }
        return way;
    }

    private void sortOutWay(List<MyPoint> way) {
        MyPoint cursor = way.get(way.size() - 1);
        while (!cursor.equals(way.get(0))) {
            MyPoint beforeCursor = way.get(way.indexOf(cursor) - 1);
            if (!_map.get(cursor).contains(beforeCursor))
                way.remove(beforeCursor);
            else
                cursor = beforeCursor;
        }
        if (way.size() > 2)
            tidyUpWay(way, 0);
    }

    private List<MyPoint> tidyUpWay(List<MyPoint> way, int index) {
        if (index < way.size() - 2) {
            for (int i = way.size() - 1; i > index + 1; i--) {
                if (_map.get(way.get(i)).contains(way.get(index))) {
                    for (int j = i - 1; j > index; j--)
                        way.remove(way.get(j));
                    return tidyUpWay(way, index);
                }
            }
            return tidyUpWay(way, ++index);
        }
        return way;
    }

    private void addLandingNodesToMap(MyPoint start, MyPoint root, List<MyPoint> allMarbles) {
        for (MyPoint end : getEndsListAfterOnePivotMove(start, root, allMarbles)) {
            List<MyPoint> list = _map.get(end);
            if (list == null)
                _map.put(end, new ArrayList<>());
            addValueToKey(start, end);
            addValueToKey(end, start);
        }
    }

    private void addValueToKey(MyPoint key, MyPoint value) {
        List<MyPoint> tempList = _map.get(key);
        if (tempList != null)
            tempList.remove(value);
        else
            tempList = new ArrayList<>();
        tempList.add(value);
        _map.put(key, tempList);
    }
}