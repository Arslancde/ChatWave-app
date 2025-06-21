package com.example.chatwave;

import android.graphics.Color;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {

    private final List<CallLog> callList;

    public CallAdapter(List<CallLog> callList) {
        this.callList = callList;
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        CallLog call = callList.get(position);

        holder.name.setText(call.getName());
        holder.time.setText(call.getTime());
        holder.profile.setImageResource(call.getProfileResId());

        if (call.isIncoming()) {
            holder.callIcon.setImageResource(R.drawable.ic_call_received); // Use your own icon
            holder.callIcon.setColorFilter(Color.GREEN);
        } else {
            holder.callIcon.setImageResource(R.drawable.ic_call_made); // Use your own icon
            holder.callIcon.setColorFilter(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return callList.size();
    }

    static class CallViewHolder extends RecyclerView.ViewHolder {
        ImageView profile, callIcon;
        TextView name, time;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.caller_profile_image);
            name = itemView.findViewById(R.id.caller_name);
            time = itemView.findViewById(R.id.call_time);
            callIcon = itemView.findViewById(R.id.call_type_icon);
        }
    }
}
