package com.example.zsports.Models;

import com.google.firebase.Timestamp;

public class ResultModel {
    String Game;
    String MatchID;
    String Type;
    Timestamp Date;
    long Winning_amount;

    public ResultModel()
    {

    }

    public ResultModel(String game, String matchID, String type, Timestamp date, long winning_amount) {
        Game = game;
        MatchID = matchID;
        Type = type;
        Date = date;
        Winning_amount = winning_amount;
    }


    public String getGame() {
        return Game;
    }

    public void setGame(String game) {
        Game = game;
    }

    public String getMatchID() {
        return MatchID;
    }

    public void setMatchID(String matchID) {
        MatchID = matchID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Timestamp getDate() {
        return Date;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public long getWinning_amount() {
        return Winning_amount;
    }

    public void setWinning_amount(long winning_amount) {
        Winning_amount = winning_amount;
    }


}
