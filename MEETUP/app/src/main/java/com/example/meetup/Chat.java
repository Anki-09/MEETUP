package com.example.meetup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meetup.databinding.ActivityChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Chat extends AppCompatActivity {
    TextView name,email;
    EditText chat;
    ImageButton send;
    ActivityChatBinding binding;
    ArrayList<ChatModel> list =new ArrayList<>();
    ChatAdapter adapter=new ChatAdapter(list,Chat.this);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        chat=(EditText)findViewById(R.id.chatting);
        send=(ImageButton)findViewById(R.id.send);

        String uid=getIntent().getStringExtra("uid");
        String nameuser=getIntent().getStringExtra("name");
        String emailuser=getIntent().getStringExtra("email");

        name.setText(nameuser);
        email.setText(emailuser);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                for (DataSnapshot ds:snapshot.getChildren())
                {
                    String message=(String)ds.child("message").getValue();
                    String sender=(String)ds.child("sender").getValue();
                    String receiver=(String)ds.child("receiver").getValue();
                    String timestamp=(String)ds.child("timestamp").getValue();
                    if((receiver.equals(uid) && sender.equals(user.getUid()))
                            || (sender.equals(uid) && receiver.equals(user.getUid())))
                    {
                        list.add(new ChatModel(message,receiver,sender,timestamp));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.recycle.setAdapter(adapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.recycle.setLayoutManager(layoutManager);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=chat.getText().toString().trim();
                if(msg.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Type something to send", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String time=String.valueOf(System.currentTimeMillis());
                    final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference dataref= FirebaseDatabase.getInstance().getReference("Chats");
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("sender",user.getUid());
                    hashMap.put("receiver",uid);
                    hashMap.put("message",msg);
                    hashMap.put("timestamp",time);
                    dataref.push().setValue(hashMap);
                    chat.setText("");
                }
            }
        });

    }

}