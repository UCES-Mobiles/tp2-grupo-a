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
        private const val DATABASE_VERSION = 1
        const val TABLE_FAVORITES = "favorites"
        const val COLUMN_ID = "id"
        const val COLUMN_MOVIE_ID = "movie_id"
        const val COLUMN_TITLE = "title"

        private const val CREATE_TABLE_FAVORITES = """
           CREATE TABLE $TABLE_FAVORITES (
               $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
               $COLUMN_MOVIE_ID INTEGER NOT NULL,
               $COLUMN_TITLE TEXT NOT NULL
           )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_FAVORITES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        onCreate(db)
    }

    // Insertar película
    fun insertMovie(movie: FavoriteMovieModel): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MOVIE_ID, movie.movieId)
            put(COLUMN_TITLE, movie.title)
        }
        return db.insert(TABLE_FAVORITES, null, values)
    }

    // Eliminar película por ID de la base
    fun deleteMovie(id: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_FAVORITES,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    // Obtener todas las películas favoritas
    fun getAllMovies(): List<FavoriteMovieModel> {
        val movies = mutableListOf<FavoriteMovieModel>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_FAVORITES,
            arrayOf(COLUMN_ID, COLUMN_MOVIE_ID, COLUMN_TITLE),
            null, null, null, null,
            "$COLUMN_TITLE ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                val movieId = it.getInt(it.getColumnIndexOrThrow(COLUMN_MOVIE_ID))
                val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                movies.add(FavoriteMovieModel(id, movieId, title))
            }
        }

        return movies
    }

    // Buscar película por movieId
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