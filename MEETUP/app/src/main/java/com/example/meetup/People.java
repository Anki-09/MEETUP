package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.meetup.databinding.ActivityPeopleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class People extends AppCompatActivity {
    ActivityPeopleBinding binding;
    ArrayList<MainModel> list= new ArrayList<>();
    MainAdapter adapter=new MainAdapter(list,this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityPeopleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle=getIntent().getExtras();
        String interest=bundle.getString("interest");
        getusers(interest);

        binding.recycleview.setAdapter(adapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.recycleview.setLayoutManager(layoutManager);
    }
    public void getusers(String interest)
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference dataref=FirebaseDatabase.getInstance().getReference("Users");
        dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    String name=(String)ds.child("name").getValue();
                    String uid=(String)ds.child("uid").getValue();
                    String email=(String)ds.child("email").getValue();
                    String interest1=(String)ds.child("interest").getValue();
                    if(!uid.equals(user.getUid()) && interest.equals(interest1)) {
                        list.add(new MainModel(name, uid, email, interest1));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}