package com.example.movieappmad24.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.MovieWithImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movies = MutableStateFlow(listOf<MovieWithImages>())
    val movies = _movies.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.getAllMovies().distinctUntilChanged()
                .collect { listOfMovies ->
                    _movies.value = listOfMovies
                    Log.d("HomeViewModel", "Movies: $listOfMovies")
                }
        }
    }

    fun toggleFavoriteMovie(movieId: String) {
        viewModelScope.launch {
            val movieWithImages = repository.getById(movieId.toLong()).firstOrNull()
            movieWithImages?.let {
                val movie = it.movie
                movie.isFavorite = !movie.isFavorite
                repository.updateMovie(movie)
            }
        }
    }
}

