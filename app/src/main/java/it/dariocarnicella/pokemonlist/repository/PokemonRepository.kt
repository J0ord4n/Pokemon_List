package it.dariocarnicella.pokemonlist.repository

import it.dariocarnicella.pokemonlist.api.RetrofitInstance

class PokemonRepository {
    suspend fun getPokemonList(limit : Int, offset : Int) =
        RetrofitInstance.api.getPokemonList(limit, offset)

    suspend fun getPokemonInfo(id : String) =
        RetrofitInstance.api.getPokemonInfo(id)
}