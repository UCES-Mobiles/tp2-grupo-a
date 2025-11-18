package com.example.imdbcito.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.imdbcito.data.models.entities.movie.FavoriteMovieModel

class FavoriteMovieDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "favorites.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_FAVORITES = "favorites"
        const val COLUMN_ID = "id"
        const val COLUMN_MOVIE_ID = "movie_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_POSTER_PATH = "poster_path"
        const val COLUMN_WATCHED = "watched"

        private const val CREATE_TABLE_FAVORITES = """
           CREATE TABLE $TABLE_FAVORITES (
               $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
               $COLUMN_MOVIE_ID INTEGER NOT NULL,
               $COLUMN_TITLE TEXT NOT NULL,
               $COLUMN_POSTER_PATH TEXT,
               $COLUMN_WATCHED INTEGER DEFAULT 0
           )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_FAVORITES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_FAVORITES ADD COLUMN $COLUMN_POSTER_PATH TEXT")
            db.execSQL("ALTER TABLE $TABLE_FAVORITES ADD COLUMN $COLUMN_WATCHED INTEGER DEFAULT 0")
        }
    }

    fun insertMovie(movie: FavoriteMovieModel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MOVIE_ID, movie.movieId)
            put(COLUMN_TITLE, movie.title)
            put(COLUMN_POSTER_PATH, movie.posterPath)
            put(COLUMN_WATCHED, if (movie.watched) 1 else 0)
        }
        return db.insert(TABLE_FAVORITES, null, values)
    }

    fun deleteMovie(id: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_FAVORITES,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun updateWatchedStatus(id: Long, watched: Boolean): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_WATCHED, if (watched) 1 else 0)
        }
        return db.update(
            TABLE_FAVORITES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getAllMovies(): List<FavoriteMovieModel> {
        val movies = mutableListOf<FavoriteMovieModel>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_FAVORITES,
            arrayOf(COLUMN_ID, COLUMN_MOVIE_ID, COLUMN_TITLE, COLUMN_POSTER_PATH, COLUMN_WATCHED),
            null, null, null, null,
            "$COLUMN_TITLE ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val movieId = it.getInt(it.getColumnIndexOrThrow(COLUMN_MOVIE_ID))
                val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                val posterPath = it.getString(it.getColumnIndexOrThrow(COLUMN_POSTER_PATH))
                val watched = it.getInt(it.getColumnIndexOrThrow(COLUMN_WATCHED)) == 1
                movies.add(FavoriteMovieModel(id, movieId, title, posterPath, watched))
            }
        }

        return movies
    }

    fun isFavorite(movieId: Int): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_FAVORITES,
            arrayOf(COLUMN_ID),
            "$COLUMN_MOVIE_ID = ?",
            arrayOf(movieId.toString()),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}