package com.example.chatwave;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.*;

public class ChatFragment extends Fragment {

    private static final String ARG_RECEIVER_UID = "receiver_uid";
    private static final String ARG_RECEIVER_NAME = "receiver_name";

    private String receiverUid, receiverName, senderUid;
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView messageRecycler;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private TextView userNameTextView;

    public static ChatFragment newInstance(String uid, String name) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECEIVER_UID, uid);
        args.putString(ARG_RECEIVER_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        receiverUid = getArguments().getString(ARG_RECEIVER_UID);
        receiverName = getArguments().getString(ARG_RECEIVER_NAME);
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // View initialization
        userNameTextView = view.findViewById(R.id.chat_user_name);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
        messageRecycler = view.findViewById(R.id.messageRecycler);

        // Set receiver name and enable change option
        userNameTextView.setText(receiverName);
        userNameTextView.setOnClickListener(v -> showChangeNameDialog());

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, senderUid);
        messageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        messageRecycler.setAdapter(messageAdapter);

        String chatId = getChatId(senderUid, receiverUid);

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);

        // Listen to message updates
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot msgSnap : snapshot.getChildren()) {
                    Message message = msgSnap.getValue(Message.class);
                    if (message != null) messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
                messageRecycler.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });

        sendButton.setOnClickListener(v -> {
            String msgText = messageInput.getText().toString().trim();
            if (TextUtils.isEmpty(msgText)) return;

            Message message = new Message(senderUid, receiverUid, msgText, System.currentTimeMillis());
            chatRef.push().setValue(message);
            messageInput.setText("");
        });

        // Optionally: Load latest name from Firebase
        loadReceiverNameFromFirebase();
    }

    private String getChatId(String uid1, String uid2) {
        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }

    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Change Your Name");

        final EditText input = new EditText(requireContext());
        input.setText(receiverName);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(receiverUid) // You can use senderUid if changing your own name
                        .child("name")
                        .setValue(newName)
                        .addOnSuccessListener(aVoid -> {
                            receiverName = newName;
                            userNameTextView.setText(newName);
                            Toast.makeText(getContext(), "Name updated", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void loadReceiverNameFromFirebase() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(receiverUid);
        userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                if (!TextUtils.isEmpty(name)) {
                    receiverName = name;
                    userNameTextView.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // No action needed
            }
        });
    }
}
