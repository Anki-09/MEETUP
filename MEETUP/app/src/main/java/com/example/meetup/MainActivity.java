package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button regsignup,regsignin;
    EditText reguser,regpwd,name;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener firebaseListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regsignup=(Button)findViewById(R.id.regsignup);
        regsignin=(Button)findViewById(R.id.regsignin);
        reguser=(EditText)findViewById(R.id.reguser);
        regpwd=(EditText)findViewById(R.id.regpwd);
        name=(EditText)findViewById(R.id.name);
        mauth=FirebaseAuth.getInstance();
        firebaseListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    String userid = mauth.getCurrentUser().getUid();
                    Bundle bundle = new Bundle();
                    if(name.getText().toString().equals(""))
                    {
                        DatabaseReference dataref= FirebaseDatabase.getInstance().getReference("Users").child(userid).child("name");
                        dataref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name=(String)snapshot.getValue();
                                bundle.putString("name",name);
                                Intent intent = new Intent(MainActivity.this, Interests.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else
                    {
                        bundle.putString("name",name.getText().toString());
                        Intent intent = new Intent(MainActivity.this, Interests.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        };
        regsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email=reguser.getText().toString();
                password=regpwd.getText().toString();
                if(email.equals("") || password.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter all the details!!", Toast.LENGTH_SHORT).show();
                }
                else if (!isValidPassword(password.trim()))
                {
                    Toast.makeText(MainActivity.this, "Enter Valid Password with combination of capital letters, small letters, digits and special characters", Toast.LENGTH_LONG).show();
                }
                else {
                    mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        regsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
    public boolean isValidPassword(final String Password){
        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN="^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&*+=])(?=\\S+$).{8,}$";
        pattern=Pattern.compile(PASSWORD_PATTERN);
        matcher=pattern.matcher(Password);
        return matcher.matches();
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