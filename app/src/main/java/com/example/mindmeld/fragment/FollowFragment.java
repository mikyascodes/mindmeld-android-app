package com.example.mindmeld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mindmeld.MessagingActivity;
import com.example.mindmeld.adapter.UserAdapter;
import com.example.mindmeld.databinding.FragmentFollowBinding;
import com.example.mindmeld.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowFragment extends Fragment {
    FragmentFollowBinding binding;
    ArrayList<User> list = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<User> searchedUser = new ArrayList<>(1);

    public FollowFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater, container, false);

        binding.usersRV.showShimmerAdapter();

        UserAdapter adapter = new UserAdapter(getContext(), list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.usersRV.setLayoutManager(layoutManager);


        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setUserID(dataSnapshot.getKey());
                    if (!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(user);
                    }
                }

                binding.usersRV.setAdapter(adapter);
                binding.usersRV.hideShimmerAdapter();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //--------------search-------------
        UserAdapter adapter2 = new UserAdapter(getContext(), searchedUser);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        binding.usersRV.setLayoutManager(layoutManager);
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                binding.searchView.clearFocus();
                searchedUser.clear();

                database.getReference("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        boolean flag = false;

                        for (DataSnapshot e : snapshot.getChildren()) {

                            flag = e.getKey().equals(auth.getCurrentUser().getUid());


                            if (!flag && e.child("email").getValue().equals(query.trim())) {


                                Log.d("testcontact", " " + query + "  name= " + e.child("name").getValue().toString());
                                User user = new User();
                                user.setName(e.child("name").getValue().toString());

                                user.setUserID(e.getKey());
                                searchedUser.add(user);
                            }
                        }

                        binding.usersRV.setAdapter(adapter2);
                        binding.usersRV.hideShimmerAdapter();
                        adapter2.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        adapter.setOnItemClickListener(new UserAdapter.OnClickListener() {
            @Override
            public void onItemClick(User userdata) {


                Intent intent = new Intent(getActivity(), MessagingActivity.class);
                intent.putExtra("USERNAME", userdata.getName());
                intent.putExtra("PROFILEIMAGE", userdata.getProfile());
                intent.putExtra("USERID", userdata.getUserID());
                intent.putExtra("TOKEN", userdata.getToken());
                startActivity(intent);


            }
        });

        adapter2.setOnItemClickListener(new UserAdapter.OnClickListener() {
            @Override
            public void onItemClick(User userdata) {


                Intent intent = new Intent(getActivity(), MessagingActivity.class);
                intent.putExtra("USERNAME", userdata.getName());
                intent.putExtra("PROFILEIMAGE", userdata.getProfile());
                intent.putExtra("USERID", userdata.getUserID());
                intent.putExtra("TOKEN", userdata.getToken());
                startActivity(intent);


            }
        });


        return binding.getRoot();
    }

}