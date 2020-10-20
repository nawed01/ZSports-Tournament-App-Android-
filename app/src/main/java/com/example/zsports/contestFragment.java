package com.example.zsports;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zsports.Models.ContestModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class contestFragment extends Fragment {


    public static  String game ;
    public static  String mode;
    private FirebaseFirestore fStore;
    private TextView WalletBalanceView;
    public FirebaseAuth mAuth;
    private Query query;
    private FirestoreRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public contestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contest, container, false);
        mAuth = FirebaseAuth.getInstance();
        RecyclerView contestList = view.findViewById(R.id.contestlist_view);
        fStore = FirebaseFirestore.getInstance();

       //Custom Back Button Function
        Button contestBackButton = view.findViewById(R.id.liveContestBack);
        contestBackButton.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            GameModeFragment gmf = new GameModeFragment();
            Bundle bundle = this.getArguments();
            assert bundle != null;
            game = bundle.getString("game");
            Bundle args2 = new Bundle();
            args2.putString("game2",game);
            gmf.setArguments(args2);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,gmf).commit();
        });

        //Setting Wallet Balance View
        String currUser = mAuth.getCurrentUser().getUid();
        WalletBalanceView = view.findViewById(R.id.contest_wallet_balance_view);
        DocumentReference documentReference =  fStore.collection("users").document(currUser).collection("UserInfo").document("WalletBalance");
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            String CurrBalance = String.valueOf(value.getLong("Curr_Wallet_Balance"));
            WalletBalanceView.setText(CurrBalance);
        });

        //Receiving value from another fragment
        Bundle bundle = this.getArguments();
        assert bundle != null;
        game = bundle.getString("game");
        mode = bundle.getString("mode");
        System.out.println(game+"  "+mode);

        //Query
        query = fStore.collection("Contest").document(game).collection(mode);

        //RecyclerOption & Adapter
        FirestoreRecyclerOptions<ContestModel> options = new FirestoreRecyclerOptions.Builder<ContestModel>()
                .setQuery(query, ContestModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ContestModel, ContestViewHolder>(options) {
            @NonNull
            @Override
            public ContestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contestlist_item_layout, parent, false);
                return new ContestViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ContestViewHolder holder, int position, @NonNull ContestModel model) {
                holder.game.setText(model.getGame());
                holder.map.setText(model.getMap());
                holder.matchID.setText(model.getMatchID());
                holder.mode.setText(model.getMode());
                holder.type.setText(model.getType());
                holder.date.setText(Date(model.getDate().toDate()));
                holder.Time.setText(Time(model.getDate().toDate()));
                holder.perkill.setText(String.valueOf(model.getPerkill()));
                holder.winning_amount.setText(String.valueOf(model.getWinning_amount()));
                holder.entryfee.setText(String.valueOf(model.getEntryFee()));
                holder.TotalSlot.setText(String.valueOf(model.getTotalSlot()));
                holder.BookedSlot.setText(String.valueOf(model.getBookedSlot()));

                if (mode.equals("SOLO"))
                {
                    holder.progressBar.setProgress((int) model.getBookedSlot());
                }

                if (mode.equals("DUO"))
                {
                    holder.progressBar.setProgress(2*(int) model.getBookedSlot());
                }

                if (mode.equals("SQUAD")) {
                    holder.progressBar.setProgress(4 * (int) model.getBookedSlot());
                }

               //New Thread*****************************************
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            //joinBtnState Check
                            System.out.println("*********THREAD STARTED**************");
                            long prebookedslot = Long.parseLong(holder.BookedSlot.getText().toString());
                            long TotalSlot = Long.parseLong(holder.TotalSlot.getText().toString());
                            long entryfee =  Long.parseLong(holder.entryfee.getText().toString());
                            long Winningamount = Long.parseLong(holder.winning_amount.getText().toString());
                            //Join Button Listener
                            if (prebookedslot<TotalSlot)
                            {
                                JoinBtnState(model, holder);
                                holder.joinButton.setOnClickListener(v -> {
                                    int View_pos = holder.getAdapterPosition();
                                    long currbookedslot = prebookedslot + 1;

                                    //Wallet Transaction
                                    long currWalletbal = Long.parseLong(WalletBalanceView.getText().toString());
                                    //Registering Contest
                                    WalletTransaction(currWalletbal,entryfee,Winningamount,View_pos,game,mode,currbookedslot,holder);

                                });
                            }else
                            {
                                JoinedState(model,holder,view,game,mode);

                            }
                        }
                        handler.post(() -> Toast.makeText(getActivity(), "Data Fetched", Toast.LENGTH_SHORT).show());
                    }
                };

                Thread DatabaseThread = new Thread(runnable);
                DatabaseThread.start();

            }

        };

        contestList.setHasFixedSize(true);
        contestList.setLayoutManager(new LinearLayoutManager(getContext()));
        contestList.setAdapter(adapter);

        //Refresh the recyclerview for data update

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
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


    public void RegisterContest(int pos, String game, String mode, Map<String, Object> playersInfo) {
        //Fetching Contest ID's
        ContestModel contestModel = new ContestModel();
        query.addSnapshotListener((value, error) -> {

            DocumentSnapshot documentSnapshot;

            if (value!=null) {
                List<DocumentSnapshot> DocList = value.getDocuments();
                documentSnapshot = DocList.get(pos);
                contestModel.setDocumentId(documentSnapshot.getId());
                contestModel.setMatchID((String) documentSnapshot.get("MatchID"));
                contestModel.setDate((Timestamp) documentSnapshot.get("Date"));
                contestModel.setEntryFee( documentSnapshot.getLong("EntryFee"));
                contestModel.setWinning_amount(documentSnapshot.getLong("Winning_amount"));
            }
            //Registering User Contest
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy G 'at' hh:mm:ss a", Locale.getDefault());
            String ContestId = contestModel.getDocumentId();
            String MatchID = contestModel.getMatchID();
            long entryfee = contestModel.getEntryFee();
            long winningamount = contestModel.getWinning_amount();
            String currUser = mAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(currUser)
                    .collection("ContestJoined").document(MatchID);
            Map<String, Object> Reg_contest = new HashMap<>();
            Reg_contest.put("PlayersInfo",playersInfo);
            Reg_contest.put("ContestID", ContestId);
            Reg_contest.put("GAME", game);
            Reg_contest.put("MODE", mode);
            Reg_contest.put("MatchID", MatchID);
            Reg_contest.put("Contest_Date", contestModel.getDate().toDate());
            Reg_contest.put("EntryFee", entryfee);
            Reg_contest.put("WinningAmount", winningamount);
            Reg_contest.put("Joined_Date",  sdf.format(new Date()));
            Reg_contest.put("Joining_Status", true);
            documentReference.set(Reg_contest);


        });
        System.out.println("*********CONTEST REGISTERED************");

    }

    //Join Button state
    private void JoinBtnState(ContestModel model, ContestViewHolder holder) {

        String currUser = mAuth.getCurrentUser().getUid();
        String MatchID = model.getMatchID();

        CollectionReference collectionReference = fStore.collection("users").document(currUser).collection("ContestJoined");

        //fetching matchlist and contest list
        collectionReference.addSnapshotListener((value, error) -> {

            if (value!=null) {
                boolean isInList = false;
                for (QueryDocumentSnapshot doc : value) {
                    System.out.println("MATCH IDS: "+doc.getId());
                    if (MatchID.equals(doc.getId()))
                    {
                        isInList = true;
                    }
                }
                if (isInList) {
                    System.out.println("FOUND MATCH !"+MatchID);
                    holder.joinButton.setText("JOINED");
                    holder.joinButton.setBackgroundResource(R.drawable.rounded_corners);
                    holder.joinButton.setEnabled(false);
                } else {
                    System.out.println("ELSE METHOD CALLED !"+MatchID);
                    holder.joinButton.setText("JOIN");
                    holder.joinButton.setBackgroundResource(R.drawable.rounded_corners);
                    holder.joinButton.setEnabled(true);
                }
                holder.joinView.setVisibility(View.INVISIBLE);

            }else {
                System.out.println("ELSE METHOD 2 CALLED !"+MatchID);
                holder.joinButton.setText("JOIN");
                holder.joinButton.setBackgroundResource(R.drawable.rounded_corners);
                holder.joinButton.setEnabled(true);
                holder.joinView.setVisibility(View.INVISIBLE);
            }

        });

    }


    private void updateSlot(int view_pos, long currbookedslot) {
        Bundle bundle2 = this.getArguments();
        assert bundle2 != null;
        game = bundle2.getString("game");
        mode = bundle2.getString("mode");
        CollectionReference collectionReference = fStore.collection("Contest").document(game).collection(mode);
        collectionReference.addSnapshotListener((value, error) -> {
            DocumentSnapshot documentSnapshot;
            assert value != null;
            List<DocumentSnapshot> ContestList = value.getDocuments();
            documentSnapshot = ContestList.get(view_pos);
            String ContestID = documentSnapshot.getId();
            System.out.println("CONTEST ID : "+ ContestID);
            DocumentReference documentReference =  fStore.collection("Contest").document(game).collection(mode).document(ContestID);
            documentReference.update("BookedSlot",currbookedslot);
        });
        System.out.println("***************SLOT UPDATED******************");
    }


    private void JoinedState(ContestModel model, ContestViewHolder holder, View view,String game,String mode)
    {
        String currUser = mAuth.getCurrentUser().getUid();
        String MatchID = model.getMatchID();

        DocumentReference documentReference = fStore.collection("users").document(currUser).collection("ContestJoined").document(MatchID);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    System.out.println("Document exists!"+MatchID);////////////////
                    holder.joinButton.setText("JOINED");
                    holder.joinButton.setBackgroundResource(R.drawable.rounded_corners);
                    holder.joinButton.setEnabled(false);
                    holder.joinView.setVisibility(View.VISIBLE);

                } else {
                    System.out.println("Document does not exist!"+MatchID);/////////////////////
                    holder.joinButton.setText("FULL");
                    holder.joinButton.setBackgroundResource(R.drawable.grey_rounded_corners);
                    holder.joinButton.setEnabled(false);
                    holder.joinView.setVisibility(View.VISIBLE);
                }
            } else {
                System.out.println("FAILED "+MatchID);
            }
        });
    }


    private void WalletTransaction(long currWalletbal, long entryfee, long winningamount, int view_pos, String game, String mode, long currbookedslot, ContestViewHolder holder)
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        if(mode.equals("SOLO"))
        {
            View dialogView = getLayoutInflater().inflate(R.layout.solo_team_reg_dialog,null);
            alert.setView(dialogView);
            final AlertDialog alertDialog = alert.create();
            TextView entryfees = dialogView.findViewById(R.id.entryfeeview);
            TextView WinningAmtView = dialogView.findViewById(R.id.winningAmtview);
            EditText game_username1 = dialogView.findViewById(R.id.solo_game_username);
            Button ConfirmButton = dialogView.findViewById(R.id.payButton);
            entryfees.setText(String.valueOf(entryfee));
            WinningAmtView.setText(String.valueOf(winningamount));

            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            ConfirmButton.setOnClickListener(v -> {

                if (currWalletbal==0)
                {
                    Toast.makeText(getActivity(), "Recharge Your Wallet !", Toast.LENGTH_SHORT).show();
                }else{
                    long updatedBal = currWalletbal-entryfee;
                    String player1_username = game_username1.getText().toString();


                    //*********VALIDATING FIELDS**************
                    if(TextUtils.isEmpty(player1_username))
                    {
                        game_username1.setError(game+" Username is required.");
                        return;
                    }
                    Map<String,Object> PlayersInfo = new HashMap<>();
                    PlayersInfo.put("player1_username",player1_username);

                    //************REGISTERING CONTEST***************
                    RegisterContest(view_pos, game,mode,PlayersInfo);
                    holder.joinButton.setText("JOINED");
                    holder.joinButton.setEnabled(false);
                    updateSlot(view_pos,currbookedslot);
                    holder.progressBar.setProgress((int) currbookedslot);


                    //******UPDATING WALLET******************
                    String currUser = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference =  fStore.collection("users").document(currUser).collection("UserInfo").document("WalletBalance");
                    documentReference.update("Curr_Wallet_Balance",updatedBal);

                    //**********Saving Transaction Details****************
                    DebitedWalletTransactionHistory(entryfee,currWalletbal,game,mode,holder);
                    alertDialog.cancel();
                    Toast.makeText(getActivity(), "Joined Successfully !", Toast.LENGTH_SHORT).show();
                }

            });
        }

        if(mode.equals("DUO"))
        {
            View dialogView = getLayoutInflater().inflate(R.layout.duo_team_reg_dialog,null);
            alert.setView(dialogView);
            final AlertDialog alertDialog = alert.create();
            TextView entryfees = dialogView.findViewById(R.id.entryfeeview);
            TextView WinningAmtView = dialogView.findViewById(R.id.winningAmtview);
            EditText game_username1 = dialogView.findViewById(R.id.duo_game_username1);
            EditText game_username2 = dialogView.findViewById(R.id.duo_game_username2);

            Button ConfirmButton = dialogView.findViewById(R.id.payButton);

            entryfees.setText(String.valueOf(entryfee));
            WinningAmtView.setText(String.valueOf(winningamount));

            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            ConfirmButton.setOnClickListener(v -> {
                if (currWalletbal==0)
                {
                    Toast.makeText(getActivity(), "Recharge Your Wallet !", Toast.LENGTH_SHORT).show();
                }else{
                    long updatedBal = currWalletbal-entryfee;
                    String player1_username = game_username1.getText().toString();
                    String player2_username = game_username2.getText().toString();


                    if(TextUtils.isEmpty(player1_username))
                    {
                        game_username1.setError(game+" Username is required.");
                        return;
                    }
                    if(TextUtils.isEmpty(player2_username))
                    {
                        game_username2.setError(game+" Username is required.");
                        return;
                    }
                    Map<String,Object> PlayersInfo = new HashMap<>();
                    PlayersInfo.put("player1_username",player1_username);
                    PlayersInfo.put("player2_username",player2_username);
                    //************REGISTERING CONTEST***************
                    RegisterContest(view_pos, game,mode,PlayersInfo);
                    holder.joinButton.setText("JOINED");
                    holder.joinButton.setEnabled(false);
                    updateSlot(view_pos,currbookedslot);
                    holder.progressBar.setProgress(2*(int) currbookedslot);

                    //******UPDATING WALLET******************
                    String currUser = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference =  fStore.collection("users").document(currUser).collection("UserInfo").document("WalletBalance");
                    documentReference.update("Curr_Wallet_Balance",updatedBal);

                    //**********Saving Transaction Details****************
                    DebitedWalletTransactionHistory(entryfee,currWalletbal,game,mode,holder);
                    alertDialog.cancel();
                    Toast.makeText(getActivity(), "Joined Successfully !", Toast.LENGTH_SHORT).show();
                }

            });
        }

        if(mode.equals("SQUAD"))
        {
            View dialogView = getLayoutInflater().inflate(R.layout.squad_team_reg_dialog,null);
            alert.setView(dialogView);
            final AlertDialog alertDialog = alert.create();
            TextView entryfees = dialogView.findViewById(R.id.entryfeeview);
            TextView WinningAmtView = dialogView.findViewById(R.id.winningAmtview);
            EditText game_username1 = dialogView.findViewById(R.id.squad_game_username1);
            EditText game_username2 = dialogView.findViewById(R.id.squad_game_username2);
            EditText game_username3 = dialogView.findViewById(R.id.squad_game_username3);
            EditText game_username4 = dialogView.findViewById(R.id.squad_game_username4);

            Button ConfirmButton = dialogView.findViewById(R.id.payButton);

            entryfees.setText(String.valueOf(entryfee));
            WinningAmtView.setText(String.valueOf(winningamount));

            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

            ConfirmButton.setOnClickListener(v -> {
                if (currWalletbal==0)
                {
                    Toast.makeText(getActivity(), "Recharge Your Wallet !", Toast.LENGTH_SHORT).show();
                }else{
                    long updatedBal = currWalletbal-entryfee;
                    String player1_username = game_username1.getText().toString();
                    String player2_username = game_username2.getText().toString();
                    String player3_username = game_username3.getText().toString();
                    String player4_username = game_username4.getText().toString();


                    if(TextUtils.isEmpty(player1_username))
                    {
                        game_username1.setError(game+" Username is required.");
                        return;
                    }
                    if(TextUtils.isEmpty(player2_username))
                    {
                        game_username2.setError(game+" Username is required.");
                        return;
                    }
                    if(TextUtils.isEmpty(player3_username))
                    {
                        game_username3.setError(game+" Username is required.");
                        return;
                    }
                    if(TextUtils.isEmpty(player4_username))
                    {
                        game_username4.setError(game+" Username is required.");
                        return;
                    }

                    Map<String,Object> PlayersInfo = new HashMap<>();
                    PlayersInfo.put("player1_username",player1_username);
                    PlayersInfo.put("player2_username",player2_username);
                    PlayersInfo.put("player3_username",player3_username);
                    PlayersInfo.put("player4_username",player4_username);

                    //************REGISTERING CONTEST***************
                    RegisterContest(view_pos, game,mode,PlayersInfo);
                    holder.joinButton.setText("JOINED");
                    holder.joinButton.setEnabled(false);
                    updateSlot(view_pos,currbookedslot);
                    holder.progressBar.setProgress(4*(int) currbookedslot);

                    //******UPDATING WALLET******************
                    String currUser = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference =  fStore.collection("users").document(currUser).collection("UserInfo").document("WalletBalance");
                    documentReference.update("Curr_Wallet_Balance",updatedBal);

                    //**********Saving Transaction Details****************
                    DebitedWalletTransactionHistory(entryfee,currWalletbal,game,mode,holder);
                    alertDialog.cancel();
                    Toast.makeText(getActivity(), "Joined Successfully !", Toast.LENGTH_SHORT).show();
                }

            });
        }



    }

    private void DebitedWalletTransactionHistory(long entryfee, long currWalletbal, String game, String mode,ContestViewHolder holder)
    {
        String userID = mAuth.getCurrentUser().getUid();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy G 'at' HH:mm:ss a", Locale.getDefault());
        String MatchID = holder.matchID.getText().toString();
        System.out.println("MATCH ID WALLET : "+MatchID);
        Map<String,Object> TransactionDetails = new HashMap<>();
        TransactionDetails.put("DeductedEntryFee",entryfee);
        TransactionDetails.put("CurrBal",currWalletbal);
        TransactionDetails.put("Date_Time",sdf.format(new Date()));
        TransactionDetails.put("Game", game);
        TransactionDetails.put("Mode", mode);

       CollectionReference collectionReference =  fStore.collection("users").document(userID).collection("UserInfo").document("WalletTransactionHistory").collection("Debited");
       collectionReference.document(MatchID).set(TransactionDetails);
    }

    private String Date(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());

        return sdf.format(date);
    }

    private String Time(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a",Locale.getDefault());
        return sdf.format(date);
    }

}
