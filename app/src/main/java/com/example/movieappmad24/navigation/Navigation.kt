package com.example.movieappmad24.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieappmad24.screens.DetailScreen
import com.example.movieappmad24.screens.HomeScreen
import com.example.movieappmad24.screens.WatchlistScreen
import com.example.movieappmad24.viewmodels.DetailViewModel
import com.example.movieappmad24.viewmodels.HomeViewModel
import com.example.movieappmad24.viewmodels.WatchlistViewModel

@Composable
fun Navigation(
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    watchlistViewModel: WatchlistViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController, viewModel = homeViewModel)
        }
        composable(Screen.DetailScreen.route + "/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            DetailScreen(movieId, navController, viewModel = detailViewModel)
        }
        composable(Screen.WatchlistScreen.route) {
            WatchlistScreen(navController = navController, viewModel = watchlistViewModel)
        }
    }
}
