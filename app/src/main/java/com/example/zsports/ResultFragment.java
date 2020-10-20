package com.example.zsports;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zsports.Models.ResultModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ResultFragment extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private FirestoreRecyclerAdapter adapter;
    private Query query;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ResultFragment() {
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
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        mAuth = FirebaseAuth.getInstance();
        RecyclerView resultList = view.findViewById(R.id.resultlist_view);
        fStore = FirebaseFirestore.getInstance();
        String currUSer = mAuth.getCurrentUser().getUid();

        query = fStore.collection("Result");
        FirestoreRecyclerOptions<ResultModel> options = new FirestoreRecyclerOptions.Builder<ResultModel>()
                .setQuery(query, ResultModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ResultModel, >



        swipeRefreshLayout = view.findViewById(R.id.resultswipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }
}
