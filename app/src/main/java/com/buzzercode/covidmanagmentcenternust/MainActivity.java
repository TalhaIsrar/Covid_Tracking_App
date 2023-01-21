package com.buzzercode.covidmanagmentcenternust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.buzzercode.covidmanagmentcenternust.LoginActivity.CMS_NAME;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.COVID_REG;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.IMAGE;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SHARED_PREFS;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.SIGNED_IN;
import static com.buzzercode.covidmanagmentcenternust.LoginActivity.TYPE;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String cms_id = sharedPreferences.getString(CMS_NAME, null);
        final String image = sharedPreferences.getString(IMAGE, null);
        final String covid_reg = sharedPreferences.getString(COVID_REG, null);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_name);
        ImageView navImage = (ImageView) headerView.findViewById(R.id.nav_image);

        Menu nav_Menu = navigationView.getMenu();
        if (covid_reg.equals("no")) {
            nav_Menu.findItem(R.id.register_covid).setVisible(true);
            nav_Menu.findItem(R.id.covid_profile).setVisible(false);
        } else {
            nav_Menu.findItem(R.id.register_covid).setVisible(false);
            nav_Menu.findItem(R.id.covid_profile).setVisible(true);
        }

        navUsername.setText(cms_id);
        Glide.with(this.getApplicationContext())
                .load(image)
                .into(navImage);

        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);

        Fragment fragment = null;
        fragment = new HomeFragment();
        loadFragment(fragment);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                Fragment fragment = null;
                switch (id) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.register_covid:
                        Intent intent1 = new Intent(MainActivity.this, RegistercovidActivity.class);
                        MainActivity.this.startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;

                    case R.id.covid_profile:
                        fragment = new CovidprofileFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.message:
                        fragment = new MessageFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.profile:
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.edit_rofile:
                        fragment = new EditprofileFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.logout:
                        editor.putBoolean(SIGNED_IN, false);
                        editor.putString(CMS_NAME, null);
                        editor.putString(TYPE, null);
                        editor.putString(IMAGE, null);
                        editor.putString(COVID_REG, null);
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        finish();
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

}