package com.example.chatwave;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdatesFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_CHANNEL_IMAGE_REQUEST = 2;

    private Uri selectedChannelImageUri;
    private ImageView dialogChannelImagePreview = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_updates, container, false);

        // ✅ My Status Click
        LinearLayout myStatusContainer = rootView.findViewById(R.id.my_status_container);
        myStatusContainer.setOnClickListener(v -> openGallery());

        // ✅ Populate dynamic channels
        LinearLayout channelsContainer = rootView.findViewById(R.id.channels_container);
        addChannelCards(channelsContainer);

        // ✅ FAB to create new channel
        FloatingActionButton fab = rootView.findViewById(R.id.fab_add_channel);
        fab.setOnClickListener(v -> showCreateChannelDialog());

        return rootView;
    }

    // ✅ Open gallery for status
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // ✅ Handle image results
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            if (requestCode == PICK_IMAGE_REQUEST) {
                Toast.makeText(getContext(), "Status image selected: " + selectedImageUri, Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_CHANNEL_IMAGE_REQUEST && dialogChannelImagePreview != null) {
                dialogChannelImagePreview.setImageURI(selectedImageUri);
                selectedChannelImageUri = selectedImageUri;
                Toast.makeText(getContext(), "Channel image selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ✅ Add predefined channels
    private void addChannelCards(LinearLayout container) {
        String[] channelNames = {
                "OpenAI", "WhatsApp News", "TechCrunch", "Android Developers",
                "NASA", "Spotify", "Netflix", "Google", "Meta", "Instagram",
                "YouTube Creators", "GitHub", "Snapchat", "BBC News", "CNN",
                "Bloomberg", "TED", "Quora", "Reddit", "Twitch"
        };

        Drawable placeholder = ContextCompat.getDrawable(requireContext(), R.drawable.ic_channel_placeholder);

        for (String name : channelNames) {
            addSingleChannelView(container, placeholder, name);
        }
    }

    // ✅ Utility to create channel view
    private void addSingleChannelView(LinearLayout container, Drawable image, String name) {
        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setPadding(16, 16, 16, 16);
        card.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        card.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

        ImageView icon = new ImageView(requireContext());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(80, 80);
        iconParams.setMarginEnd(24);
        icon.setLayoutParams(iconParams);
        icon.setImageDrawable(image);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        icon.setBackgroundResource(R.drawable.circular_background);
        icon.setPadding(4, 4, 4, 4);

        TextView nameView = new TextView(requireContext());
        nameView.setText(name);
        nameView.setTextSize(16);
        nameView.setTextColor(getResources().getColor(android.R.color.black));
        nameView.setGravity(Gravity.CENTER_VERTICAL);

        card.addView(icon);
        card.addView(nameView);
        container.addView(card);
    }

    // ✅ Show channel creation dialog
    private void showCreateChannelDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_channel, null);

        dialogChannelImagePreview = dialogView.findViewById(R.id.channel_image_preview);
        EditText nameInput = dialogView.findViewById(R.id.channel_name_input);
        Button createButton = dialogView.findViewById(R.id.create_channel_button);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        // Pick image
        dialogChannelImagePreview.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_CHANNEL_IMAGE_REQUEST);
        });

        // Create channel
        createButton.setOnClickListener(v -> {
            String channelName = nameInput.getText().toString().trim();
            if (channelName.isEmpty()) {
                nameInput.setError("Enter channel name");
                return;
            }

            Drawable image;
            if (selectedChannelImageUri != null) {
                image = Drawable.createFromPath(selectedChannelImageUri.getPath());
                if (image == null) {
                    image = ContextCompat.getDrawable(requireContext(), R.drawable.ic_channel_placeholder);
                }
            } else {
                image = ContextCompat.getDrawable(requireContext(), R.drawable.ic_channel_placeholder);
            }

            LinearLayout container = requireView().findViewById(R.id.channels_container);
            addSingleChannelView(container, image, channelName);
            Toast.makeText(getContext(), "Channel Created: " + channelName, Toast.LENGTH_SHORT).show();
            selectedChannelImageUri = null;
            dialogChannelImagePreview = null;
            dialog.dismiss();
        });

        dialog.show();
    }
}
