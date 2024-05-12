package com.example.movieappmad24.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.MovieWithImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movie = MutableStateFlow<MovieWithImages?>(null)
    val movie: StateFlow<MovieWithImages?> = _movie.asStateFlow()

    fun getMovieById(id: Long) {
        viewModelScope.launch {
            repository.getById(id).distinctUntilChanged().collect { movie ->
                _movie.value = movie
            }
        }
    }

    fun toggleFavoriteMovie() {
        val movie = _movie.value?.movie ?: return
        viewModelScope.launch {
            val updatedMovie = movie.copy(isFavorite = !movie.isFavorite)
            repository.updateMovie(updatedMovie)
        }
    }
}
