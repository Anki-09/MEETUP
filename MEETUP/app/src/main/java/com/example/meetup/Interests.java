package com.example.meetup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Interests extends AppCompatActivity {
    Button code,robo,foot,cricket,dance,music,logout;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);
        String name;
        code=(Button)findViewById(R.id.code);
        robo=(Button)findViewById(R.id.robo);
        foot=(Button)findViewById(R.id.foot);
        cricket=(Button)findViewById(R.id.cricket);
        dance=(Button)findViewById(R.id.dance);
        music=(Button)findViewById(R.id.music);
        logout=(Button)findViewById(R.id.logout);
        Bundle bundle=getIntent().getExtras();
        name=bundle.getString("name");
        mAuth=FirebaseAuth.getInstance();
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB(name,"Coding");
                Bundle bundle1 = new Bundle();
                bundle1.putString("interest","Coding");
                Intent intent = new Intent(Interests.this, People.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        robo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB(name,"Robotics");
                Bundle bundle1 = new Bundle();
                bundle1.putString("interest","Robotics");
                Intent intent = new Intent(Interests.this, People.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB(name,"Football");
                Bundle bundle1 = new Bundle();
                bundle1.putString("interest","Football");
                Intent intent = new Intent(Interests.this, People.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        cricket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB(name,"Cricket");
                Bundle bundle1 = new Bundle();
                bundle1.putString("interest","Cricket");
                Intent intent = new Intent(Interests.this, People.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        dance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB(name,"Dance");
                Bundle bundle1 = new Bundle();
                bundle1.putString("interest","Dance");
                Intent intent = new Intent(Interests.this, People.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeDB(name,"Music");
                Bundle bundle1 = new Bundle();
                bundle1.putString("interest","Music");
                Intent intent = new Intent(Interests.this, People.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(Interests.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Interests.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void  storeDB(String name,String interest)
    {
        FirebaseUser user= mAuth.getCurrentUser();
        String email=user.getEmail();
        String uid=user.getUid();
        HashMap<Object,String> hashMap=new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("interest", interest);
        FirebaseDatabase db= FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference("Users");
        ref.child(uid).setValue(hashMap);
    }
}