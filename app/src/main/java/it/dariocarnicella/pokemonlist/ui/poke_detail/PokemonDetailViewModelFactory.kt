package it.dariocarnicella.pokemonlist.ui.poke_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.dariocarnicella.pokemonlist.repository.PokemonRepository

class PokemonDetailViewModelFactory(val pokemonRepsitory : PokemonRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonDetailViewModel(pokemonRepsitory) as T
    }
}