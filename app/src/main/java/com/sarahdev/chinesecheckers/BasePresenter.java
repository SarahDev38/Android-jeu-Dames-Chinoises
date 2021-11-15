package com.sarahdev.chinesecheckers;

import android.content.Context;

import com.sarahdev.chinesecheckers.model.DataModel;
import com.sarahdev.chinesecheckers.model.MyPoint;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.model.Type;
import com.sarahdev.chinesecheckers.persistance.Files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BasePresenter {
    private DataModel _data;
    protected int _playersCount = 2;

    public abstract Context getContext();

// ----------------------- about data ---------------------------------

    public DataModel getData() {return this._data;}
    public void readData() { this._data = Files.readFile(getContext()); }
    public void saveData() { Files.writeFile(getContext(), _data); }

    public void initGameParams() {
        _data.setRounds(1);
        _data.setCurrentIndex(0);
        _data.setInProgressGame(false);
        _data.setOpenGame(false);
    }

    public void initGame() {
        initGameParams();
        _data.initPlayers();
    }

    public void setGameNames() {
        for (Player player : _data.getPlayers()) {
            if (player.getType() == Type.Computer)
                player.setName("android");
            else if (player.getType() == Type.Human && player.getName().equals(""))
                player.setName("joueur inconnu");
            else if (player.getType() == Type.NoOne)
                player.setName("");
        }
    }

    public void reverseColor(int playerIndex1, int playerIndex2) {
        int color1 = _data.getColor(playerIndex1);
        int color2 = _data.getColor(playerIndex2);
        _data.getPlayer(playerIndex1).setColor(color2);
        _data.getPlayer(playerIndex2).setColor(color1);
    }

    public int findFreeIndex() {
        int lastPlayerIndex = 5;
        while(_data.getType(lastPlayerIndex).equals(Type.NoOne) && (lastPlayerIndex > 0)) {
            lastPlayerIndex--;
        }
        if (lastPlayerIndex == 5)
            for (int i = 0 ; i < 6 ; i++)
                if (_data.getType(i).equals(Type.NoOne))
                    return i;
        return (lastPlayerIndex + 1);
    }

    public Map<Integer, List<MyPoint>> getMarblesMap() {
        Map<Integer, List<MyPoint>> marbles = new HashMap<>();
        for (Player player : _data.getPlayers())
            marbles.put(player.getColor(), player.getMarbles());
        return marbles;
    }

    public List<String> getProfilesNames() {
        List<String> names = new ArrayList<>();
        if (_data.getProfiles() != null)
            names.addAll(_data.getProfiles().keySet());
        return names;
    }

// ------------------------- about data - scores

    public void setGameScore(int winnerIndex) {
        _data.resetGameScore();
        int winnerScore = 0;
        for (int i = 0 ; i < 6 ; i++) {
            if ((i != winnerIndex) && !_data.getType(i).equals(Type.NoOne)) {
                winnerScore += _data.getPlayer(i).gameScore();
                _data.getGameScore().put(_data.getColor(i), -_data.getPlayer(i).gameScore());
            }
        }
        _data.getGameScore().put(_data.getColor(winnerIndex), winnerScore);
    }

    public void addScores() {
        if (_data.getGameScore() != null) {
            for (int colorIndex : _data.getGameScore().keySet()) {
                String name = _data.getPlayerByColor(colorIndex).getName();
                Integer tempScore = _data.getScores().get(name);
                if (tempScore != null)
                    tempScore += _data.getGameScore().get(colorIndex);
                else
                    tempScore = _data.getGameScore().get(colorIndex);
                _data.getScores().put(name, tempScore);
            }
        }
    }

    public void deleteScore() {
        _data.resetScores();}

// ------------------------- getters et setters

    public int getPlayersCount() {return _playersCount;}
    public void setPlayersCount(int playersCount) {this._playersCount = playersCount;}
}