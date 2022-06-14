package com.example.seireivom.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.seireivom.repository.MovieRepository;

import java.util.List;

public class WatchlistViewModel extends ViewModel {

    private MovieRepository movieRepository;


    // Constructor
    public WatchlistViewModel() {
        movieRepository = MovieRepository.getInstance();

    }

    public LiveData<List<MovieModel>> getMovies(){

        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPop(){

        return movieRepository.getPop();
    }

    public LiveData<List<MovieModel>> getWatchlist(){

        return movieRepository.getWatchlist();
    }

    public void searchNextpage(){

        movieRepository.searchNextPage();
    }


}