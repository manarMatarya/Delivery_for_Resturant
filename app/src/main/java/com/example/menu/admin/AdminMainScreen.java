package com.example.menu.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.menu.R;
import com.example.menu.activities.MainScreen;
import com.example.menu.activities.Registeration;
import com.example.menu.fragments.Cart;
import com.example.menu.fragments.Favourite;
import com.example.menu.fragments.about;
import com.example.menu.fragments.main_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainScreen extends AppCompatActivity {
    Fragment selectedFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);

        selectedFragment = new AdminMainFragment();
        loadCalFragment(selectedFragment);

        BottomNavigationView bottomNavigation = findViewById(R.id.admin_bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(listener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.food:
                    selectedFragment = new AdminMainFragment();
                    break;
                case R.id.reservation:
                    selectedFragment = new AdminReservationFragment();
                    break;
                case R.id.adminmore:
                    PopupMenu pm = new PopupMenu(getApplicationContext(), findViewById(R.id.adminmore));
                    pm.getMenuInflater().inflate(R.menu.admin_popup_menu, pm.getMenu());
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.admin_logout) {
                                FirebaseAuth.getInstance().signOut();
                                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                                        "file", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.clear();

                                Toast.makeText(AdminMainScreen.this, "logout succecfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AdminMainScreen.this, Registeration.class));
                            }
                            return true;
                        }
                    });
                    pm.show();

            }
            loadCalFragment(selectedFragment);
            return true;
        }
    };

    public void loadCalFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.admin_main_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}