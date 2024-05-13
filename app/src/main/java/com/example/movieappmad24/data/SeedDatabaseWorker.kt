package com.example.movieappmad24.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.movieappmad24.models.getMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SeedDatabaseWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = MovieDatabase.getDatabase(applicationContext)
            val dao: MovieDao = database.movieDao()
            val movies = getMovies()
            dao.addAll(movies)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
