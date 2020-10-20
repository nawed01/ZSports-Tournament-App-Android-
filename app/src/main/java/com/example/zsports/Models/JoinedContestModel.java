package com.example.zsports.Models;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class JoinedContestModel {

    Map<String,Object> PlayersInfo;
    Timestamp Contest_Date;
    long EntryFee;
    long WinningAmount;

    String GAME;
    String MODE;
    String MatchID;


    JoinedContestModel()
    {

    }

    public JoinedContestModel(Map<String, Object> playersInfo, Timestamp contest_Date, long entryFee, long winningAmount,String GAME,String MODE,String MatchID,String player1_username, String player2_username, String player3_username, String player4_username) {
        PlayersInfo = playersInfo;
        Contest_Date = contest_Date;
        EntryFee = entryFee;
        WinningAmount = winningAmount;
        this.GAME = GAME;
        this.MODE = MODE;
        this.MatchID = MatchID;

    }


    public Timestamp getContest_Date() {
        return Contest_Date;
    }

    public void setContest_Date(Timestamp contest_Date) {
        Contest_Date = contest_Date;
    }

    public long getEntryFee() {
        return EntryFee;
    }

    public void setEntryFee(long entryFee) {
        EntryFee = entryFee;
    }

    public long getWinningAmount() {
        return WinningAmount;
    }

    public void setWinningAmount(long winningAmount) {
        WinningAmount = winningAmount;
    }

    public String getGAME() {
        return GAME;
    }

    public void setGAME(String GAME) {
        this.GAME = GAME;
    }

    public String getMODE() {
        return MODE;
    }

    public void setMODE(String MODE) {
        this.MODE = MODE;
    }

    public String getMatchID() {
        return MatchID;
    }

    public void setMatchID(String matchID) {
        MatchID = matchID;
    }


    public void setPlayersInfo(HashMap<String, Object> playersInfo) {
        PlayersInfo = playersInfo;
    }

    public Map<String, Object> getPlayersInfo() {
        return PlayersInfo;
    }

}
