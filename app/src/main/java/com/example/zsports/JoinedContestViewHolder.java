package com.example.zsports;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class JoinedContestViewHolder extends RecyclerView.ViewHolder {

    public TextView date, time, matchID, game,mode,entryfee,
            WinningAmount,Player1username,Player2username,
            Player3username, Player4username,RoomID,RoomPassword;

    public JoinedContestViewHolder(@NonNull View itemView) {

        super(itemView);
        date = itemView.findViewById(R.id.contestDate);
        time = itemView.findViewById(R.id.contestTime);
        matchID = itemView.findViewById(R.id.joinedMatchId);
        game = itemView.findViewById(R.id.joinedGame);
        mode = itemView.findViewById(R.id.joinedMode);
        entryfee = itemView.findViewById(R.id.joinedEntryfee);
        WinningAmount = itemView.findViewById(R.id.joinedWinningAmount);
        Player1username = itemView.findViewById(R.id.player_username1);
        Player2username = itemView.findViewById(R.id.player_username2);
        Player3username = itemView.findViewById(R.id.player_username3);
        Player4username = itemView.findViewById(R.id.player_username4);
        RoomID = itemView.findViewById(R.id.joinedRoomId);
        RoomPassword = itemView.findViewById(R.id.JoinedRoomPassword);

    }
}
