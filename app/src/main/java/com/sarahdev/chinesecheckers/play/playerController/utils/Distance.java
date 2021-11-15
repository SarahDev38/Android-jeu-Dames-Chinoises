package com.sarahdev.chinesecheckers.play.playerController.utils;

import com.sarahdev.chinesecheckers.model.MyPoint;

public class Distance {

    public static Integer between(MyPoint node1, MyPoint node2) {
        int a = node1.x - node2.x;
        int b = node1.y - node2.y;
        if (Math.signum(a) == Math.signum(b))
            return (Math.abs(a) + Math.abs(b));
        else if (Math.abs(a) >= Math.abs(b))
            return (Math.abs(a + b) + Math.abs(b));
        else
            return (Math.abs(a + b) + Math.abs(a));
    }

}
