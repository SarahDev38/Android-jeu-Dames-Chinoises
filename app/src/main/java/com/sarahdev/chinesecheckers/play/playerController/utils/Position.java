package com.sarahdev.chinesecheckers.play.playerController.utils;

import com.sarahdev.chinesecheckers.model.GameBoard;
import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;

import java.util.ArrayList;
import java.util.List;

public class Position {

    public static boolean placeIsFree(int x, int y, List<MyPoint> allMarbles) {
        return (!allMarbles.contains(new MyPoint(x, y)));
    }

    public static boolean areAligned(MyPoint node1, MyPoint node2) {
        return areAligned(node1.x, node1.y, node2.x, node2.y);
    }

    public static boolean areAligned(int x1, int y1, int x2, int y2) {
        return ((x1 == x2) || (y1 == y2) || ((x1 - x2) == (y2 - y1)));
    }

    /*
    utilisé pour les gros doigts : si on ne vise pas bien un node en bordure du jeu
    évite de recommencer le trajet
     */
    public static boolean isOnBorder(MyPoint touchpoint) {
        if (Math.abs(touchpoint.x) == 5)
            return true;
        if (Math.abs(touchpoint.y) == 5)
            return true;
        if (Math.abs(touchpoint.x + touchpoint.y) == 5)
            return true;
        return false;
    }


    public static boolean isBlockingEnd(Player player, List<Player> players, MyPoint start, MyPoint end) {
        if (GameBoard.getColor(end) != GameBoard.getOppositeColor(player.getColor()))
            return false;

        List<MyPoint> oppositeNodes = GameBoard.getNodesOfColor(GameBoard.getOppositeColor(player.getColor()));
        List<MyPoint> newMarbles = new ArrayList<>(player.getMarbles());
        newMarbles.set( newMarbles.indexOf(start) , end);
        if (countRemainingInTop3(GameBoard.getOppositePlayer(player, players), oppositeNodes) > 0) {
            switch (countRemainingInTop2(GameBoard.getOppositePlayer(player, players), oppositeNodes)) {
                case 0:
                    if (!newMarbles.contains(oppositeNodes.get(5)) && !newMarbles.contains(oppositeNodes.get(8)))
                        return !oneIsFree(oppositeNodes, newMarbles, 0, 1, 2, 3, 4, 6, 7);
                    if (newMarbles.contains(oppositeNodes.get(5)) && newMarbles.contains(oppositeNodes.get(8)))
                        return !oneIsFree(oppositeNodes, newMarbles, 2, 7);
                    if (newMarbles.contains(oppositeNodes.get(5)))
                        return !oneIsFree(oppositeNodes, newMarbles, 1, 4, 6, 7);
                    return !oneIsFree(oppositeNodes, newMarbles, 0, 2, 3, 4);
                case 1:
                    if (!newMarbles.contains(oppositeNodes.get(5)) && !newMarbles.contains(oppositeNodes.get(8)))
                        return !oneIsFree(oppositeNodes, newMarbles, 0, 1, 2, 3, 4, 6, 7);
                    if(newMarbles.contains(oppositeNodes.get(5)))
                        return !oneIsFree(oppositeNodes, newMarbles, 1, 4, 6, 7);
                    return (!oneIsFree(oppositeNodes, newMarbles, 0, 2, 3, 4));
                case 2:
                    return !oneIsFree(oppositeNodes, newMarbles, 0, 1, 2, 3, 4, 6, 7);
            }
        }
        return false;
    }

    private static Boolean oneIsFree(List<MyPoint> oppositeNodes, List<MyPoint> marbles, Integer... indexes) {
        for (Integer i : indexes)
            if (!marbles.contains(oppositeNodes.get(i)))
                return true;
        return false;
    }

    private static int countRemainingInTop3(Player oppositePlayer, List<MyPoint> oppositeNodes) {
        int n = 0;
        for (MyPoint marble : oppositePlayer.getMarbles())
            if (marble.equals(oppositeNodes.get(5)) || marble.equals(oppositeNodes.get(8)) || marble.equals(oppositeNodes.get(9)))
                n++;
        return n;
    }

    private static int countRemainingInTop2(Player oppositePlayer, List<MyPoint> oppositeNodes) {
        int n = 0;
        for (MyPoint marble : oppositePlayer.getMarbles())
            if (marble.equals(oppositeNodes.get(5)) || marble.equals(oppositeNodes.get(8)))
                n++;
        return n;
    }
}
