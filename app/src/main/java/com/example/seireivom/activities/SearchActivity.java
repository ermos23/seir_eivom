package com.example.seireivom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seir_eivom.MovieDetails;
import com.example.seir_eivom.R;
import com.example.seir_eivom.adapters.MovieRecyclerView;
import com.example.seir_eivom.adapters.OnMovieListener;
import com.example.seir_eivom.models.MovieModel;
import com.example.seir_eivom.ui.dashboard.SearchViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements OnMovieListener {

    // RecyclerView
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;

    // ViewModel
    private SearchViewModel movieListViewModel;

    boolean isPopular = true;   // True for popular

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);

        // SearchView
        SetupSearchView();

        recyclerView = findViewById(R.id.recyclerView);
        movieListViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        ConfigureRecyclerView();
        ObserveAnyChange();
        ObservePopular();
        movieListViewModel.searchMoviePop(1);

        //Log.v("Tagy", "ispop: " + isPopular);
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
                        //binding.recyclerViewMovies.setAlpha(0);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.bottomNavigationView, finalHost)
                                .setPrimaryNavigationFragment(finalHost) // equivalent to app:defaultNavHost="true"
                                .commit();
                        return true;
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        //binding.recyclerViewMovies.setAlpha(1);
                        return true;
                    case R.id.navigation_notifications:
                        //binding.recyclerViewMovies.setAlpha(0);
                        startActivity(new Intent(getApplicationContext(), WatchlistActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void ObservePopular(){
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observing for any data change
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){
                        // Get the data in log
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }

            }
        });

    }


    // Observing any data change
    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observing for any data change
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){
                        // Get the data in log
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }

            }
        });

    }

    // 5- Intializing recyclerView & adding data to it
    private void ConfigureRecyclerView() {
        movieRecyclerAdapter = new MovieRecyclerView( this);
        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        // RecyclerView Pagination
        // Loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    // Here we need to display the next search results on the next page of api
                    movieListViewModel.searchNextpage();

                }
            }
        });

    }

    @Override
    public void onMovieClick(int position) {
        // Toast.makeText(this, "The Position "  +position, Toast.LENGTH_SHORT).show();

        // We don't need position of the movie in recyclerview
        // WE NEED THE ID OF THE MOVIE IN ORDER TO GET ALL IT"S DETAILS
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {

    }

    // Get data from searchview & query the api to get the results (Movies)
    private void SetupSearchView() {
        final SearchView searchView = findViewById(R.id.toolbar);
        // Detect Search
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want when search view expended
                isPopular = false;
                Log.v("Tagy", "ispop: " +isPopular);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //do what you want  searchview is not expanded
                return false;
            }
        });

        // Make search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        // The search string getted from searchview
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

}