package com.example.chatwave;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment {

    private RecyclerView callsRecyclerView;
    private CallAdapter callAdapter;
    private List<CallLog> callLogs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate your fragment_calls.xml layout here (make sure this layout exists)
        return inflater.inflate(R.layout.fragment_calls, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callsRecyclerView = view.findViewById(R.id.callsRecyclerView); // RecyclerView id in fragment_calls.xml
        callsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        callLogs = generateDummyCalls();
        callAdapter = new CallAdapter(callLogs);

        callsRecyclerView.setAdapter(callAdapter);
    }

    private List<CallLog> generateDummyCalls() {
        List<CallLog> calls = new ArrayList<>();

        // Replace R.drawable.ic_user_placeholder with your profile image drawable
        int placeholder = R.drawable.ic_user_placeholder;

        // Example Muslim names (mix of incoming and outgoing)
        calls.add(new CallLog("Ahmed Ali", "Today, 10:15 AM", true, placeholder));
        calls.add(new CallLog("Fatima Khan", "Yesterday, 9:30 PM", false, placeholder));
        calls.add(new CallLog("Hassan Malik", "Today, 8:05 AM", true, placeholder));
        calls.add(new CallLog("Ayesha Siddiqui", "Yesterday, 7:45 PM", false, placeholder));
        calls.add(new CallLog("Zain Abbas", "Today, 6:20 AM", true, placeholder));
        calls.add(new CallLog("Sara Tariq", "Yesterday, 5:10 PM", false, placeholder));
        calls.add(new CallLog("Bilal Shah", "Today, 4:00 PM", true, placeholder));
        calls.add(new CallLog("Nadia Mirza", "Yesterday, 3:30 PM", false, placeholder));
        calls.add(new CallLog("Omar Farooq", "Today, 2:45 PM", true, placeholder));
        calls.add(new CallLog("Sana Riaz", "Yesterday, 1:15 PM", false, placeholder));
        calls.add(new CallLog("Yusuf Iqbal", "Today, 12:00 PM", true, placeholder));
        calls.add(new CallLog("Layla Ahmed", "Yesterday, 11:20 AM", false, placeholder));
        calls.add(new CallLog("Imran Khan", "Today, 10:10 AM", true, placeholder));
        calls.add(new CallLog("Mariam Nasir", "Yesterday, 9:00 AM", false, placeholder));
        calls.add(new CallLog("Farhan Saeed", "Today, 8:30 AM", true, placeholder));
        calls.add(new CallLog("Zara Hussain", "Yesterday, 7:50 AM", false, placeholder));
        calls.add(new CallLog("Noman Shahid", "Today, 6:40 AM", true, placeholder));
        calls.add(new CallLog("Amna Bashir", "Yesterday, 5:55 AM", false, placeholder));
        calls.add(new CallLog("Raza Ahmed", "Today, 4:30 AM", true, placeholder));
        calls.add(new CallLog("Hina Qureshi", "Yesterday, 3:20 AM", false, placeholder));

        return calls;
    }
}
