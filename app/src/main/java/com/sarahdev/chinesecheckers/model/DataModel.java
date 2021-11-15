package com.sarahdev.chinesecheckers.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModel implements Serializable {
    private List<Player> _players = new ArrayList<>();
    private String _profileName = "";
    private final Map<String, Integer> _profiles = new HashMap<>();
    private int _rounds = 1;
    private int _currentIndex = 0;
    private boolean _inProgressGame = false;
    private boolean _openGame = false;
    private boolean _helpAllowed = true;
    private final Map<String, Integer> _scores = new HashMap<>();
    private final Map<Integer, Integer> _gameScore = new HashMap<>();
    private int _delay = 40;
    public static final int MAX_DELAY = 80;

    public DataModel() {
        initPlayers();
    }

    public void initPlayers() {
        _players.clear();
        int icon = (_profiles.get(_profileName) == null) ? 0 : _profiles.get(_profileName);
        _players.add(new Player(_profileName, 0, Type.Human, icon));
        _players.add(new Player("", 1, Type.Computer, 0));
        _players.add(new Player("", 2, Type.NoOne, 0));
        _players.add(new Player("", 3, Type.NoOne, 0));
        _players.add(new Player("", 4, Type.NoOne, 0));
        _players.add(new Player("", 5, Type.NoOne, 0));
    }

    public void setName(int index, String name) {
        _players.get(index).setName(name);}
    public String getName(int index) {return _players.get(index).getName();}

    public void setType(int index, Type type) {
        _players.get(index).setType(type);
        if(type.equals(Type.Human) && getName(index).equals("android"))
            setName(index, "");
    }
    public Type getType(int index) {return _players.get(index).getType();}

    public Integer getIndexByColor(int color){ return _players.indexOf(getPlayerByColor(color)); }
    public int getColor(int index) {return _players.get(index).getColor();}
    public Player getPlayer(int index) {return _players.get(index);}
    public Player getPlayerByColor(int color) {
        for (Player player : _players)
            if (player.getColor() == color)
                return player;
        return null;
    }
    public int getIcon(int index) {return _players.get(index).getIcon();}
    public void setIcon(int index, int icon) {
        _players.get(index).setIcon(icon);}

    public boolean isCurrentHuman() {return getCurrent().getType().equals(Type.Human);}
    public Player getCurrent() {return _players.get(_currentIndex);}
    public int getResult(int index) {return _players.get(index).tempResult();}

// -------------------- GETTERS ET SETTERS ---------------------------

    public List<Player> getPlayers() {return _players;}
    public void setPlayers(List<Player> players) {this._players = players;}

    public int getRounds() {return _rounds;}
    public void setRounds(int rounds) {this._rounds = rounds;}

    public String getProfileName() {return _profileName;}
    public void setProfileName(String name) {this._profileName = name.trim();}

    public void setInProgressGame(boolean inProgressGame) {this._inProgressGame = inProgressGame;}
    public boolean isInProgressGame() {return _inProgressGame;}

    public boolean isOpenGame() {return _openGame;}
    public void setOpenGame(boolean openGame) {this._openGame = openGame;}

    public int getCurrentIndex() {return _currentIndex;}
    public void setCurrentIndex(int currentIndex) {this._currentIndex = currentIndex;}

    public void resetGameScore() {this._gameScore.clear();}
    public Map<Integer, Integer> getGameScore() {return _gameScore;}

    public Map<String, Integer> getScores() {return _scores;}
    public void resetScores() {this._scores.clear();}

    public int getDelay() {return _delay;}
    public void setDelay(int delay) {this._delay = delay;}

    public boolean isHelpAllowed() {return _helpAllowed;}
    public void setHelpAllowed(boolean helpAllowed) {this._helpAllowed = helpAllowed;}

    public Map<String, Integer> getProfiles() {return this._profiles;}
    public void addProfiles(String name, int icon) {this._profiles.put(name, icon);}
}