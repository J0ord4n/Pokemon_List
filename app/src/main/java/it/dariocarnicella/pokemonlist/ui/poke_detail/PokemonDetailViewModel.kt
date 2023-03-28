package it.dariocarnicella.pokemonlist.ui.poke_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.dariocarnicella.pokemonlist.repository.PokemonRepository
import it.dariocarnicella.pokemonlist.ui.models.Pokemon
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val pokemonRepository: PokemonRepository) : ViewModel() {

    val pokemonInfo = MutableLiveData<Pokemon>()

    fun getPokemonInfo(id : String) = viewModelScope.launch {
        val response = pokemonRepository.getPokemonInfo(id)
        response.body()?.let{
            pokemonInfo.postValue(it)
        }
    }
}