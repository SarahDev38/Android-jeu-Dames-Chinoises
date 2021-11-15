package com.sarahdev.chinesecheckers.play.playerController.utils;

import com.sarahdev.chinesecheckers.model.MyPoint;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    public static List<MyPoint> tidy(List<MyPoint> way) {
        List<MyPoint> tidy = new ArrayList<>();
        int regress = way.size();
        if (way.size() > 2) {
            for (int i = 0; i < way.size() - 2; i++)
                if (way.get(i).equals(way.get(way.size() - 1))) {
                    regress = i + 1;
                    break;
                }
        }
        for (int j = 0; j < regress; j++)
            tidy.add(way.get(j));
        return tidy;
    }

}
