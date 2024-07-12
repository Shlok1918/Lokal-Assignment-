package com.shlokyadav.lokalassignment.Activities;

import static androidx.core.view.ViewCompat.onApplyWindowInsets;
import static androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shlokyadav.lokalassignment.Fragments.BookmarkFragment;
import com.shlokyadav.lokalassignment.Fragments.JobFragment;
import com.shlokyadav.lokalassignment.Model.NetworkUtils;
import com.shlokyadav.lokalassignment.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        frameLayout = findViewById(R.id.frame_layout);

        setOnApplyWindowInsetsListener(bottomNavigationView, (v, insets) -> {
            WindowInsetsCompat insetsCompat = onApplyWindowInsets(v, insets);
            int bottomInset = insetsCompat.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            v.setPadding(0, 0, 0, bottomInset);
            return insetsCompat;
        });

        if (savedInstanceState == null) {
            currentFragment = new JobFragment();
            loadFragment(currentFragment);
        }

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            handleNavigation(itemId);
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.jobs_nav_btn);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showNoInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please turn on your internet connection to continue.")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadFragment(new BookmarkFragment());
                    }
                })
                .show();
    }

    private void handleNavigation(int itemId) {
        Fragment selectedFragment = null;

        if (itemId == R.id.jobs_nav_btn) {
            if (NetworkUtils.isConnectedToInternet(this) ){
                selectedFragment = new JobFragment();
            } else {
                showNoInternetDialog();
                return;
            }
        } else if (itemId == R.id.bookmark_nav_btn) {
            selectedFragment = new BookmarkFragment();
        }

        if (selectedFragment != null && (currentFragment == null || !selectedFragment.getClass().equals(currentFragment.getClass()))) {
            currentFragment = selectedFragment;
            loadFragment(currentFragment);
        }
    }
}
