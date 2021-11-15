package com.sarahdev.chinesecheckers.play.playerController.utils;

import com.sarahdev.chinesecheckers.model.GameBoard;
import com.sarahdev.chinesecheckers.model.MyPoint;

import java.util.List;

public class PivotMove {

    public static MyPoint getEnd(MyPoint start, MyPoint pivot) {
        return new MyPoint(2 * pivot.x - start.x, 2 * pivot.y - start.y);
    }

    public static boolean isPossibleAround(MyPoint pivot, MyPoint root, MyPoint start, List<MyPoint> allMarbles) {
        if (pivot.equals(start) || pivot.equals(root) || !Position.areAligned(start.x, start.y, pivot.x, pivot.y))
            return false;
        if (!GameBoard._nodes.contains(getEnd(start, pivot)) || !allMarbles.contains(pivot))
            return false;
        int distance = Math.max(Math.abs(start.x - pivot.x), Math.abs(start.y - pivot.y));
        int dX = Integer.compare(pivot.x, start.x);
        int dY = Integer.compare(pivot.y, start.y);
        for (int i = 1; i < 2 * distance + 1 ; i++)
            if ((i != distance) &&!Position.placeIsFree(start.x + dX * i, start.y + dY * i, allMarbles))
                return false;
        return true;
    }

    public static boolean isPossible(MyPoint root, MyPoint start, MyPoint end, List<MyPoint> allMarbles) {
        int distance = Distance.between(start, end);
        if (distance == 0 || (distance % 2 != 0) || !Position.areAligned(start, end) )
            return false;
        if (isPivotOf(root, start, end))
            return false;
        if (Position.placeIsFree((start.x + end.x) / 2, (start.y + end.y) / 2, allMarbles))
            return false;
        for (int i = 1; (i < distance + 1) && (2 * i != distance); i++)
            if (!Position.placeIsFree(start.x + (end.x - start.x) * i / distance, start.y + (end.y - start.y) * i / distance,
                    allMarbles))
                return false;
        return true;
    }

    private static boolean isPivotOf(MyPoint pivot, MyPoint start, MyPoint end) {
        if ((end.x - start.x) != 2 * (pivot.x - start.x))
            return false;
        return (end.y - start.y) == 2 * (pivot.y - start.y);
    }
}
