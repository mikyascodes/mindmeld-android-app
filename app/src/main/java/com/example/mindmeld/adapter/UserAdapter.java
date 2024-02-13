package com.example.mindmeld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mindmeld.R;
import com.example.mindmeld.databinding.UserSampleBinding;
import com.example.mindmeld.model.Follow;
import com.example.mindmeld.model.Notification;
import com.example.mindmeld.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    ArrayList<User> list;
    private static UserAdapter.OnClickListener listener;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_sample, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        User user = list.get(position);
        Picasso.get()
                .load(user.getProfilePhoto())
                .placeholder(R.drawable.cover_placeholder)
                .into(holder.binding.profileImage);
        holder.binding.name.setText(user.getName());
        holder.binding.profession.setText(user.getProfession());

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(user.getUserID())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                            holder.binding.followBtn.setText("Following");
                            holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.silver));
                            holder.binding.followBtn.setEnabled(false);
                        } else {
                            holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Follow follow = new Follow();
                                    follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    follow.setFollowedAt(new Date().getTime());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(user.getUserID())
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Users")
                                                            .child(user.getUserID())
                                                            .child("followerCount")
                                                            .setValue(user.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.follow_active_btn));
                                                                    holder.binding.followBtn.setText("Following");
                                                                    holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.silver));
                                                                    holder.binding.followBtn.setEnabled(false);
                                                                    Toast.makeText(context, "You Followed " + user.getName(), Toast.LENGTH_SHORT).show();


                                                                    Notification notification = new Notification();
                                                                    notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notification.setNotificationAt(new Date().getTime());
                                                                    notification.setType("follow");

                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(user.getUserID())
                                                                            .push()
                                                                            .setValue(notification);

                                                                    //
                                                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                            .child("Contacts").child(user.getUserID()).setValue("Chats");
                                                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Contacts").child(user.getUserID())
                                                                            .child("interactionTime").setValue(new Date().getTime());
                                                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Contacts").child(user.getUserID())
                                                                            .child("recentMessage").setValue("");

                                                                }
                                                            });
                                                }
                                            });

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        UserSampleBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = UserSampleBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();

                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(list.get(pos));
                    }
                }
            });
        }
    }

    public interface OnClickListener {
        void onItemClick(User userData);
    }

    public void setOnItemClickListener(UserAdapter.OnClickListener listener) {
        UserAdapter.listener = listener;
    }
}

