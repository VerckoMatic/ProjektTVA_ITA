package View.Activitys;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.matic.projekttva.R;

import View.Activitys.Fragments.HomeFragment;
import View.Activitys.Fragments.ProfilFragment;
import rx.subscriptions.CompositeSubscription;
import utils.Constants;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;

    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mSubscriptions = new CompositeSubscription();
        contextOfApplication = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            //super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfilFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_user_data:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfilFragment()).commit();
                break;
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfilFragment()).commit();
                break;
            case R.id.nav_logout:
                mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String mToken = mSharedPreferences.getString(Constants.TOKEN,"");
                String mEmail = mSharedPreferences.getString(Constants.EMAIL,"");
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.EMAIL,"");
                editor.putString(Constants.TOKEN,"");
                editor.apply();
                finish();
                break;
        }
        return true;
    }
}
