package com.example.zsports.Models;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;



public class ContestModel implements Serializable {
    String Game;
    String Map;
    String MatchID;
    String Mode;
    String Type;
    Timestamp Date;
    String documentId;
    long Perkill;
    long EntryFee;
    long Winning_amount;
    long BookedSlot , TotalSlot;




    public ContestModel()
    {

    }

    private ContestModel(String Game,String Map,String MatchID,String Mode,String Type,Timestamp Date,long Perkill, long Winning_amount,long EntryFee,long BookedSlot,long TotalSlot)
    {
        this.BookedSlot = BookedSlot;
        this.TotalSlot = TotalSlot;
        this.Game = Game;
        this.Map = Map;
        this.MatchID = MatchID;
        this.Mode = Mode;
        this.Type = Type;
        this.Perkill= Perkill;
        this.Date = Date;
        this.EntryFee = EntryFee;
        this.Winning_amount = Winning_amount;
    }


    public long getEntryFee() {
        return EntryFee;
    }

    public void setEntryFee(long entryFee) {
        EntryFee = entryFee;
    }

    public String getGame() {
        if (Game != null)
        {
            return Game.toUpperCase();
        }else
        {
            return Game;
        }
    }

    public void setGame(String game) {
        Game = game;
    }

    public String getMap() {
        if (Map != null) {
            return Map.toUpperCase();
        }else{
            return Map;
        }
    }

    public void setMap(String map) {
        Map = map;
    }

    public String getMatchID() {
        if (MatchID != null)
        {
            return MatchID.toUpperCase();
        }else
        {
            return MatchID;
        }

    }

    public void setMatchID(String matchID) {
        MatchID = matchID;
    }

    public String getMode() {
        if (Mode != null)
        {
            return Mode.toUpperCase();
        }else
        {
            return Mode;
        }
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getType() {
        if (Type != null)
        {
            return Type.toUpperCase();
        }else
        {
            return Type;
        }
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



    public long getPerkill() {
        return Perkill;
    }

    public void setPerkill(long perkill) {
        Perkill = perkill;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public long getBookedSlot() {
        return BookedSlot;
    }

    public void setBookedSlot(long bookedSlot) {
        BookedSlot = bookedSlot;
    }

    public long getTotalSlot() {
        return TotalSlot;
    }

    public void setTotalSlot(long totalSlot) {
        TotalSlot = totalSlot;
    }


}
