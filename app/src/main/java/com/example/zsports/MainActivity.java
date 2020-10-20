package com.example.zsports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
 private FirebaseAuth mAuth;
 private FirebaseFirestore fStore;
 private String  userID;
 private Toolbar MyToolbar;
 private long backPressedTime;
 private Toast backToast;
 TextView fullname,email,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        fullname = findViewById(R.id.profile_name);
//        email = findViewById(R.id.profile_email);
//        phone = findViewById(R.id.profile_phone);
//
//        mAuth = FirebaseAuth.getInstance();
//        fStore =  FirebaseFirestore.getInstance();
//        userID = mAuth.getCurrentUser().getUid();

//        //Fetching data from firebase
//        DocumentReference documentReference = fStore.collection("users").document(userID);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                phone.setText(value.getString("phone"));
//                fullname.setText(value.getString("fullName"));
//                email.setText(value.getString("email"));
//            }
//        });

//        MyToolbar = findViewById(R.id.myToolbar);
//        setSupportActionBar(MyToolbar);
        ChipNavigationBar bottomNavigationView = findViewById(R.id.bottomNavigationView);
        if (savedInstanceState == null)
        {
            bottomNavigationView.setItemSelected(R.id.contest,true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,new GamesFragment()).commit();
        }
        bottomNavigationView.setOnItemSelectedListener(i -> {
            Fragment selectedFragment = null;
            switch (i)
            {
                case R.id.contest:
                    selectedFragment = new GamesFragment();

                    break;
                case R.id.ongoing:
                    selectedFragment = new onGoingFragment();

                    break;
                case R.id.reuslts:
                    selectedFragment = new ResultFragment();
                    break;
                case R.id.more:
                    selectedFragment = new moreFragment();
                    break;
            }
            assert selectedFragment != null;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentcontainer,selectedFragment).commit();

        });




    }


    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis())
        {
            backToast.cancel();
            super.onBackPressed();
            return;
        }else
        {
            backToast = Toast.makeText(this,"Press again to exit.",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
