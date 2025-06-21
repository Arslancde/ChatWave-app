package com.example.chatwave;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.*;

public class UserListFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseReference usersRef;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProgressBar loadingIndicator = view.findViewById(R.id.loadingIndicator);
        TextView noUsersText = view.findViewById(R.id.noUsersText);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        recyclerView = view.findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, getContext(), user -> {
            ChatFragment chatFragment = ChatFragment.newInstance(user.getUid(), user.getName());
            ((MainActivity) requireActivity()).loadFragment(chatFragment);
        });

        recyclerView.setAdapter(userAdapter);

        // Logout action
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            ((MainActivity) requireActivity()).loadFragment(new LoginFragment());
        });

        loadingIndicator.setVisibility(View.VISIBLE);
        noUsersText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    if (user != null && !user.getUid().equals(currentUserId)) {
                        userList.add(user);
                    }
                }

                loadingIndicator.setVisibility(View.GONE);

                if (userList.isEmpty()) {
                    noUsersText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noUsersText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingIndicator.setVisibility(View.GONE);
                noUsersText.setVisibility(View.VISIBLE);
                noUsersText.setText("Failed to load users.");
            }
        });
    }
}
