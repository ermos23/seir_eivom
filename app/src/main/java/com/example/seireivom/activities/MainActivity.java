package com.example.seireivom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
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
    //FirebaseFirestore db = FirebaseFirestore.getInstance();
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        movieListViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        movieListViewModel.searchMoviePop(1);


        NavController navController = Navigation.findNavController(this, R.id.fragment);
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(navigationView, navController);

    }
}
