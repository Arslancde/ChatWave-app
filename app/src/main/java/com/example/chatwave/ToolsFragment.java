package com.example.chatwave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToolsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ToolsAdapter toolsAdapter;
    private List<String> toolsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.toolsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        toolsList = new ArrayList<>();
        // Add your tools here:
        toolsList.add("Conversation Started");
        toolsList.add("Catalog Views");
        toolsList.add("Status Views");
        toolsList.add("Grow Your Business");
        toolsList.add("Catalogue");
        toolsList.add("Advertise");
        toolsList.add("Marketing Messages");
        toolsList.add("Organize Your Chats");
        toolsList.add("Labels");
        toolsList.add("Greeting Message");
        toolsList.add("Away Message");
        toolsList.add("Quick Reply");
        toolsList.add("Manage Your Account");
        toolsList.add("Profile");
        toolsList.add("Facebook and Instagram");
        toolsList.add("Help Center");
        toolsList.add("Add a Catalog");

        toolsAdapter = new ToolsAdapter(toolsList, toolName -> {
            Toast.makeText(getContext(), "Clicked: " + toolName, Toast.LENGTH_SHORT).show();

            // Add your click handling here for each tool if needed
            // e.g. navigate to fragment/activity for specific tool
        });

        recyclerView.setAdapter(toolsAdapter);
    }
}
