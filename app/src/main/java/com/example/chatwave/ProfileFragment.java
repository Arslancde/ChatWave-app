package com.example.chatwave;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.*;

public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private EditText nameInput;
    private TextView emailTextView;
    private Button changeImageButton, saveNameButton;

    private Uri selectedImageUri;
    private String currentUserId;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    profileImageView.setImageURI(selectedImageUri);
                    uploadNewImage();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImageView = view.findViewById(R.id.profileImageView);
        nameInput = view.findViewById(R.id.nameInput);
        emailTextView = view.findViewById(R.id.emailTextView);
        changeImageButton = view.findViewById(R.id.changeImageButton);
        saveNameButton = view.findViewById(R.id.saveNameButton);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadUserProfile();

        changeImageButton.setOnClickListener(v -> openGallery());

        saveNameButton.setOnClickListener(v -> {
            String newName = nameInput.getText().toString().trim();
            if (!newName.isEmpty()) {
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(currentUserId)
                        .child("name")
                        .setValue(newName)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to update name", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfile() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    nameInput.setText(user.getName());
                    emailTextView.setText(user.getEmail());
                    if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
                        Glide.with(requireContext())
                                .load(user.getProfileImageUrl())
                                .placeholder(R.drawable.ic_default_profile)
                                .into(profileImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void uploadNewImage() {
        if (selectedImageUri == null) return;

        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference("profile_images/" + currentUserId + ".jpg");

        storageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String newImageUrl = uri.toString();
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(currentUserId)
                                    .child("profileImageUrl")
                                    .setValue(newImageUrl);
                            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show());
    }
}
