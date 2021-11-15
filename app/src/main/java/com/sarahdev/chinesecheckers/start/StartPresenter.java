package com.sarahdev.chinesecheckers.start;

import android.content.Context;

import com.sarahdev.chinesecheckers.BasePresenter;
import com.sarahdev.chinesecheckers.model.Player;
import com.sarahdev.chinesecheckers.model.Type;

import java.util.List;

public class StartPresenter extends BasePresenter {
    private final ViewActivity _viewActivity;

    public StartPresenter(ViewActivity viewActivity) {
        this._viewActivity = viewActivity;
        readData();
        updatePlayersCount();
        if (getData().isOpenGame() && getData().isInProgressGame() )
            viewActivity.dialogbox_getBackGame();
        if (getData().getProfileName().equals(""))
            viewActivity.dialogbox_profile("", 0);
        viewActivity.setSavedNames(getProfilesNames());
    }

    public void displayPlayers() {
        _viewActivity.hideAddButton((getPlayersCount() == 6));
        for (int i = 0 ; i < 6; i++) {
            if (getData().getType(i).equals(Type.NoOne))
                _viewActivity.hidePlayer(getData().getColor(i));
            else
                _viewActivity.displayPlayer(getData().getIcon(i), getData().getColor(i), getData().getName(i), getData().getType(i).toString());
        }
    }

    public void initView() {
        initGame();
        setPlayersCount(2);
        saveData();
        displayPlayers();
    }

    public void updatePlayerName(int color, String name) {
        int index = getData().getIndexByColor(color);
        if (getData().getType(index).equals(Type.Human))
            getData().setName(index, name);
        saveData();
    }

    public void updateProfile(String name, int icon) {
        getData().setProfileName(name);
        getData().addProfiles(name, icon);
        _viewActivity.setSavedNames(getProfilesNames());
        saveData();
        displayPlayers();
    }

    public void addPlayer() {
        if (getPlayersCount() < 6)
            changePlayerType(findFreeIndex());
        updatePlayersCount();
        displayPlayers();
    }

    public void deletePlayer(int color) {
        getData().setType(getData().getIndexByColor(color), Type.NoOne);
        getData().setInProgressGame(false);
        saveData();
        updatePlayersCount();
        displayPlayers();
    }

    public void changePlayerTypeByColor(int color) {
        changePlayerType(getData().getIndexByColor(color));
    }

    public void changePlayerType(int index) {
        getData().setType(index, (getData().getType(index).equals(Type.Human)) ? Type.Computer : Type.Human);
        getData().setInProgressGame(false);
        saveData();
        _viewActivity.hideAddButton((getPlayersCount() == 6));
        displayPlayers();
    }

    public void startGame() {
        if (getPlayersCount() == 0)
            _viewActivity.showERRORMSG(1);
        else {
            if (!getData().isInProgressGame()) {
                getData().setRounds(1);
                getData().setCurrentIndex(0);
            }
            saveData();
            _viewActivity.openPlayActivity();
        }
    }

    // ---------------- implementation abstract class

    public Context getContext(){ return _viewActivity.getContext(); }

    public void updatePlayer(int playerIndex, boolean isHuman, String name, int color, int iconIndex){
        getData().setType(playerIndex, (isHuman) ? Type.Human : Type.Computer);
        getData().setName(playerIndex, name);
        reverseColor(playerIndex, getData().getIndexByColor(color));
        getData().setIcon(playerIndex, iconIndex);
        getData().setInProgressGame(false);
        if (isHuman)
            getData().addProfiles(name, iconIndex);
        saveData();
        displayPlayers();
    }

    public void updatePlayersCount() {
        int n = 0;
        for (Player player : getData().getPlayers())
            if (!player.getType().equals(Type.NoOne) )
                n++;
        setPlayersCount(n);
    }

    public void displayPlayerProfile(Integer color) {
        Player player = getData().getPlayerByColor(color);
        _viewActivity.dialogbox_playerProfile(player.getName(), getData().getIndexByColor(color), color, player.getIcon(), player.getType().equals(Type.Human));
    }

    public void displayProfile() {
        String name = getData().getProfileName();
        _viewActivity.dialogbox_profile(name, getData().getProfiles().get(name));
    }


    // ----------------------------------- ViewActivity --------------------------

    public interface ViewActivity {
        void displayPlayer(int iconId, int colorIndex,String name, String playerType_string);
        void showERRORMSG(Integer errorType);
        void hideAddButton(boolean hidden);
        void hidePlayer(int colorIndex);
        void openPlayActivity();
        void dialogbox_getBackGame();
        void dialogbox_profile(String name, int iconIndex);
        void setSavedNames(List<String> names);
        Context getContext();
        void dialogbox_playerProfile(String name, int index, int color, int icon, boolean isHuman);
    }
}
