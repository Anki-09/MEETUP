package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button login, signup;
    EditText user, pwd;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener firebaseListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mauth=FirebaseAuth.getInstance();
        firebaseListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    String userid = mauth.getCurrentUser().getUid();
                    Bundle bundle = new Bundle();
                    DatabaseReference dataref= FirebaseDatabase.getInstance().getReference("Users").child(userid).child("name");
                    dataref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                             String name=(String)snapshot.getValue();
                             bundle.putString("name",name);
                             Intent intent = new Intent(Login.this, Interests.class);
                             intent.putExtras(bundle);
                             startActivity(intent);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        };

        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.signup);
        user=(EditText)findViewById(R.id.user);
        pwd=(EditText)findViewById(R.id.pwd);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=user.getText().toString().trim();
                String password=pwd.getText().toString().trim();
                if(email.equals("") || password.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter all the details!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mauth.addAuthStateListener(firebaseListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(firebaseListener);
    }
}