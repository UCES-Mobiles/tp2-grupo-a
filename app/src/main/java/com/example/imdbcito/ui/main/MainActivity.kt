package com.example.imdbcito.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.imdbcito.R
import com.example.imdbcito.databinding.ActivityMainBinding
import com.example.imdbcito.ui.favorites.FavoritesFragment
import com.example.imdbcito.ui.home.HomeFragment
import com.example.imdbcito.ui.search.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ELIMINAR EL ACTION BAR POR DEFECTO
        supportActionBar?.hide()

        setupBottomNavigation()

        // Cargar HomeFragment por defecto
        if (savedInstanceState == null) {
            loadHomeFragment()
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadHomeFragment()
                    true
                }
                R.id.nav_search -> {
                    loadSearchFragment()
                    true
                }
                R.id.nav_favorites -> {
                    loadFavoritesFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()
    }

    private fun loadSearchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SearchFragment())
            .commit()
    }

    private fun loadFavoritesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FavoritesFragment())
            .commit()
    }
}