package com.example.seireivom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.RequestManager;
import com.example.seireivom.R;
import com.example.seireivom.adapters.MovieRecyclerView;
import com.example.seireivom.databinding.ActivityMainBinding;
import com.example.seireivom.model.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;

    // ViewModel
    private HomeViewModel movieListViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = findViewById(R.id.recyclerViewMovies);
        movieListViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        movieListViewModel.searchMoviePop(1);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment finalHost = NavHostFragment.create(R.navigation.mobile_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        binding.recyclerViewMovies.setAlpha(0);
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.bottomNavigationView, finalHost)
                                .setPrimaryNavigationFragment(finalHost) // equivalent to app:defaultNavHost="true"
                                .commit();
                        binding.recyclerViewMovies.setAlpha(1);
                        return true;
                    case R.id.navigation_notifications:
                        binding.recyclerViewMovies.setAlpha(0);
                        startActivity(new Intent(getApplicationContext(), WatchlistActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);}