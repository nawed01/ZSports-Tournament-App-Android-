package com.example.zsports;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    public static final String TAG = "TAG";
    private TextView mLoginview;
private EditText mUserphone, mUseremail, mUsername,mUserpass2,mUserpass;
private Button mRegister_btn;
private ProgressBar mLoginprogressbar;
private  FirebaseAuth mAuth;
private FirebaseFirestore fStore;
private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mLoginview = findViewById(R.id.Loginview);
        mLoginview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
              }
        });

        mUserphone = findViewById(R.id.profile_phone);
        mUseremail = findViewById(R.id.profile_email);
        mUsername = findViewById(R.id.profile_name);
        mUserpass = findViewById(R.id.user_pass);
        mUserpass2 = findViewById(R.id.user_pass2);
        mRegister_btn = findViewById(R.id.register_btn);
        mLoginprogressbar = findViewById(R.id.Login_progressbar);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();





        mRegister_btn.setOnClickListener(v -> {
            String email = mUseremail.getText().toString().trim();
            String password1 = mUserpass.getText().toString().trim();
            String password2 = mUserpass2.getText().toString().trim();
            String userfullname = mUsername.getText().toString();
            String user_phone = mUserphone.getText().toString();
            //Validating Fields
            if(TextUtils.isEmpty(email))
            {
                mUseremail.setError("Email Is Required.");
                return;
            }

            if(TextUtils.isEmpty(password1))
            {
                mUserpass.setError("Password Is Required.");
                return;
            }

            if(password1.length()<6)
            {
                mUserpass.setError("Password Must Be Greater or Equal To 6 Characters .");
                return;
            }
            if(!(password1.equals(password2)))
            {

               mUserpass.setError("Password Doesn't Match.");
                mUserpass2.setError("Password Doesn't Match.");
                return;
            }

            mLoginprogressbar.setVisibility(View.VISIBLE);

            //Register the user now in firebase

            mAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(task -> {
               if(task.isSuccessful())
               {
                   Toast.makeText(getApplicationContext(),"User Created.",Toast.LENGTH_SHORT).show();

                   //Saving data in firebase-firestore

                    userID = mAuth.getCurrentUser().getUid();
                   DocumentReference documentReference = fStore.collection("users").document(userID);
                   Map<String,Object> user = new HashMap<>();
                   user.put("fullName",userfullname);
                   user.put("email",email);
                   user.put("phone",user_phone);

                   //User Wallet Creation................................
                   Map<String,Object> UserWallet = new HashMap<>();
                   UserWallet.put("Curr_Wallet_Balance",0);
                   //Writing user data................................
                    documentReference.collection("UserInfo").document("Info").set(user);
                   documentReference.collection("UserInfo").document("WalletBalance").set(UserWallet);
                    //Changing to next Activity
                   System.out.println("*******************USER REGISTERED*********************");
                   Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                   startActivity( new Intent(getApplicationContext(),MainActivity.class));
               }else
               {
                   Toast.makeText(getApplicationContext(),"Error !" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                   mLoginprogressbar.setVisibility(View.GONE);
               }

            });
        });

    }

}
