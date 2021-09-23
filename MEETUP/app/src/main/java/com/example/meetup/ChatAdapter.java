package com.example.meetup;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.myHolder>{
    private static final int MSG_LEFT=0;
    private static final int MSG_RIGHT=1;
    List<ChatModel> chatlist;
    Context context;
    FirebaseUser user;

    public ChatAdapter(List<ChatModel> chatlist, Context context) {
        this.chatlist = chatlist;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_RIGHT)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_right,parent,false);
            return new myHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.chat_left,parent,false);
            return new myHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
       String msg=chatlist.get(position).getMessage();
       String time=chatlist.get(position).getTime();
       Calendar cal=Calendar.getInstance(Locale.ENGLISH);
       cal.setTimeInMillis(Long.parseLong(time));
       String datetime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
       holder.msguser.setText(msg);
       holder.time.setText(datetime);
    }

    @Override
    public int getItemViewType(int position) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        if (chatlist.get(position).getSender().equals(user.getUid()))
        {
            return MSG_RIGHT;
        }
        else
        {
            return MSG_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    class  myHolder extends RecyclerView.ViewHolder
    {
        TextView msguser,time;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            msguser=itemView.findViewById(R.id.msguser);
            time=itemView.findViewById(R.id.time);
        }
    }
}
