package com.example.imdbcito.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imdbcito.ui.detail.MovieDetailActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Para ejecutar movieDetailActivity para pruebas
        val intent = Intent(this, MovieDetailActivity::class.java).apply {
            putExtra("MOVIE_ID", 640146)
        }
        startActivity(intent)
    }
}