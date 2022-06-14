package com.example.seireivom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seireivom.MovieDetails;
import com.example.seireivom.R;
import com.example.seireivom.adapters.MovieRecyclerView;
import com.example.seireivom.adapters.OnMovieListener;
import com.example.seireivom.model.MovieModel;
import com.example.seireivom.model.WatchlistViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WatchlistActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;

    // ViewModel
    private WatchlistViewModel movieListViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String client;
    CollectionReference watchlistRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications);
        recyclerView = findViewById(R.id.recyclerWatchlist);
        movieListViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        client = getString(R.string.facebook_client_token);
        watchlistRef = db.collection("users").document(client).collection("watchlist");
        watchlistRef.orderBy("title", Query.Direction.ASCENDING);
        watchlistRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                if (document.exists()) {
                                    List<String> list = new ArrayList<>();

                                    Map<String, Object> map = document.getData();
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        list.add(entry.getValue().toString());
                                        Log.d("TAG", String.valueOf(list));

                                    }
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());

                        }
                    }
                });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment finalHost = NavHostFragment.create(R.navigation.mobile_navigation);



    }
    private void ObserveWatchlist(){
        movieListViewModel.getWatchlist().observe(this, new Observer<List<MovieModel>>() {
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
}