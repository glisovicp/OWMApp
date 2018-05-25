package rs.gecko.petar.owmapp.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rs.gecko.petar.owmapp.R;
import rs.gecko.petar.owmapp.fragments.AddLocationFragment;
import rs.gecko.petar.owmapp.fragments.HelpFragment;
import rs.gecko.petar.owmapp.fragments.SavedLocationsFragment;
import rs.gecko.petar.owmapp.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private static final int REQUEST_PERMISSIONS = 101;

    FloatingActionButton fab;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = findViewById(android.R.id.content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set initially saved locations fragment
        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = SavedLocationsFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.maincontainer, fragment).commit();

        }

        // show map fab button only on list of saved locations screen

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                replaceFragment(AddLocationFragment.class);
                fab.setVisibility(View.GONE);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // require location permission
        if (Build.VERSION.SDK_INT >= 23) {
            requestMultiplePermissions();
        }
    }

    private void replaceFragment(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.maincontainer, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Class fragmentClass = null;
        if (id == R.id.nav_mylocations) {
            //open list with my locations
            fragmentClass = SavedLocationsFragment.class;
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_settings) {
            //open settings screen
            fragmentClass = SettingsFragment.class;
            fab.setVisibility(View.GONE);
        } else if (id == R.id.nav_help) {
            //open help screen
            fragmentClass = HelpFragment.class;
            fab.setVisibility(View.GONE);
        }

        replaceFragment(fragmentClass);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(23)
    private void requestMultiplePermissions() {
        String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        int hasLocationPermission = checkSelfPermission(locationPermission);

        List<String> permissions = new ArrayList<String>();
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            permissions.add(locationPermission);
        }

        if (!permissions.isEmpty()) {
            try {
                String[] params = permissions.toArray(new String[permissions.size()]);
                requestPermissions(params, REQUEST_PERMISSIONS);
            }
            catch (ActivityNotFoundException e) {
                Snackbar.make(parentLayout, this.getString(R.string.no_permision_packager_message), Snackbar.LENGTH_LONG)
                        .show();
            }
        } else {
            // We already have permission, so handle as normal

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:

                boolean result = true;
                for (int mPermission : grantResults) {
                    if (mPermission != PackageManager.PERMISSION_GRANTED) result = false;
                }

                if (result) {
                    // Handle permission granted

                } else {
                    // Handle permission denied
                }

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
