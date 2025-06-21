package com.example.chatwave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.chatwave.User;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private final Context context;
    private final List<User> userList;
    private final OnContactClickListener listener;

    public interface OnContactClickListener {
        void onContactClick(User user);
    }

    public ContactAdapter(Context context, List<User> userList, OnContactClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User user = userList.get(position);
        holder.nameText.setText(user.getName());

        // ✅ Load profile image or fallback to placeholder
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
            Glide.with(context).load(user.getProfileImageUrl()).into(holder.profileImage);
        } else {
            holder.profileImage.setImageResource(R.drawable.ic_user_placeholder);
        }

        holder.itemView.setOnClickListener(v -> listener.onContactClick(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameText;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.contact_profile_image);
            nameText = itemView.findViewById(R.id.contact_name);
        }
    }
}
