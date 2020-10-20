package com.example.zsports;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.jetbrains.annotations.NotNull;

public class GameModeFragment extends Fragment {

    private CardView soloMode,duoMode,squadMode;
    private Button FragmentbackButton;
    public static String game;
    public GameModeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_mode, container, false);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        if( bundle.getString("game")==null)
        {
            game = bundle.getString("game2");
            System.out.println("GAME IF : "+game);
        }else
        {
            game =  bundle.getString("game");
            System.out.println("GAME ELSE : "+game);
        }


        duoMode = view.findViewById(R.id.duo_card2);
        soloMode = view.findViewById(R.id.solo_card1);
        squadMode = view.findViewById(R.id.squad_card3);

        soloMode.setOnClickListener(v -> {
            contestFragment cf = new contestFragment();
            assert getFragmentManager() != null;
            Bundle args = new Bundle();
            args.putString("game",game);
            args.putString("mode","SOLO");
            cf.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,cf).commit();

        });

        duoMode.setOnClickListener(v -> {
            contestFragment cf = new contestFragment();
            assert getFragmentManager() != null;
            Bundle args = new Bundle();
            args.putString("game",game);
            args.putString("mode","DUO");
            cf.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,cf).commit();

        });

        squadMode.setOnClickListener(v -> {
            contestFragment cf = new contestFragment();
            assert getFragmentManager() != null;
            Bundle args = new Bundle();
            args.putString("game",game);
            args.putString("mode","SQUAD");
            cf.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,cf).commit();

        });
        FragmentbackButton = view.findViewById(R.id.GameModeBack);
        FragmentbackButton.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,new GamesFragment()).commit();
        });

        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
    }
}