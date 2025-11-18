package com.example.imdbcito.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imdbcito.databinding.FragmentSearchBinding
import com.example.imdbcito.databinding.LayoutSearchByNameBinding
import com.example.imdbcito.databinding.LayoutSearchByActorBinding
import com.example.imdbcito.databinding.LayoutSearchByGenreBinding
import com.example.imdbcito.databinding.LayoutSearchByDecadeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.imdbcito.ui.detail.MovieDetailActivity
import android.content.Intent
import android.widget.AutoCompleteTextView

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchAdapter

    // Variables para los layouts dinámicos
    private var nameBinding: LayoutSearchByNameBinding? = null
    private var actorBinding: LayoutSearchByActorBinding? = null
    private var genreBinding: LayoutSearchByGenreBinding? = null
    private var decadeBinding: LayoutSearchByDecadeBinding? = null

    private val searchTypes = listOf(
        "Por Nombre",
        "Por Actor/Actriz",
        "Por Género",
        "Por Década"
    )

    private val decades = listOf(
        "1950s", "1960s", "1970s", "1980s", "1990s",
        "2000s", "2010s", "2020s"
    )

    private val genres = listOf(
        "Acción", "Aventura", "Animación", "Comedia", "Crimen",
        "Documental", "Drama", "Familia", "Fantasía", "Historia",
        "Terror", "Música", "Misterio", "Romance", "Ciencia ficción",
        "Película de TV", "Suspenso", "Bélica", "Western"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupSearchTypeSelector()
        setupListeners()
        observeViewModel()

        // Cargar búsqueda por nombre por defecto
        showSearchByName()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter { movie ->
            val intent = Intent(requireContext(), MovieDetailActivity::class.java).apply {
                putExtra("MOVIE_ID", movie.id)
                putExtra("MOVIE_TITLE", movie.title)
            }
            startActivity(intent)
        }

        binding.recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewResults.adapter = adapter
    }

    private fun setupSearchTypeSelector() {
        binding.tvSearchType.text = searchTypes[0]
    }

    private fun setupListeners() {
        binding.tvSearchType.setOnClickListener {
            showSearchTypeDialog()
        }
    }

    private fun showSearchTypeDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val dialogView = layoutInflater.inflate(com.example.imdbcito.R.layout.dialog_search_types, null)

        val listView = dialogView.findViewById<android.widget.ListView>(com.example.imdbcito.R.id.listViewSearchTypes)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, searchTypes)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            binding.tvSearchType.text = searchTypes[position]
            when (position) {
                0 -> showSearchByName()
                1 -> showSearchByActor()
                2 -> showSearchByGenre()
                3 -> showSearchByDecade()
            }
            dialog.dismiss()
        }

        dialog.setContentView(dialogView)
        dialog.show()
    }

    private fun showSearchByName() {
        clearSearchContent()
        nameBinding = LayoutSearchByNameBinding.inflate(layoutInflater)
        binding.searchContentContainer.addView(nameBinding?.root)

        nameBinding?.btnSearchByName?.setOnClickListener {
            val query = nameBinding?.etMovieName?.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchMovies(query)
            } else {
                android.widget.Toast.makeText(requireContext(), "Ingresa un nombre de película", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSearchByActor() {
        clearSearchContent()
        actorBinding = LayoutSearchByActorBinding.inflate(layoutInflater)
        binding.searchContentContainer.addView(actorBinding?.root)

        actorBinding?.btnSearchByActor?.setOnClickListener {
            val query = actorBinding?.etActorName?.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchActors(query)
            } else {
                android.widget.Toast.makeText(requireContext(), "Ingresa un nombre de actor/actriz", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSearchByGenre() {
        clearSearchContent()
        genreBinding = LayoutSearchByGenreBinding.inflate(layoutInflater)
        binding.searchContentContainer.addView(genreBinding?.root)

        // Configurar autocompletado
        val genreAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, genres)
        genreBinding?.etGenre?.setAdapter(genreAdapter)

        genreBinding?.btnSearchByGenre?.setOnClickListener {
            val query = genreBinding?.etGenre?.text.toString().trim()
            if (query.isNotEmpty()) {
                if (genres.contains(query)) {
                    viewModel.searchByGenre(query)
                } else {
                    android.widget.Toast.makeText(requireContext(), "Género no válido. Usa la lista de sugerencias.", android.widget.Toast.LENGTH_LONG).show()
                }
            } else {
                android.widget.Toast.makeText(requireContext(), "Selecciona o escribe un género", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSearchByDecade() {
        clearSearchContent()
        decadeBinding = LayoutSearchByDecadeBinding.inflate(layoutInflater)
        binding.searchContentContainer.addView(decadeBinding?.root)

        // Configurar spinner de décadas
        val decadeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, decades)
        decadeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        decadeBinding?.spinnerDecade?.adapter = decadeAdapter

        decadeBinding?.btnSearchByDecade?.setOnClickListener {
            val selectedDecade = decadeBinding?.spinnerDecade?.selectedItem.toString()
            viewModel.searchByDecade(selectedDecade)
        }
    }

    private fun clearSearchContent() {
        // SOLUCIÓN AL CRASH: Verificar que el binding no sea nulo
        if (_binding != null) {
            binding.searchContentContainer.removeAllViews()
        }
        nameBinding = null
        actorBinding = null
        genreBinding = null
        decadeBinding = null
    }

    private fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
            binding.tvResultsCount.text = "Resultados: ${movies.size}"
            binding.emptyState.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                android.widget.Toast.makeText(requireContext(), error, android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // SOLUCIÓN AL CRASH: Limpiar primero el contenido y luego el binding
        clearSearchContent()
        _binding = null
    }
}