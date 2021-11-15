package com.sarahdev.chinesecheckers.model;

import com.sarahdev.chinesecheckers.play.playerController.utils.Distance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player implements Serializable {
    private String _name;
    private int _color;
    private Type _type;
    private int _icon;
    private List<MyPoint> _marbles;

    public Player(String name, Integer color, Type type, int icon) {
        this._name = name.trim();
        this._color = color;
        this._type = type;
        this._marbles = new ArrayList<> ();
        this._icon = icon;
    }

    public void initMarblesPlace() {
        if (!_type.equals(Type.NoOne) )
            this._marbles = new ArrayList<> (Objects.requireNonNull(GameBoard._nodesMap.get(_color)));
        else
            this._marbles = new ArrayList<> ();
    }

    public void move(MyPoint previous, MyPoint next) {
        if (_marbles.contains(previous))
            _marbles.set(_marbles.indexOf(previous), next);
    }

    public int tempResult() {
        int result = 0;
        for (MyPoint marble : _marbles)
            if (GameBoard.getColor(marble) == GameBoard.getOppositeColor(_color))
                result++;
        return result;
    }

    public int gameScore() {
        int score = 0;
        int oppositeColor = GameBoard.getOppositeColor(_color);
        for (MyPoint marble : _marbles) {
            if (GameBoard.getColor(marble) != oppositeColor)
                score += Distance.between(GameBoard.getTop(oppositeColor), marble) - 3;
        }
        return score;
    }

    // -------------------- GETTERS ET SETTERS ---------------------------

    public void setName(String name) {this._name = name.trim();}
    public String getName() {return _name;}
    public List<MyPoint> getMarbles() {return _marbles;}
    public void setType(Type type) {this._type = type;}
    public Type getType() {return _type;}
    public int getColor() {return this._color;}
    public void setColor(int colorIndex) {this._color = colorIndex;}
    public int getIcon() {return this._icon;}
    public void setIcon(int icon) {this._icon = icon;}
}