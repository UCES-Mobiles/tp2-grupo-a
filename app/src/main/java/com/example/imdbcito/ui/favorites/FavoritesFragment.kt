package com.example.imdbcito.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdbcito.databinding.FragmentFavoritesBinding
import com.example.imdbcito.ui.detail.MovieDetailActivity

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        observeViewModel()

        viewModel.loadFavorites()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(
            onItemClick = { movie ->
                val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                    putExtra("MOVIE_ID", movie.movieId)
                    putExtra("MOVIE_TITLE", movie.title)
                }
                startActivity(intent)
            },
            onRemoveClick = { movie ->
                viewModel.removeFavorite(movie)
            },
            onToggleWatchedClick = { movie ->
                viewModel.toggleWatchedStatus(movie)
            }
        )

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            adapter.submitList(favorites)
            binding.emptyState.visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
            binding.tvFavoritesCount.text = "Favoritos: ${favorites.size}"
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null && error.isNotEmpty()) {
                android.widget.Toast.makeText(requireContext(), error, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}