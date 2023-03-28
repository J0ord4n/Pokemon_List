package it.dariocarnicella.pokemonlist.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.dariocarnicella.pokemonlist.repository.PokemonRepository
import it.dariocarnicella.pokemonlist.ui.models.Pokemon
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokemonRepository: PokemonRepository) : ViewModel() {

    val pokemonList : MutableLiveData<Pokemon> = MutableLiveData()

    init {
        getPokemonList(100, 0)
    }

    private fun getPokemonList(limit : Int, offset : Int) = viewModelScope.launch {
        val response = pokemonRepository.getPokemonList(limit, offset)
        response.body()?.let {
            pokemonList.postValue(it)
        }
    }
}