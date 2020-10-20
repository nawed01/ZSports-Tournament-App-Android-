package com.example.zsports;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zsports.Models.JoinedContestModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class onGoingFragment extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private FirestoreRecyclerAdapter adapter;
    private Query query;
    private SwipeRefreshLayout swipeRefreshLayout;
    public onGoingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_on_going, container, false);
        mAuth = FirebaseAuth.getInstance();
        RecyclerView joinedContestList = view.findViewById(R.id.joinedcontestlist_view);
        fStore = FirebaseFirestore.getInstance();
        String currUSer = mAuth.getCurrentUser().getUid();
        query = fStore.collection("users").document(currUSer).collection("ContestJoined");

        FirestoreRecyclerOptions<JoinedContestModel> options = new FirestoreRecyclerOptions.Builder<JoinedContestModel>().setQuery(query,JoinedContestModel.class).build();;
       adapter = new FirestoreRecyclerAdapter<JoinedContestModel,JoinedContestViewHolder>(options) {

           @NonNull
           @Override
           public JoinedContestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
               View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoinglist_item_layout, parent, false);
               return new JoinedContestViewHolder(view);
           }

           @Override
           protected void onBindViewHolder(@NonNull JoinedContestViewHolder holder, int position, @NonNull JoinedContestModel model) {

               String Player1username, Player2username, Player3username, Player4username;
              if(model.getPlayersInfo().get("player1_username")==null)
              {
                   Player1username ="";
              }else{
                   Player1username = model.getPlayersInfo().get("player1_username").toString();
              }

               if(model.getPlayersInfo().get("player2_username")==null)
               {
                    Player2username ="";
               }else{
                    Player2username = model.getPlayersInfo().get("player2_username").toString();
               }

               if(model.getPlayersInfo().get("player3_username")==null)
               {
                    Player3username ="";
               }else{
                    Player3username = model.getPlayersInfo().get("player3_username").toString();
               }

               if(model.getPlayersInfo().get("player4_username")==null)
               {
                    Player4username ="";
               }else{
                    Player4username = model.getPlayersInfo().get("player4_username").toString();
               }

               holder.game.setText(model.getGAME());
               holder.mode.setText(model.getMODE());
               holder.entryfee.setText(String.valueOf(model.getEntryFee()));
               holder.WinningAmount.setText(String.valueOf(model.getWinningAmount()));
               holder.date.setText(Date(model.getContest_Date().toDate()));
               holder.time.setText(Time(model.getContest_Date().toDate()));
               holder.matchID.setText(model.getMatchID());
               holder.Player1username.setText(Player1username);
               holder.Player2username.setText(Player2username);
               holder.Player3username.setText(Player3username);
               holder.Player4username.setText(Player4username);
           }
       };

        joinedContestList.setHasFixedSize(true);
        joinedContestList.setLayoutManager(new LinearLayoutManager(getContext()));
        joinedContestList.setAdapter(adapter);

         swipeRefreshLayout = view.findViewById(R.id.joinedswipeRefreshLayout);
         swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;

    }



    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    private String Date(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        return sdf.format(date);
    }

    private String Time(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a",Locale.getDefault());
        return sdf.format(date);
    }


}
