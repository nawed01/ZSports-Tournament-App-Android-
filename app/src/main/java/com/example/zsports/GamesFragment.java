package com.example.zsports;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zsports.Adapters.ImageSliderAdapter;
import com.example.zsports.Models.ImagesSliderModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class GamesFragment extends Fragment {

    private RecyclerView contestList;
    private FirebaseFirestore fStore;
    public FirebaseAuth mAuth;
    private Query query;
    private FirestoreRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SliderView sliderView;
    private ImageSliderAdapter SliderAdapter;
    private LinearLayout pubgmobileicon,pubgliteicon,callofDutyicon;
    List<ImagesSliderModel> imagesSliderModelList;
    private TextView WalletBalanceView;

    public GamesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        imagesSliderModelList = new ArrayList<>();
        sliderView = view.findViewById(R.id.imageSlider);
        SliderAdapter = new ImageSliderAdapter(getContext(),imagesSliderModelList);
        imagesSliderModelList.add(new ImagesSliderModel(R.drawable.image1));
        imagesSliderModelList.add(new ImagesSliderModel(R.drawable.image3));
        sliderView.setSliderAdapter(SliderAdapter);
        pubgmobileicon = view.findViewById(R.id.pubgmobile_layout);
        pubgliteicon = view.findViewById(R.id.pubglite_layout);
        callofDutyicon = view.findViewById(R.id.callofduty_layout);
        WalletBalanceView =view.findViewById(R.id.game_wallet_balance_view);

        //Setting Wallet Balance View
        String currUser = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference =  fStore.collection("users").document(currUser).collection("UserInfo").document("WalletBalance");
        documentReference.addSnapshotListener((value, error) -> {
            assert value != null;
            String CurrBalance = String.valueOf(value.getLong("Curr_Wallet_Balance"));
            WalletBalanceView.setText(CurrBalance);
        });
        
        pubgmobileicon.setOnClickListener(v -> {
            GameModeFragment gmf = new GameModeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            assert fragmentManager != null;
            Bundle args = new Bundle();
            args.putString("game","PUBGM");
            gmf.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer,gmf).commit();
        });
        pubgliteicon.setOnClickListener(v -> {
            GameModeFragment gmf = new GameModeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            assert fragmentManager != null;
            Bundle args = new Bundle();
            args.putString("game","PUBGL");
            gmf.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer,gmf).commit();
        });
        callofDutyicon.setOnClickListener(v -> {
            GameModeFragment gmf = new GameModeFragment();
            FragmentManager fragmentManager = getFragmentManager();
            assert fragmentManager != null;
            Bundle args = new Bundle();
            args.putString("game","CODM");
            gmf.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.fragmentcontainer,gmf).commit();
        });

        return view;
    }



    @Override
    public void onStop() {
        super.onStop();
        sliderView.stopAutoCycle();
    }

    @Override
    public void onStart() {
        super.onStart();
        sliderView.startAutoCycle();
    }



}



