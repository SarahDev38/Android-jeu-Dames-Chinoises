package com.sarahdev.chinesecheckers.model;

import com.sarahdev.chinesecheckers.play.playerController.utils.Distance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameBoard {
    public static final List<MyPoint> _nodes = new ArrayList<>();
    public static final Map<Integer, List<MyPoint>> _nodesMap = new HashMap<>();
    public static final List<Integer> _colors = new ArrayList<>();

    public GameBoard (List<Player> players) {
        setNodes();
        setColors(players);
        setNodesMap();
    }

    //////// -------  INIT GAMEBOARD -----------

    private static void setNodes () {
        _nodes.clear();
        _nodes.add(new MyPoint(0, 0));
        for (int x = 1; x < 5; x++)
            for (int y = 0; y < 5; y++)
                rotateAndAdd(_nodes, x, y, 0);
    }

    private static void rotateAndAdd (List<MyPoint> list, int x, int y, int increment) {
        if (increment < 6) {
            list.add(new MyPoint(x, y));
            rotateAndAdd(list, x + y, -x, ++increment);
        }
    }

    public static void setColors (List<Player> players) {
        _colors.clear();
        for (Player player : players) {
            _colors.add(player.getColor());
        }
    }

    public static void setNodesMap () {
        _nodesMap.clear();
        for (Integer color : _colors) {
            _nodesMap.put(color, getNodesOfColor(color));
        }
        _nodesMap.put(6, getNodesOfColor(6));
    }

    //////// ------------------ UTILS

    public static int getColor(MyPoint node) {
        int x = node.x;
        int y = node.y;
        if (Math.abs(x + y) > 4 || Math.abs(x) > 4 || Math.abs(y) > 4) {
            if (x > 4)
                return _colors.get(2);
            if (x < -4)
                return _colors.get(5);
            if (y > 4)
                return _colors.get(0);
            if (y < -4)
                return _colors.get(3);
            if (x > 0 && y > 0)
                return _colors.get(1);
            if (x < 0 && y < 0)
                return _colors.get(4);
        }
        return 6;
    }

    public static List<MyPoint> getNodesOfColor(int color) {
        List<MyPoint> nodes = new ArrayList<>();
        for (MyPoint node : _nodes) {
            if (getColor(node) == color)
                nodes.add(node);
        }
        return nodes;
    }

    public static Player getOppositePlayer(Player player, List<Player> players) {
        return players.get((players.indexOf(player) + 3) % 6);
    }

    public static int getOppositeColor(int color) {
        return _colors.get((_colors.indexOf(color) + 3) % 6);
    }

    public static List<MyPoint> getAllMarbles(List<Player> players) {
        List<MyPoint> marbles = new ArrayList<>();
        for (Player player : players)
            marbles.addAll(player.getMarbles());
        return marbles;
    }
    public static MyPoint getTop(int color) {
        for (MyPoint node : Objects.requireNonNull(_nodesMap.get(color))) {
            if ((Math.abs(node.x) == 8 || Math.abs(node.y) == 8) || (Math.abs(node.x) == 4 && Math.abs(node.y) == 4))
                return node;
        }
        return new MyPoint();
    }

    //////// ------------------ TESTS

    /**
     * tests whether marble is arrived in the opposite area
     * @param marble - The MyPoint to test
     * @param color - The color of the player who owns MyPoint marble
     * @return true if the distance between top of opposite area and marble is less than 4, false otherwise
     */
    public static boolean isArrived(MyPoint marble, int color) {
        return (Distance.between(marble, getTop(getOppositeColor(color))) < 4);
    }

    /**
     * tests whether marble is in the start area
     * @param marble - The MyPoint to test
     * @param color - The color of the player who owns MyPoint marble
     * @return true if the distance between top of start area and marble is less than 4, false otherwise
     */
    public static boolean isInStartArea(MyPoint marble, int color) {
        return (Distance.between(marble, getTop(color)) < 4);
    }

    /**
     * tests whether a player has already moved his marbles
     * @param player - The player engaged
     * @return true all of the player's marbles are in the start area, false otherwise
     */
    public static boolean isFirstRound(Player player) {
        for (MyPoint marble : player.getMarbles())
            if (!isInStartArea(marble, player.getColor()))
                return false;
        return true;
    }

    /**
     * Tests whether end is on a suitable node to stay and possibly end the turn of game
     * @param end - The MyPoint to test
     * @param color - The color of the player who owns MyPoint end
     * @return true if end is on a node which color is color, is the opposite of color or is non-colored node; false otherwise
     */
    public static boolean isParkPossible(MyPoint end, int color) {
        int nodeColor = getColor(end);
        return (nodeColor == color
                || nodeColor == getOppositeColor(color)
                || nodeColor == 6);
    }

    /**
     * Tests whether marble is on the top 3 nodes of the opposite area
     * @param marble - The MyPoint to test
     * @param color - The color of the player who owns MyPoint end
     * @return true if the distance between the top of opposite team and marble is less than 2 (0 or 1)
     */
    public static boolean isInTipArea(MyPoint marble, int color) {
        return (Distance.between(marble, getTop(getOppositeColor(color))) < 2);
    }

    public static boolean isInColoredArea (MyPoint marble) {
        return (getColor(marble) != 6);
    }
}
