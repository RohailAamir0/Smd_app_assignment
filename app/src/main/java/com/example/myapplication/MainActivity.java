package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.util.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toast with tag "CineFAST" on launch
        Toast toast = Toast.makeText(this, "Welcome to CineFAST!", Toast.LENGTH_SHORT);
        toast.setTag("CineFAST");
        toast.show();

        sessionManager = new SessionManager(this);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("CineFAST");
        }

        // Setup DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup NavigationView
        NavigationView navigationView = findViewById(R.id.navigationView);

        // Set user email in nav header
        TextView navEmailView = navigationView.getHeaderView(0).findViewById(R.id.navHeaderEmail);
        String email = sessionManager.getUserEmail();
        if (navEmailView != null && email != null) {
            navEmailView.setText(email);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_my_bookings) {
                loadFragment(new MyBookingsFragment());
            } else if (id == R.id.nav_logout) {
                logout();
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void logout() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}
