package com.example.zsports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
private TextView Register;
private EditText mUseremail, mUserpass;
private Button Login_btn;
private ProgressBar mLoginProgressbar;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        Register = findViewById(R.id.Registerview);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);  /////need to change the .class
                startActivity(intent);
            }
        });

        mUseremail = findViewById(R.id.emailview);
        mUserpass = findViewById(R.id.passwordview);
        mLoginProgressbar = findViewById(R.id.mLoginprogressBar2);
        Login_btn = findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();

        //Checking USer Already Logged in or Registered

        if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mUseremail.getText().toString().trim();
                String password1 = mUserpass.getText().toString().trim();


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

                mLoginProgressbar.setVisibility(View.VISIBLE);

                //authenticate the user now

                mAuth.signInWithEmailAndPassword(email, password1).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(),"Logged In Successfully",Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Error !" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        mLoginProgressbar.setVisibility(View.GONE);
                    }
                });

            }
        });


    }
}
