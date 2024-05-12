package com.example.movieappmad24.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.MovieWithImages
import com.example.movieappmad24.viewmodels.DetailViewModel
import com.example.movieappmad24.widgets.HorizontalScrollableImageView
import com.example.movieappmad24.widgets.MovieRow
import com.example.movieappmad24.widgets.SimpleTopAppBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

@Composable
fun DetailScreen(
    movieId: String?,
    navController: NavController,
    viewModel: DetailViewModel // Injecting DetailViewModel
) {

    // Observe movie state from viewModel
    val movieWithImages by viewModel.movie.collectAsState()

    movieWithImages?.let { movieWithImages ->
        val movie = movieWithImages.movie
        Scaffold(
            topBar = {
                SimpleTopAppBar(title = movie.title) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column {
                MovieRow(
                    modifier = Modifier.padding(innerPadding),
                    movie = movie,
                    onFavoriteClick = { viewModel.toggleFavoriteMovie() }
                )

                Divider(modifier = Modifier.padding(4.dp))

                Column {
                    Text("Movie Trailer")
                    VideoPlayer(trailerURL = movie.trailer)
                }

                Divider(modifier = Modifier.padding(4.dp))

                HorizontalScrollableImageView(movie = movie)
            }
        }
    }

    // Fetch movie when movieId changes
    LaunchedEffect(movieId) {
        if (movieId != null) {
            viewModel.getMovieById(movieId.toLong())
        }
    }
}

private operator fun <T> Flow<T>.get(i: Int) {

}

@Composable
fun VideoPlayer(trailerURL: String) {

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(
                MediaItem.fromUri(
                    "android.resource://${context.packageName}/${context.resources.getIdentifier(
                        trailerURL,
                        "raw",
                        context.packageName
                    )}"
                )
            )
            prepare()
            playWhenReady = true
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            exoPlayer.release()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f),
        factory = {
            PlayerView(context).also { playerView ->
                playerView.player = exoPlayer
            }
        },
        update = {
            when (lifecycle) {
                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                    //it.player?.play()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }
                else -> Unit
            }
        }
    )
}
