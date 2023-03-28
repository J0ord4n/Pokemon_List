package it.dariocarnicella.pokemonlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dariocarnicella.pokemonlist.repository.PokemonRepository

class PokemonViewModelProviderFactory(
    val pokemonRepository: PokemonRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonViewModel(pokemonRepository) as T
    }
}