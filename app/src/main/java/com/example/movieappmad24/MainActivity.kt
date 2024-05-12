package com.example.movieappmad24

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movieappmad24.data.MovieDatabase
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.navigation.Navigation
import com.example.movieappmad24.ui.theme.MovieAppMAD24Theme
import com.example.movieappmad24.viewmodels.DetailViewModel
import com.example.movieappmad24.viewmodels.HomeViewModel
import com.example.movieappmad24.viewmodels.ViewModelFactory
import com.example.movieappmad24.viewmodels.WatchlistViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val movieDao = MovieDatabase.getDatabase(LocalContext.current).movieDao()
            val movieRepository = MovieRepository(movieDao)
            val viewModelFactory = ViewModelFactory(movieRepository)

            val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
            val detailViewModel: DetailViewModel by viewModels { viewModelFactory }
            val watchlistViewModel: WatchlistViewModel by viewModels { viewModelFactory }

            Navigation(
                homeViewModel = homeViewModel,
                detailViewModel = detailViewModel,
                watchlistViewModel = watchlistViewModel
            )
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "onStart called.")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "onResume called.")
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "onPause called.")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "onStop called.")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("MainActivity", "onRestart called.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MainActivity", "onDestroy called.")
    }
}




