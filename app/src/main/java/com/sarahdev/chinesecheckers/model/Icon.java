package com.sarahdev.chinesecheckers.model;

import androidx.annotation.DrawableRes;

import com.sarahdev.chinesecheckers.R;

public enum Icon {
    A(R.drawable.persofille50, R.drawable.persofille36), B(R.drawable.medal2, R.drawable.medal2),
    C(R.drawable.gagnant50, R.drawable.gagnant50), D(R.drawable.podium50, R.drawable.podium50),
    E( R.drawable.winner50, R.drawable.winner50), F(R.drawable.medal1, R.drawable.medal1),
    COMPUTER(R.drawable.persorobot1, R.drawable.persorobot36);

    private final int id;
    private final int miniId;

    Icon(@DrawableRes final int id, @DrawableRes final int miniId) {
        this.id = id;
        this.miniId = miniId;
    }

    @DrawableRes
    public int id() {
        return id;
    }

    @DrawableRes
    public int miniId() {
        return miniId;
    }

    @DrawableRes
    public static int miniId(int index) {return icon(index).miniId();}

    @DrawableRes
    public static int id(int index) {return icon(index).id();}

    public static Icon icon(int index) {
        switch (index) {
            case 0:
                return A;
            case 1:
                return B;
            case 2:
                return C;
            case 3:
                return D;
            case 4:
                return E;
            case 5:
                return F;
            default:
                return COMPUTER;
        }
    }
}
