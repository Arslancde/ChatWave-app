package com.example.chatwave;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bottom Navigation setup
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_chats) {
                selectedFragment = new UserListFragment();
            } else if (itemId == R.id.nav_calls) {
                selectedFragment = new CallsFragment();
            } else if (itemId == R.id.nav_updates) {
                selectedFragment = new UpdatesFragment();
            } else if (itemId == R.id.nav_tools) {
                selectedFragment = new ToolsFragment();  // <<<<<< Updated here
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }

            return false;
        });

        // Default screen
        if (savedInstanceState == null) {
            if (currentUser != null) {
                loadFragment(new UserListFragment());
            } else {
                loadFragment(new LoginFragment());
            }
        }
    }

    // Switch fragment
    public void loadFragment(Fragment fragment) {
        Log.d("MainActivity", "Loading fragment: " + fragment.getClass().getSimpleName());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_menu, menu);
        return true;
    }

    // Handle top bar actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_camera) {
            Toast.makeText(this, "Camera clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_search) {
            loadFragment(new ContactSearchFragment());
            return true;

        } else if (id == R.id.menu_advertise) {
            Toast.makeText(this, "Advertise clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_marketing_messages) {
            Toast.makeText(this, "Marketing Messages clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_new_group) {
            Toast.makeText(this, "New Group clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_new_broadcast) {
            Toast.makeText(this, "New Broadcast clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_communities) {
            Toast.makeText(this, "Communities clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_labels) {
            Toast.makeText(this, "Labels clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_linked_devices) {
            Toast.makeText(this, "Linked Devices clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_starred) {
            Toast.makeText(this, "Starred clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.menu_settings) {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
