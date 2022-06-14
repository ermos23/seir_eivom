package com.example.seireivom.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.seireivom.repository.MovieRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MovieRepository movieRepository;


    // Constructor
    public HomeViewModel() {
        movieRepository = MovieRepository.getInstance();

    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPop(){
        return movieRepository.getPop();
    }



    // 3- Calling method in view-model
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.serachMovieApi(query, pageNumber);
    }

    // 3- Calling method in view-model
    public void searchMoviePop(int pageNumber){
        movieRepository.serachMoviePop( pageNumber);
    }



    public void searchNextpage(){
        movieRepository.searchNextPage();
    }


}