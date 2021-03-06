package cosapp.com.nostra.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cosapp.com.nostra.CityBikesProvider;
import cosapp.com.nostra.Fragments.BikeStationFragment;
import cosapp.com.nostra.Fragments.ParkingMachinesFragment;
import cosapp.com.nostra.Fragments.TicketMachinesFragment;
import cosapp.com.nostra.Fragments.TicketPointsFragment;
import cosapp.com.nostra.R;
import cosapp.com.nostra.Websites;
import cosapp.com.nostra.XMLParser;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private MenuItem lastSelectedMenuItem;
    private Class fragmentClass;
    private Fragment fragment;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nav_view);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }

        if (id == R.id.action_information) {
            if (fragmentClass == BikeStationFragment.class) {
                XMLParser xmlParser = new XMLParser(Websites.BIKE_STATIONS);
                CityBikesProvider cityBikesProvider = xmlParser.readInformationBikesProvider();
                buildInfoDialog(cityBikesProvider);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(final MenuItem menuItem) {
        fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_bikes:
                fragmentClass = BikeStationFragment.class;
                Log.d("switch-case", "id=nav_bikes");
                break;

            case R.id.nav_ticket_machines:
                fragmentClass = TicketMachinesFragment.class;
                Log.d("switch-case", "id=nav_settings");
                break;

            case R.id.nav_parking_machines:
                fragmentClass = ParkingMachinesFragment.class;
                Log.d("Switch-case", "id=nav_parking-machines");
                break;

            case R.id.nav_ticket_points:
                fragmentClass = TicketPointsFragment.class;
                Log.d("Switch-case", "id=nav_ticket_points");
                break;

            default:
                fragmentClass = TicketMachinesFragment.class;
                break;
        }
        mDrawer.closeDrawers();

        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            //wait for drawer to be closed then continute with loading fragment
            @Override
            public void onDrawerClosed(View drawerView) {
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.mapContent, fragment).commit();

                if (lastSelectedMenuItem != null) {
                    lastSelectedMenuItem.setChecked(false);
                }
                menuItem.setChecked(true);
                lastSelectedMenuItem = menuItem;

                setTitle(menuItem.getTitle());

            }

            @Override
            public void onDrawerStateChanged(int newState) {}
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(View drawerView) {}
        });
    }

    private void buildInfoDialog(final CityBikesProvider cityBikesProvider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence[] items = {
                getResources().getString(R.string.official_site),
                getResources().getString(R.string.terms),
                getResources().getString(R.string.hotline)
        };


        builder.setSingleChoiceItems(
                items,
                0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == 0) {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(cityBikesProvider.getWebsite()));
                        } else if (which == 1) {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(cityBikesProvider.getTerms()));
                        } else if (which == 2) {
                            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + cityBikesProvider.getHotline()));
                        }

                    }
                });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
