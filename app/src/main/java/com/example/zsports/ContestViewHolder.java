package com.example.zsports;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



class ContestViewHolder extends RecyclerView.ViewHolder {

    public TextView game,map,matchID,mode,type,date,winning_amount,perkill,entryfee, TotalSlot,BookedSlot,joinView,Time;
    public Button joinButton;
    public  ProgressBar progressBar;

    public ContestViewHolder(@NonNull View itemView) {
        super(itemView);
        joinView = itemView.findViewById(R.id.slot_full_view);
        progressBar = itemView.findViewById(R.id.joining_bar);
        TotalSlot = itemView.findViewById(R.id.total_slot_view);
        BookedSlot = itemView.findViewById(R.id.joining_view);
        joinButton= itemView.findViewById(R.id.contest_join_button);
        game = itemView.findViewById(R.id.game_view);
        map = itemView.findViewById(R.id.map_view2);
        matchID = itemView.findViewById(R.id.matchid_view2);
        mode = itemView.findViewById(R.id.mode_view2);
        type = itemView.findViewById(R.id.type_view2);
        date = itemView.findViewById(R.id.date_view2);
        Time = itemView.findViewById(R.id.timeView2);
        perkill = itemView.findViewById(R.id.perkill_view2);
        winning_amount = itemView.findViewById(R.id.winning_amt_view2);
        entryfee = itemView.findViewById(R.id.entry_fee_view2);

    }

}
