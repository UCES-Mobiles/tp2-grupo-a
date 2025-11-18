package com.example.imdbcito.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.imdbcito.R
import com.example.imdbcito.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cargar HomeFragment en el FrameLayout
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }
    }
}