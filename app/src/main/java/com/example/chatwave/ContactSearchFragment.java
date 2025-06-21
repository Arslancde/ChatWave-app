package com.example.chatwave;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactSearchFragment extends Fragment {

    private static final int REQUEST_CONTACTS = 100;
    private RecyclerView contactsRecyclerView;
    private ContactAdapter contactAdapter;
    private List<User> matchedUsers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_search, container, false);
        contactsRecyclerView = view.findViewById(R.id.contacts_recycler_view);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        contactAdapter = new ContactAdapter(requireContext(), matchedUsers, user -> {
            // Handle contact click — open chat with selected user
            ChatFragment chatFragment = ChatFragment.newInstance(user.getUid(), user.getName());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, chatFragment)
                    .addToBackStack(null)
                    .commit();
        });

        contactsRecyclerView.setAdapter(contactAdapter);

        checkContactPermission();
        return view;
    }

    private void checkContactPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
        } else {
            loadContactsFromPhone();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContactsFromPhone();
        } else {
            Toast.makeText(getContext(), "Permission denied to read contacts", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadContactsFromPhone() {
        Set<String> phoneNumbers = new HashSet<>();
        Cursor cursor = requireContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String number = cursor.getString(
                        cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                number = number.replaceAll("\\s+", "").replaceAll("-", "");
                phoneNumbers.add(number);
            }
            cursor.close();
        }

        fetchRegisteredUsersFromFirebase(phoneNumbers);
    }

    private void fetchRegisteredUsersFromFirebase(Set<String> phoneNumbers) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchedUsers.clear();
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    User user = userSnap.getValue(User.class);
                    if (user != null && user.getPhone() != null) {
                        String userPhone = user.getPhone().replaceAll("\\s+", "").replaceAll("-", "");
                        if (phoneNumbers.contains(userPhone)) {
                            matchedUsers.add(user);
                        }
                    }
                }
                contactAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
