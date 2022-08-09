package com.example.menu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.menu.R;
import com.example.menu.admin.AdminMainScreen;
import com.example.menu.fragments.Cart;
import com.example.menu.fragments.Favourite;
import com.example.menu.fragments.about;
import com.example.menu.fragments.main_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainScreen extends AppCompatActivity {
    Fragment selectedFragment = null;
    SharedPreferences sharedPref ;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        selectedFragment = new main_fragment();
        loadCalFragment(selectedFragment);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(listener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new main_fragment();
                    break;
                case R.id.favourite:
                    selectedFragment = new Favourite();
                    break;
                case R.id.cart:
                    selectedFragment = new Cart();
                    break;
                case R.id.navmore:
                    PopupMenu pm = new PopupMenu(getApplicationContext(), findViewById(R.id.navmore));
                    pm.getMenuInflater().inflate(R.menu.popup_menu, pm.getMenu());
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.about:
                                    selectedFragment = new about();
                                    loadCalFragment(selectedFragment);
                                    break;
                                case R.id.call:
                                    dialContactPhone("0598576933");

                                    break;
                                case R.id.logout:
                                    FirebaseAuth.getInstance().signOut();
                                   SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                                            "file", Context.MODE_PRIVATE);
                                   SharedPreferences.Editor editor = sharedPref.edit();
                                   editor.clear();

                                    Toast.makeText(MainScreen.this, "logout succecfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainScreen.this, Registeration.class));
                                    break;
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
        ft.replace(R.id.main_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        fAuth = FirebaseAuth.getInstance();
//        sharedPref = this.getSharedPreferences(
//                "file", Context.MODE_PRIVATE);
//        if(fAuth.getCurrentUser()!=null){
//            if(sharedPref.getString("email", "").equals("mataryamanar@gmail.com")){
//                Intent intent = new Intent(this, AdminMainScreen.class);
//                startActivity(intent);
//            }else{
//                Intent intent = new Intent(this, MainScreen.class);
//                startActivity(intent);            }
//            finish();
//        }
//    }
}
