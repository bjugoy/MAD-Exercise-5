package com.example.movieappmad24.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.models.MovieWithImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class WatchlistViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _favoriteMovies = MutableStateFlow<List<MovieWithImages>>(emptyList())
    val favoriteMovies: StateFlow<List<MovieWithImages>> = _favoriteMovies.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavoriteMovies().distinctUntilChanged()
                .collect { listOfMovies ->
                    _favoriteMovies.value = listOfMovies
                }
        }
    }

    fun toggleFavoriteMovie(id: Long) {
        viewModelScope.launch {
            val movieWithImages = _favoriteMovies.value.find { it.movie.dbId == id } ?: return@launch
            val movie = movieWithImages.movie
            movie.isFavorite = !movie.isFavorite
            repository.updateMovie(movie)
        }
    }
}
