package com.example.crisol_Jaen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.crisol_Jaen.fragments.AccountFragment;
import com.example.crisol_Jaen.fragments.FavoriteFragment;
import com.example.crisol_Jaen.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_menu, new HomeFragment()).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.favorite:
                        selectedFragment = new FavoriteFragment();
                        break;
                    case R.id.account:
                        selectedFragment = new AccountFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_menu, selectedFragment).commit();

                return true;
            }
        });
    }
}
