package com.example.imdbcito.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imdbcito.data.models.apirest.Movie
import com.example.imdbcito.data.repository.MovieRepository
import com.example.imdbcito.data.models.entities.movie.MovieStatsModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class MovieDetailViewModel : ViewModel() {

    private val repository = MovieRepository()

    val movie: LiveData<Movie> = repository.movie
    val error: LiveData<String> = repository.error

    private val _movieName = MutableLiveData<String>()
    val movieName: LiveData<String> = _movieName

    private val _statDisplay = MutableLiveData<MovieStatsModel>()
    val statDisplay: LiveData<MovieStatsModel> = _statDisplay

/*    private val _nextMatchDisplay = MutableLiveData<NextMatchModel?>()
    val nextMatchDisplay: LiveData<NextMatchModel> = _nextMatchDisplay as LiveData<NextMatchModel>*/

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    private val _league = MutableLiveData<String>()
    val league: LiveData<String> = _league

    fun fetchMovie(movieId: Int) {
        repository.fetchMovie(movieId)

        repository.movie.observeForever { movie ->
            movie?.let {
                _movieName.value = it.originalTitle ?: "Nombre no disponible"
                //processStats(it.record?.items?.firstOrNull())

/*                val event = it.nextEvent?.firstOrNull()
                if (event == null) {
                    processNextMatch(null)
                } else {
                    val formattedDate = formatMatchDate(event.date)
                    val nextMatch = NextMatchModel(
                        name = event.name ?: "Próximo partido no disponible",
                        formattedDate = "Fecha: $formattedDate",
                        venue = event.venue?.fullName ?: "Por definir"
                    )
                    processNextMatch(nextMatch)
                }*/
            }
        }
    }

    /*private fun processStats(record: RecordItem?) {
        val displayModel = StatDisplayModel(
            gamesPlayed = getStatValue(record, "gamesPlayed"),
            wins = getStatValue(record, "wins"),
            losses = getStatValue(record, "losses"),
            ties = getStatValue(record, "ties"),
            points = getStatValue(record, "points"),
            pointsFor = getStatValue(record, "pointsFor"),
            pointsAgainst = getStatValue(record, "pointsAgainst"),
            rank = getStatValue(record, "rank")
        )
        _statDisplay.value = displayModel
    }*/

/*    private fun getStatValue(record: RecordItem?, statName: String): String {
        return record?.stats?.find { it.name == statName }?.value?.toString() ?: "N/A"
    }

    private fun processNextMatch(nextMatch: NextMatchModel?) {
        if (nextMatch == null) {
            _nextMatchDisplay.value = NextMatchModel(
                name = "No hay próximos partidos",
                formattedDate = "Fecha: No disponible",
                venue = "Estadio: No disponible"
            )
        } else {
            _nextMatchDisplay.value = nextMatch
        }
    }*/

    private fun formatMatchDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "No disponible"
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val outputFormat = SimpleDateFormat("d 'de' MMMM, yyyy", Locale("es", "ES"))
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Formato inválido"
        }
    }
}
