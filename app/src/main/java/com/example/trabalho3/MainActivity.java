package com.example.trabalho3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.Menu;

import com.example.trabalho3.Notification.ServiceNotification;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import com.example.trabalho3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //INICIA O SERVIÇO PRA VERIFICAR OS AGENDAMOS
        Intent serviceIntent = new Intent(this, ServiceNotification.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            // Antes do Android 8.0, use startService()
            startService(serviceIntent);
        }
        

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavigationView navigationView = findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();

                int checkedItemId = menu.findItem(navigationView.getCheckedItem().getItemId()).getItemId();

                if (checkedItemId == R.id.nav_home) {
                    // Item 1 do Navigation Drawer está ativo
                    Snackbar.make(view, "Ação do Item 1", Snackbar.LENGTH_LONG).show();
                } else if (checkedItemId == R.id.nav_gallery) {
                    // Item 2 do Navigation Drawer está ativo
                    Snackbar.make(view, "Ação do Item 2", Snackbar.LENGTH_LONG).show();
                } else if (checkedItemId == R.id.nav_slideshow) {
                    Intent intent = new Intent(MainActivity.this, CategoriaActivity.class);
                    startActivity(intent);
                } else {
                    // Nenhum item do Navigation Drawer está ativo
                    Snackbar.make(view, "Nenhum item ativo", Snackbar.LENGTH_LONG).show();
                }


            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}