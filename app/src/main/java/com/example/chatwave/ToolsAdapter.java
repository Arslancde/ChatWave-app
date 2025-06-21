package com.example.chatwave;

import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ToolViewHolder> {

    private final List<String> toolsList;
    private final OnToolClickListener listener;

    public interface OnToolClickListener {
        void onToolClick(String toolName);
    }

    public ToolsAdapter(List<String> toolsList, OnToolClickListener listener) {
        this.toolsList = toolsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tool, parent, false);
        return new ToolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolViewHolder holder, int position) {
        String toolName = toolsList.get(position);
        holder.textView.setText(toolName);

        // Set icon based on tool name
        switch (toolName) {
            case "Conversation Started":
                holder.icon.setImageResource(R.drawable.ic_conversation_started);
                break;
            case "Catalog Views":
                holder.icon.setImageResource(R.drawable.ic_catalog_views);
                break;
            case "Status Views":
                holder.icon.setImageResource(R.drawable.ic_status_views);
                break;
            case "Grow Your Business":
                holder.icon.setImageResource(R.drawable.ic_grow_business);
                break;
            case "Catalogue":
                holder.icon.setImageResource(R.drawable.ic_catalogue);
                break;
            case "Advertise":
                holder.icon.setImageResource(R.drawable.ic_advertise);
                break;
            default:
                holder.icon.setImageDrawable(null); // No icon
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onToolClick(toolName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toolsList.size();
    }

    static class ToolViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView icon;

        public ToolViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.toolTitle);
            icon = itemView.findViewById(R.id.toolIcon);
        }
    }
}
