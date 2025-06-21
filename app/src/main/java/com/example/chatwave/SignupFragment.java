package com.example.chatwave;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText emailInput, passwordInput, confirmPasswordInput, phoneInput;
    private Button signupButton, selectImageButton;
    private TextView loginText;
    private ImageView profileImageView;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    profileImageView.setImageURI(selectedImageUri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput);
        phoneInput = view.findViewById(R.id.phoneInput); // ✅ New
        signupButton = view.findViewById(R.id.signupButton);
        loginText = view.findViewById(R.id.loginText);
        profileImageView = view.findViewById(R.id.profileImageView);
        selectImageButton = view.findViewById(R.id.selectImageButton);

        selectImageButton.setOnClickListener(v -> openGallery());

        signupButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim(); // ✅ New

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phone)) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            signupButton.setEnabled(false);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        signupButton.setEnabled(true);

                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();
                                String name = email.split("@")[0];

                                if (selectedImageUri != null) {
                                    uploadProfileImage(uid, name, email, phone);
                                } else {
                                    saveUserToDatabase(new User(uid, name, email, "", phone));
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        loginText.setOnClickListener(v -> ((MainActivity) requireActivity()).loadFragment(new LoginFragment()));
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void uploadProfileImage(String uid, String name, String email, String phone) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("profile_images/").child(uid + ".jpg");
        storageRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot ->
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveUserToDatabase(new User(uid, name, email, imageUrl, phone));
                })
        ).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
        );
    }

    private void saveUserToDatabase(User user) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUid())
                .setValue(user)
                .addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        Toast.makeText(getContext(), "Signup successful", Toast.LENGTH_SHORT).show();
                        ((MainActivity) requireActivity()).loadFragment(new HomeFragment());
                    } else {
                        Toast.makeText(getContext(), "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
