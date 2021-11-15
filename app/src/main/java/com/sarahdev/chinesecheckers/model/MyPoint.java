package com.sarahdev.chinesecheckers.model;

import java.io.Serializable;

public class MyPoint implements Serializable {
    private static final long serialVersionUID = -455530706921004893L;
    public int x;
    public int y;

    public MyPoint() {
    }

    public MyPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object)
            return true;
        if (object != null)
            if (object instanceof MyPoint) {
                MyPoint p = (MyPoint) object;
                return ( (this.x == p.x) && (this.y == p.y) );
            }
        return false;
     }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}